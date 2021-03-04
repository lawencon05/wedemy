package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lawencon.base.BaseServiceImpl;
import com.lawencon.elearning.constant.ExtensionDocument;
import com.lawencon.elearning.dao.LearningMaterialsDao;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.LearningMaterialTypes;
import com.lawencon.elearning.model.LearningMaterials;

@Service
public class LearningMaterialsServiceImpl extends BaseServiceImpl implements LearningMaterialsService {

	@Autowired
	private LearningMaterialsDao learningMaterialsDao;

	@Autowired
	private DetailModuleRegistrationsService dtlModuleRgsService;

	@Autowired
	private LearningMaterialTypesService learningMaterialTypesService;

	@Autowired
	private FilesService filesService;

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
		if (learningMaterial.getIdLearningMaterialType() == null) {
			LearningMaterialTypes materialType = learningMaterialTypesService
					.getById(learningMaterial.getIdLearningMaterialType().getId());
			if (materialType == null) {
				throw new Exception("Id tipe bahan ajar tidak ada!");
			}
		}
		if (learningMaterial.getLearningMaterialName() == null
				|| learningMaterial.getLearningMaterialName().trim().equals("")) {
			throw new Exception("Nama bahan ajar tidak boleh kosong!");
		}
		if (learningMaterial.getDescription() == null || learningMaterial.getDescription().trim().equals("")) {
			throw new Exception("Deskripsi bahan ajar tidak boleh kosong!");
		}
	}

	private void validateInsert(LearningMaterials learningMaterial) throws Exception {
		if (learningMaterial.getCode() == null || learningMaterial.getCode().trim().equals("")) {
			throw new Exception("Kode bahan ajar tidak boleh kosong!");
		} else {
			LearningMaterials learningMaterials = getByCode(learningMaterial.getCode());
			if (learningMaterials != null) {
				throw new Exception("Kode bahan ajar tidak boleh sama!");
			}
		}
	}

	private void validateUpdate(LearningMaterials learningMaterial) throws Exception {
		if (learningMaterial.getId() == null || learningMaterial.getId().trim().equals("")) {
			throw new Exception("Id kelas tidak boleh kosong!");
		} else {
			LearningMaterials lm = getById(learningMaterial.getId());
			if (learningMaterial.getVersion() == null) {
				throw new Exception("Kelas version tidak boleh kosong!");
			} else {
				if (learningMaterial.getVersion() != lm.getVersion()) {
					throw new Exception("Kelas version tidak sama!");
				} 
				if (learningMaterial.getCode() == null || learningMaterial.getCode().trim().equals("")) {
					throw new Exception("Kode bahan ajar tidak boleh kosong!");
				} else {
					if (!learningMaterial.getCode().equalsIgnoreCase(lm.getCode())) {
						LearningMaterials learningMaterials = getByCode(learningMaterial.getCode());
						if (learningMaterials != null) {
							throw new Exception("Kode bahan ajar tidak boleh sama!");
						}
					}
				}
			}
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
		System.out.println("cek type materi" + learningMaterial.getIdFile().getType());
		String[] type = learningMaterial.getIdFile().getType().split("/");
		String ext = type[1];
		System.out.println("extension" + ext);
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
