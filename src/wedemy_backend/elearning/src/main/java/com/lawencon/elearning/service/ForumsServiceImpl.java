package com.lawencon.elearning.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.ForumsDao;
import com.lawencon.elearning.helper.ForumAndDetailForums;
import com.lawencon.elearning.model.DetailForums;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.Forums;
import com.lawencon.elearning.model.Users;

@Service
public class ForumsServiceImpl extends ElearningBaseServiceImpl implements ForumsService {

	@Autowired
	private ForumsDao forumsDao;

	@Autowired
	private DetailForumsService detailForumService;

	@Autowired
	private DetailModuleRegistrationsService dtlModuleRgsService;

	@Autowired
	private UsersService usersService;

	@Override
	public void insertForum(Forums forum) throws Exception {
		forum.setCreatedBy(forum.getIdUser().getId());
		forum.setForumDateTime(LocalDateTime.now());
		forum.setTrxNumber(generateTrxNumber(TransactionNumberCode.FORUM.code));
		forumsDao.insertForum(forum, () -> validateInsert(forum));
	}

	@Override
	public void deleteForumByIdDetailModuleRegistration(String idDetailModuleRegistration, String idUser)
			throws Exception {
		begin();
		List<Forums> forumList = forumsDao.getForumByIdDetailModuleRegistration(idDetailModuleRegistration);
		for (Forums forum : forumList) {
			forumsDao.softDeleteForumById(forum.getId(), idUser);
			List<DetailForums> detailForumList = detailForumService.getAllDetailForumsByIdForum(forum.getId());
			for (DetailForums detailForum : detailForumList) {
				detailForumService.softDeleteDetailForumById(detailForum.getId(), idUser);
			}
		}
		commit();
	}

	@Override
	public Forums getForumById(String id) throws Exception {
		return forumsDao.getForumById(id);
	}

	@Override
	public List<Forums> getAllForums() throws Exception {
		return forumsDao.getAllForums();
	}

	@Override
	public List<ForumAndDetailForums> getForumByIdDetailModuleRegistration(String id) throws Exception {
		verifyNullAndEmptyString(id, "Id Detail Module Registration tidak boleh kosong");
		DetailModuleRegistrations detailModuleRgs = dtlModuleRgsService.getDtlModuleRgsById(id);
		verifyNull(detailModuleRgs, "Id Detail Module Registration tidak ada");
		List<ForumAndDetailForums> listResult = new ArrayList<>();
		List<Forums> forums = forumsDao.getForumByIdDetailModuleRegistration(id);
		for (Forums forum : forums) {
			List<DetailForums> detailForums = detailForumService.getAllDetailForumsByIdForum(forum.getId());
			ForumAndDetailForums result = new ForumAndDetailForums();
			result.setForum(forum);
			result.setDetailForums(detailForums);
			listResult.add(result);
		}
		return listResult;
	}

	private void validateInsert(Forums forum) throws Exception {
		verifyNull(forum.getContentText(), "Konten forum tidak boleh kosong");

		verifyNull(forum.getIdDetailModuleRegistration(), "Id Detail Module Registration tidak boleh kosong");

		DetailModuleRegistrations dtlModuleRgs = dtlModuleRgsService
				.getDtlModuleRgsById(forum.getIdDetailModuleRegistration().getId());
		verifyNull(dtlModuleRgs, "Id Detail Module Registration salah");

		verifyNull(forum.getIdUser(), "Id User tidak boleh kosong");
		
		Users user = usersService.getById(forum.getIdUser().getId());
		verifyNull(user, "Id User salah");
	}

}
