package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lawencon.elearning.constant.ExtensionDocument;
import com.lawencon.elearning.dao.LearningMaterialsDao;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.LearningMaterialTypes;
import com.lawencon.elearning.model.LearningMaterials;
import com.lawencon.elearning.model.Users;

@Service
public class LearningMaterialsServiceImpl extends ElearningBaseServiceImpl implements LearningMaterialsService {

	@Autowired
	private LearningMaterialsDao learningMaterialsDao;

	@Autowired
	private DetailModuleRegistrationsService dtlModuleRgsService;

	@Autowired
	private LearningMaterialTypesService learningMaterialTypesService;

	@Autowired
	private FilesService filesService;
	
	@Autowired
	private UsersService userService;

	@Override
	public void insert(DetailModuleRegistrations dtlModuleRgs, MultipartFile fileInput) throws Exception {
		try {
			begin();
			Files file = new Files();
			file.setCreatedBy(dtlModuleRgs.getIdLearningMaterial().getCreatedBy());
			file.setFile(fileInput.getBytes());
			file.setType(fileInput.getContentType());
			file.setName(fileInput.getOriginalFilename());
			filesService.insert(file);
			LearningMaterials learningMaterial = dtlModuleRgs.getIdLearningMaterial();
			learningMaterial.setIdFile(file);
			dtlModuleRgs.setIdLearningMaterial(learningMaterial);
			learningMaterialsDao.insert(dtlModuleRgs.getIdLearningMaterial(), () -> {
				validate(dtlModuleRgs.getIdLearningMaterial());
				validateInsert(dtlModuleRgs.getIdLearningMaterial());
				validateFileLearningMaterial(dtlModuleRgs.getIdLearningMaterial());
			});
			insertDetailModulRegistration(dtlModuleRgs);
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void update(DetailModuleRegistrations dtlModuleRgs, MultipartFile fileInput) throws Exception {
		try {
			begin();
			Files file = filesService.getById(dtlModuleRgs.getIdLearningMaterial().getIdFile().getId());
			if (fileInput != null && !fileInput.isEmpty()) {
				file.setFile(fileInput.getBytes());
				file.setType(fileInput.getContentType());
				file.setName(fileInput.getOriginalFilename());
				file.setUpdatedBy(file.getCreatedBy());
				filesService.update(file);
				dtlModuleRgs.getIdLearningMaterial().setIdFile(file);
			}
			LearningMaterials material = learningMaterialsDao
					.getMaterialById(dtlModuleRgs.getIdLearningMaterial().getId());
			dtlModuleRgs.getIdLearningMaterial().setCreatedAt(material.getCreatedAt());
			dtlModuleRgs.getIdLearningMaterial().setCreatedBy(material.getCreatedBy());
			dtlModuleRgs.getIdLearningMaterial().setUpdatedBy(material.getCreatedBy());
			dtlModuleRgs.getIdLearningMaterial().setIdFile(material.getIdFile());
			learningMaterialsDao.update(dtlModuleRgs.getIdLearningMaterial(), () -> {
				validate(dtlModuleRgs.getIdLearningMaterial());
				validateUpdate(dtlModuleRgs.getIdLearningMaterial());
				validateFileLearningMaterial(dtlModuleRgs.getIdLearningMaterial());
			});
			dtlModuleRgsService.update(dtlModuleRgs);
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		try {
			begin();
			verifyNull(id, "Id Learning Material tidak boleh kosong");
			LearningMaterials lm = learningMaterialsDao.getMaterialById(id);
			verifyNull(lm, "Id Learning Material tidak ada");
			verifyNull(idUser, "Updated by tidak boleh kosong");
			Users user = userService.getById(idUser);
			verifyNull(user, "Id User tidak ada");
			if (checkDelete(id) == false) {
				learningMaterialsDao.softDeleteMaterialById(id, idUser);
			} else {
				validateDelete();
			}
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<LearningMaterials> getAll() throws Exception {
		return learningMaterialsDao.getAllMaterials();
	}

	@Override
	public LearningMaterials getById(String id) throws Exception {
		return learningMaterialsDao.getMaterialById(id);
	}

	@Override
	public LearningMaterials getByCode(String code) throws Exception {
		return learningMaterialsDao.getMaterialByCode(code);
	}

	@Override
	public LearningMaterials getByIdDetailModuleRgs(String idDtlModuleRgs) throws Exception {
		DetailModuleRegistrations detailModuleRgs = dtlModuleRgsService.getDtlModuleRgsById(idDtlModuleRgs);
		return learningMaterialsDao.getMaterialById(detailModuleRgs.getIdLearningMaterial().getId());
	}

	private void insertDetailModulRegistration(DetailModuleRegistrations dtlModuleRgs) throws Exception {
		dtlModuleRgs.setCreatedBy(dtlModuleRgs.getIdLearningMaterial().getCreatedBy());
		dtlModuleRgs.setIdLearningMaterial(dtlModuleRgs.getIdLearningMaterial());
		dtlModuleRgsService.insert(dtlModuleRgs);
	}

	private void validate(LearningMaterials learningMaterial) throws Exception {
		verifyNull(learningMaterial.getIdLearningMaterialType(), "Learning material type tidak boleh kosong");

		LearningMaterialTypes materialType = learningMaterialTypesService
				.getById(learningMaterial.getIdLearningMaterialType().getId());

		verifyNull(materialType, "Id tipe bahan ajar tidak ada!");

		verifyNullAndEmptyString(learningMaterial.getLearningMaterialName(), "Nama bahan ajar tidak boleh kosong!");

		verifyNullAndEmptyString(learningMaterial.getDescription(), "Deskripsi bahan ajar tidak boleh kosong!");
	}

	private void validateInsert(LearningMaterials learningMaterial) throws Exception {
		verifyNullAndEmptyString(learningMaterial.getCode(), "Kode bahan ajar tidak boleh kosong!");
		LearningMaterials learningMaterials = getByCode(learningMaterial.getCode());
		verifyNull(!verifyNull(learningMaterials) ? null : false, "Kode bahan ajar tidak boleh sama!");
	}

	private void validateUpdate(LearningMaterials learningMaterial) throws Exception {
		verifyNullAndEmptyString(learningMaterial.getId(), "Id kelas tidak boleh kosong!");

		LearningMaterials lm = getById(learningMaterial.getId());

		verifyNull(learningMaterial.getVersion(), "Kelas version tidak boleh kosong!");
		if (learningMaterial.getVersion() != lm.getVersion()) {
			throw new Exception("Kelas version tidak sama!");
		}
		verifyNullAndEmptyString(learningMaterial.getCode(), "Kode bahan ajar tidak boleh kosong!");
		if (!learningMaterial.getCode().equalsIgnoreCase(lm.getCode())) {
			LearningMaterials learningMaterials = getByCode(learningMaterial.getCode());
			verifyNull(!verifyNull(learningMaterials) ? null : false, "Kode bahan ajar tidak boleh sama!");
		}
	}

	private boolean checkDelete(String id) throws Exception {
		List<?> listObj = learningMaterialsDao.validateDeleteMaterial(id);
		List<?> list = listObj.stream().filter(val -> val != null).collect(Collectors.toList());
		return list.size() > 0 ? true : false;
	}

	private void validateDelete() throws Exception {
		throw new Exception("Bahan ajar telah digunakan dalam kelangsungan kelas !");
	}

	private void validateFileLearningMaterial(LearningMaterials learningMaterial) throws Exception {
		String[] type = learningMaterial.getIdFile().getType().split("/");
		String ext = type[1];
		if (ext != null) {
			if (ext.equalsIgnoreCase(ExtensionDocument.DOC.code) || ext.equalsIgnoreCase(ExtensionDocument.DOCX.code)
					|| ext.equalsIgnoreCase(ExtensionDocument.PDF.code)
					|| ext.equalsIgnoreCase(ExtensionDocument.ODT.code)
					|| ext.equalsIgnoreCase(ExtensionDocument.WPS.code)
					|| ext.equalsIgnoreCase(ExtensionDocument.PPT.code)
					|| ext.equalsIgnoreCase(ExtensionDocument.PPTX.code)
					|| ext.equalsIgnoreCase(ExtensionDocument.TXT.code)) {
			} else {
				throw new Exception("File harus dokumen!");
			}
		}
	}
}
