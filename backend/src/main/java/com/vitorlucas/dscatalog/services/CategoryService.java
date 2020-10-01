package com.vitorlucas.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vitorlucas.dscatalog.dto.CategoryDTO;
import com.vitorlucas.dscatalog.entities.Category;
import com.vitorlucas.dscatalog.repositories.CategoryRepository;
import com.vitorlucas.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repositoy;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repositoy.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

		/*
		 * List<CategoryDTO> listDTO = new ArrayList<>(); for (Category cat : list) {
		 * listDTO.add(new CategoryDTO(cat)); } return listDTO;
		 */
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repositoy.findById(id);
		Category cat = obj.orElseThrow(() -> new ResourceNotFoundException("Category not Found"));
		return new CategoryDTO(cat);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO catDTO) {
		Category cat = new Category();
		cat.setName(catDTO.getName());
		cat = repositoy.save(cat);
		return new CategoryDTO(cat);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO catDTO) {
		try {
			Category entity = repositoy.getOne(id);
			entity.setName(catDTO.getName());
			entity = repositoy.save(entity);
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found :" + id);
		}
	}
}
