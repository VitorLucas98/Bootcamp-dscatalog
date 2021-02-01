package com.vitorlucas.dscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vitorlucas.dscatalog.dto.CategoryDTO;
import com.vitorlucas.dscatalog.dto.ProductDTO;
import com.vitorlucas.dscatalog.entities.Category;
import com.vitorlucas.dscatalog.entities.Product;
import com.vitorlucas.dscatalog.repositories.CategoryRepository;
import com.vitorlucas.dscatalog.repositories.ProductRepository;
import com.vitorlucas.dscatalog.services.exceptions.DatabaseException;
import com.vitorlucas.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repositoy;
	
	@Autowired
	private CategoryRepository categoryRepositoy;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, PageRequest pageRequest) {
		List<Category> categories = (categoryId == 0) ? null :Arrays.asList(categoryRepositoy.getOne(categoryId));
		Page<Product> list = repositoy.find(categories, name, pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repositoy.findById(id);
		Product cat = obj.orElseThrow(() -> new ResourceNotFoundException("Product not Found"));
		return new ProductDTO(cat, cat.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repositoy.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repositoy.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repositoy.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found :" + id);
		}
	}

	public void delete(Long id) {
		try {
			repositoy.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found :" + id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());
		
		for (CategoryDTO catDTO : dto.getCategories()) {
			Category cat = categoryRepositoy.getOne(catDTO.getId());
			entity.getCategories().add(cat);
		}
		
	}
}
