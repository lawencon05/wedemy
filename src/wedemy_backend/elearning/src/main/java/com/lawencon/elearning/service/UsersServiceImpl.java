package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.RoleCode;
import com.lawencon.elearning.constant.TemplateEmail;
import com.lawencon.elearning.dao.UsersDao;
import com.lawencon.elearning.model.General;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Roles;
import com.lawencon.elearning.model.Users;

import net.bytebuddy.utility.RandomString;

@Service
public class UsersServiceImpl extends ElearningBaseServiceImpl implements UsersService {

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ProfilesService profilesService;

	@Autowired
	private RolesService rolesService;

	@Autowired
	private GeneralService generalService;

	@Override
	public void insert(Users user) throws Exception {
		try {
			begin();
			usersDao.insert(user, () -> {
				validateInsert(user);
				Roles role = rolesService.getByCode(user.getIdRole().getCode());
				user.setIdRole(role);
				user.getIdProfile().setCreatedBy(user.getCreatedBy());
				profilesService.insert(user.getIdProfile());
				user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
			});
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
		if (user.getIdRole().getCode().equals(RoleCode.PARTICIPANT.code)) {
			user.setCreatedAt(user.getCreatedAt());
			user.setCreatedBy(user.getId());
			user.setUpdatedBy(user.getId());
			usersDao.update(user, null);
			user.getIdProfile().setCreatedAt(user.getCreatedAt());
			user.getIdProfile().setCreatedBy(user.getId());
			user.getIdProfile().setUpdatedBy(user.getId());
			profilesService.autoUpdateParticipant(user.getIdProfile(), null);
		}
	}

	@Override
	public void update(Users user) throws Exception {
		user.setCreatedAt(usersDao.getUserById(user.getId()).getCreatedAt());
		user.setCreatedBy(usersDao.getUserById(user.getId()).getCreatedBy());
		usersDao.update(user, () -> {
			validateUpdate(user);
			user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
		});
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		try {
			begin();
			Users user = getById(id);
			if (validateDelete(id)) {
				usersDao.softDeleteUserById(id, idUser);
				profilesService.softDeleteById(user.getIdProfile().getId(), idUser);
			} else {
				usersDao.deleteUserById(id);
				profilesService.deleteById(user.getIdProfile().getId());
			}
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Users updateUserPassword(Profiles profile) throws Exception {
		Profiles profiles = profilesService.getByEmail(profile.getEmail());
		if (profiles == null) {
			throw new Exception("Email yang diinput tidak terdaftar");
		} else {
			Users user = usersDao.getUserByIdProfile(profiles);
			String pass = generatePassword();
			System.out.println(pass);
			user.setUserPassword(passwordEncoder.encode(pass));
			usersDao.update(user, () -> validateUpdate(user));
			System.out.println("Sending mail...");
			sendEmailResetPassword(pass, profiles);
			System.out.println("Done");
			return user;
		}
	}

	@Override
	public Users getById(String id) throws Exception {
		return usersDao.getUserById(id);
	}

	@Override
	public Users getByUsername(String username) throws Exception {
		return usersDao.getUserByUsername(username);
	}

	@Override
	public Users getByIdNumber(String idNumber) throws Exception {
		return usersDao.getUserByIdNumber(idNumber);
	}

	@Override
	public Users getByIdClass(String idClass) throws Exception {
		return usersDao.getUserByIdClass(idClass);
	}

	@Override
	public List<Users> getAll() throws Exception {
		return usersDao.getAllUsers();
	}

	@Override
	public List<Users> getByRoleCode(String code) throws Exception {
		return usersDao.getUsersByRoleCode(code);
	}

	private String generatePassword() {
		RandomString random = new RandomString(5);
		String pass = random.nextString();
		return pass;
	}

	private void sendEmailResetPassword(String pass, Profiles profile) throws Exception {
		General general = generalService.getTemplateEmail(TemplateEmail.RESET_PASSWORD.code);
		String text = general.getTemplateHtml();
		text = text.replace("#1#", profile.getFullName());
		text = text.replace("#2#", pass);
		sendMail(TemplateEmail.RESET_PASSWORD, profile, text);
	}

	private void sendEmailRegister(Profiles profile) throws Exception {
		General general = generalService.getTemplateEmail(TemplateEmail.REGISTER.code);
		String text = general.getTemplateHtml();
		text = text.replace("#1#", profile.getFullName());
		sendMail(TemplateEmail.REGISTER, profile, text);
	}

	private void validateInsert(Users user) throws Exception {
		if (user.getUsername() == null || user.getUsername().trim().equals("")) {
			throw new Exception("Username tidak boleh kosong");
		} else if (user.getUsername() != null) {
			Users usr = getByUsername(user.getUsername());
			if (usr != null) {
				throw new Exception("Username sudah ada");
			} else {
				if (user.getUserPassword() == null || user.getUserPassword().trim().equals("")) {
					throw new Exception("Password tidak boleh kosong");
				} else if (user.getIdProfile().getFullName() == null
						|| user.getIdProfile().getFullName().trim().equals("")) {
					throw new Exception("Nama Lengkap tidak boleh kosong");
				} else if (user.getIdProfile().getEmail() == null || user.getIdProfile().getEmail().trim().equals("")) {
					throw new Exception("Email tidak boleh kosong");
				} else if (user.getIdProfile().getEmail() != null) {
					Profiles profile = profilesService.getByEmail(user.getIdProfile().getEmail());
					if (profile != null) {
						throw new Exception("Email sudah ada");
					}
					if (user.getIdRole().getCode() != null) {
						Roles role = rolesService.getByCode(user.getIdRole().getCode());
						if (role.getCode().equals("TTR") || role.getCode().equals("ADM")
								|| role.getCode().equals("SADM")) {
							validateInsertExceptParticipant(user);
						} else {
							System.out.println("Sending Email......");
							sendEmailRegister(user.getIdProfile());
							System.out.println("Done");
						}
					}
				}
			}
		}

	}

	private void validateInsertExceptParticipant(Users user) throws Exception {
		if (user.getIdProfile().getIdNumber() == null || user.getIdProfile().getIdNumber().trim().equals("")) {
			throw new Exception("Nomor Kartu Penduduk tidak boleh kosong");
		} else {
			String regex = "\\d+";
			if(!user.getIdProfile().getIdNumber().matches(regex)) {
				throw new Exception("Nomor Kartu Penduduk tidak sesuai");
			}
		}
		if (user.getIdProfile().getBirthPlace() == null
				|| user.getIdProfile().getBirthPlace().trim().equals("")) {
			throw new Exception("Tempat Lahir tidak boleh kosong");
		}
		if (user.getIdProfile().getBirthDate() == null
				|| user.getIdProfile().getBirthDate().toString().trim().equals("")) {
			throw new Exception("Tanggal Lahir tidak boleh kosong");
		}
		if (user.getIdProfile().getPhone() == null) {
			throw new Exception("Nomor Handphone tidak boleh kosong");
		} else {
			String regex = "\\d+";
			if(!user.getIdProfile().getPhone().matches(regex)) {
				throw new Exception("Nomor Handphone tidak sesuai");
			}
		}
		if (user.getIdProfile().getEmail() == null) {
			throw new Exception("Email tidak boleh kosong");
		}
	}

	private void validateUpdate(Users user) throws Exception {
		if (user.getId() == null || user.getId().trim().equals("")) {
			throw new Exception("Id user tidak boleh kosong");
		} else {
			Users usr = getById(user.getId());
			if (user.getUsername() == null || user.getUsername().trim().equals("")) {
				throw new Exception("Username tidak boleh kosong");
			}
			if (usr.getVersion() != user.getVersion()) {
				throw new Exception("User yang diedit telah diperbarui, silahkan coba lagi");
			}
			if (user.getUserPassword() == null || user.getUserPassword().trim().equals("")) {
				throw new Exception("Password tidak boleh kosong");
			} else {
				if (passwordEncoder.matches(user.getUserPassword(), usr.getUserPassword())) {
					throw new Exception("Password tidak boleh sama dengan sebelumnya");
				}
			}
		}
	}

	private boolean validateDelete(String idUser) throws Exception {
		List<?> listObj = usersDao.validateDeleteUser(idUser);
		listObj.forEach(System.out::println);
		List<?> list = listObj.stream().filter(val -> val != null).collect(Collectors.toList());
		System.out.println(list.size());
		return list.size() > 0 ? true : false;
	}
}
