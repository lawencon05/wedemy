package com.lawencon.elearning.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.DetailForumsDao;
import com.lawencon.elearning.model.DetailForums;
import com.lawencon.elearning.model.Forums;
import com.lawencon.elearning.model.Users;

@Service
public class DetailForumsServiceImpl extends ElearningBaseServiceImpl implements DetailForumsService {

	@Autowired
	private DetailForumsDao detailForumDao;

	@Autowired
	private UsersService usersService;

	@Autowired
	private ForumsService forumService;

	@Override
	public void insertDetailForum(DetailForums detailForum) throws Exception {
		detailForum.setCreatedBy(detailForum.getIdUser().getId());
		detailForum.setDtlForumDateTime(LocalDateTime.now());
		detailForum.setTrxNumber(generateTrxNumber(TransactionNumberCode.DETAIL_FORUM.code));
		detailForumDao.insertDetailForum(detailForum, () -> validateInsert(detailForum));
	}

	@Override
	public DetailForums getDetailForumById(String id) throws Exception {
		return detailForumDao.getDetailForumById(id);
	}

	@Override
	public DetailForums getDetailForumByCode(String code) throws Exception {
		return detailForumDao.getDetailForumByCode(code);
	}

	@Override
	public List<DetailForums> getAllDetailForums() throws Exception {
		return detailForumDao.getAllDetailForums();
	}

	@Override
	public void softDeleteDetailForumById(String id, String idUser) throws Exception {
		detailForumDao.softDeleteDetailForumById(id, idUser);
	}

	@Override
	public List<DetailForums> getAllDetailForumsByIdForum(String idForum) throws Exception {
		return detailForumDao.getAllDetailForumsByIdForum(idForum);
	}

	private void validateInsert(DetailForums detailForum) throws Exception {
		if (detailForum.getContentText() == null) {
			throw new Exception("Konten forum tidak boleh kosong");
		}
		if (detailForum.getIdUser() != null) {
			Users user = usersService.getById(detailForum.getIdUser().getId());
			if (user == null) {
				throw new Exception("Id User salah");
			}
		} else {
			throw new Exception("Id User tidak boleh kosong");
		}
		if (detailForum.getIdForum() != null) {
			Forums forum = forumService.getForumById(detailForum.getIdForum().getId());
			if (forum == null) {
				throw new Exception("Id Forum salah");
			}
		} else {
			throw new Exception("Id Forum tidak boleh kosong");
		}
	}

}
