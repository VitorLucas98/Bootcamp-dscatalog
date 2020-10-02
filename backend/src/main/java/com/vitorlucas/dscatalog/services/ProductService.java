package com.vitorlucas.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vitorlucas.dscatalog.dto.ProductDTO;
import com.vitorlucas.dscatalog.entities.Product;
import com.vitorlucas.dscatalog.repositories.ProductRepository;
import com.vitorlucas.dscatalog.services.exceptions.DatabaseException;
import com.vitorlucas.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repositoy;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repositoy.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));

		/*
		 * List<ProductDTO> listDTO = new ArrayList<>(); for (Product cat : list) {
		 * listDTO.add(new ProductDTO(cat)); } return listDTO;
		 */
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repositoy.findById(id);
		Product cat = obj.orElseThrow(() -> new ResourceNotFoundException("Product not Found"));
		return new ProductDTO(cat, cat.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO catDTO) {
		Product entity = new Product();
		//entity.setName(catDTO.getName());
		entity = repositoy.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO catDTO) {
		try {
			Product entity = repositoy.getOne(id);
			//entity.setName(catDTO.getName());
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
}
