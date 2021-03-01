package com.lawencon.elearning.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lawencon.base.BaseServiceImpl;
import com.lawencon.elearning.constant.ExtensionImage;
import com.lawencon.elearning.dao.ProfilesDao;
import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.model.Evaluations;
import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.util.Callback;

@Service
public class ProfilesServiceImpl extends BaseServiceImpl implements ProfilesService {

	@Autowired
	private ProfilesDao profilesDao;

	@Autowired
	private FilesService filesService;

	@Override
	public void insert(Profiles profile) throws Exception {
		Files file = new Files();
		filesService.insert(file);
		profile.setIdFile(file);
		profilesDao.insert(profile, null);
	}

	@Override
	public void update(Profiles profile, MultipartFile file) throws Exception {
		try {
			begin();
			Files profilePict = filesService.getById(profile.getIdFile().getId());
			if (file != null && !file.isEmpty()) {
				profilePict.setFile(file.getBytes());
				profilePict.setType(file.getContentType());
				profilePict.setName(file.getOriginalFilename());
				profilePict.setCreatedAt(profilePict.getCreatedAt());
				profilePict.setCreatedBy(profilePict.getCreatedBy());
				filesService.update(profilePict);
				profile.setIdFile(profilePict);
			} else {
				profile.setIdFile(profilePict);
			}
			profile.setCreatedAt(profilesDao.getProfileById(profile.getId()).getCreatedAt());
			profile.setCreatedBy(profilesDao.getProfileById(profile.getId()).getCreatedBy());
			profilesDao.update(profile, () -> {
				validateUpdate(profile);
			});
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void autoUpdateParticipant(Profiles profile, Callback before) throws Exception {
		profilesDao.autoUpdateParticipant(profile, before);
	}

	@Override
	public void deleteById(String id) throws Exception {
		profilesDao.deleteProfileById(id);
	}

	@Override
	public void softDeleteById(String id, String idUser) throws Exception {
		profilesDao.softDeleteProfileById(id, idUser);
	}

	@Override
	public Profiles getById(String id) throws Exception {
		return profilesDao.getProfileById(id);
	}

	@Override
	public Profiles getByEmail(String email) throws Exception {
		return profilesDao.getProfileByEmail(email);
	}

	@Override
	public Profiles getByIdNumber(String idNumber) throws Exception {
		return profilesDao.getProfileByIdNumber(idNumber);
	}

	@Override
	public Profiles getTutorProfileByIdDtlModuleRgs(AssignmentSubmissions assignmentSubmission) throws Exception {
		return profilesDao.getTutorProfileByIdDtlModuleRgs(assignmentSubmission);
	}

	@Override
	public Profiles getParticipantProfileByIdDtlModuleRgs(AssignmentSubmissions assignmentSubmission) throws Exception {
		return profilesDao.getParticipantProfileByIdDtlModuleRgs(assignmentSubmission);
	}

	@Override
	public Profiles getParticipantProfileById(Evaluations evaluation) throws Exception {
		return profilesDao.getParticipantProfileById(evaluation);
	}

	@Override
	public List<Profiles> getAll() throws Exception {
		return profilesDao.getAllProfiles();
	}

	private void validateUpdate(Profiles profile) throws Exception {
		if (profile.getId() == null || profile.getId().trim().equals("")) {
			throw new Exception("Id tidak boleh kosong");
		} else {
			Profiles pfl = getById(profile.getId());
			if (profile.getFullName() == null || profile.getFullName().trim().equals("")) {
				throw new Exception("Nama Lengkap tidak boleh kosong");
			}
			if (pfl.getVersion() != profile.getVersion()) {
				throw new Exception("Profile yang diedit telah diperbarui, silahkan coba lagi");
			}
			if (profile.getIdFile() != null) {
				if (profile.getIdFile().getType() != null) {
					String[] type = profile.getIdFile().getType().split("/");
					String ext = type[1];
					if (ext != null) {
						if (ext.equalsIgnoreCase(ExtensionImage.PNG.code)
								|| ext.equalsIgnoreCase(ExtensionImage.JPG.code)
								|| ext.equalsIgnoreCase(ExtensionImage.JPEG.code)) {

						} else {
							throw new Exception("File harus gambar");
						}
					}
				}
			}
			if (profile.getPhone() != null) {
				if (profile.getPhone().length() <= 10 && profile.getPhone().length() >= 13) {
					Pattern pattern = Pattern.compile("\\d{10, 13}");
					Matcher matcher = pattern.matcher(profile.getPhone());
					if (matcher.matches()) {
						throw new Exception("Nomor handphone tidak sesuai!");
					}
				}
			}
			if (profile.getIdNumber() != null) {
				if (profile.getIdNumber().length() > 16) {
					throw new Exception("Nomor identitas tidak sesuai!");
				} else {
					String regex = "\\d+";
					if(!profile.getIdNumber().matches(regex)) {
						throw new Exception("Nomor identitas harus angka");
					}
				}
			}
		}
	}

}
