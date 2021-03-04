package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.base.BaseServiceImpl;
//import com.lawencon.elearning.dao.AssignmentTypesDao;
import com.lawencon.elearning.dao.LearningMaterialTypesDao;
import com.lawencon.elearning.model.LearningMaterialTypes;

@Service
public class LearningMaterialTypesServiceImpl extends BaseServiceImpl implements LearningMaterialTypesService {

	@Autowired
	private LearningMaterialTypesDao typesDao;

	@Override
	public void insert(LearningMaterialTypes lmType) throws Exception {
		typesDao.insert(lmType, () -> validateInsert(lmType));
	}

	@Override
	public void update(LearningMaterialTypes lmType) throws Exception {
		lmType.setCreatedAt(typesDao.getTypeById(lmType.getId()).getCreatedAt());
		lmType.setCreatedBy(typesDao.getTypeById(lmType.getId()).getCreatedBy());
		typesDao.update(lmType, () -> validateUpdate(lmType));
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		try {
			begin();
			if (validateDelete(id)) {
				typesDao.softDeleteTypeById(id, idUser);
			} else {
				typesDao.deleteTypeById(id);
			}
			commit();
		} catch (Exception e) {
			e.getMessage();
			rollback();
		}
	}

	@Override
	public LearningMaterialTypes getById(String id) throws Exception {
		return typesDao.getTypeById(id);
	}

	@Override
	public LearningMaterialTypes getByCode(String code) throws Exception {
		return typesDao.getTypeByCode(code);
	}

	@Override
	public List<LearningMaterialTypes> getAll() throws Exception {
		return typesDao.getAllTypes();
	}

	private void validateInsert(LearningMaterialTypes learningMaterialTypes) throws Exception {
		if (learningMaterialTypes.getCode() == null || learningMaterialTypes.getCode().trim().equals("")) {
			throw new Exception("Kode tipe bahan ajar tidak boleh kosong!");
		} else {
			LearningMaterialTypes learningMaterialType = getByCode(learningMaterialTypes.getCode());
			if (learningMaterialType != null) {
				throw new Exception("Kode tipe bahan ajar tidak boleh sama!");
			} else {
				if (learningMaterialTypes.getLearningMaterialTypeName() == null
						|| learningMaterialTypes.getLearningMaterialTypeName().trim().equals("")) {
					throw new Exception("Nama tipe bahan ajar tidak boleh kosong!");
				}
			}
		}
	}

	private void validateUpdate(LearningMaterialTypes learningMaterialTypes) throws Exception {
		if (learningMaterialTypes.getId() == null || learningMaterialTypes.getId().trim().equals("")) {
			throw new Exception("Id tipe bahan ajar tidak boleh kosong!");
		} else {
			LearningMaterialTypes learningType = getById(learningMaterialTypes.getId());
			if (learningType == null) {
				throw new Exception("Id tipe bahan ajar tidak ada!");
			} else {
				if (learningType.getVersion() == null) {
					throw new Exception("Tipe bahan ajar version tidak boleh kosong!");
				} else {
					if (learningMaterialTypes.getVersion() != learningType.getVersion()) {
						throw new Exception("Tipe bahan ajar version tidak sama!");
					} else {
						if (learningMaterialTypes.getCode() == null
								|| learningMaterialTypes.getCode().trim().equals("")) {
							throw new Exception("Kode tipe bahan ajar tidak boleh kosong!");
						} else {
							if (!learningMaterialTypes.getCode().equalsIgnoreCase(learningType.getCode())) {
								LearningMaterialTypes lmType = getByCode(learningMaterialTypes.getCode());
								if (lmType != null) {
									throw new Exception("Kode tipe bahan ajar tidak boleh sama!");
								}
							} else {
								if (learningMaterialTypes.getLearningMaterialTypeName() == null
										|| learningMaterialTypes.getLearningMaterialTypeName().trim().equals("")) {
									throw new Exception("Name tipe bahan ajar tidak boleh kosong!");
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean validateDelete(String id) throws Exception {
		List<?> listObj = typesDao.validateDelete(id);
		listObj.forEach(System.out::println);
		List<?> list = listObj.stream().filter(val -> val != null).collect(Collectors.toList());
		System.out.println(list.size());
		return list.size() > 0 ? true : false;
	}
}