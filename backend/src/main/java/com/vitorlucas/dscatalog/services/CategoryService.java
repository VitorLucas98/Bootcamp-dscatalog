package com.vitorlucas.dscatalog.services;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vitorlucas.dscatalog.entities.Category;
import com.vitorlucas.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repositoy;
	
	@Transactional(readOnly = true)
	public List<Category> findAll(){
		return repositoy.findAll();
	}
}
