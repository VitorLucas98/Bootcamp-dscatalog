package com.vitorlucas.dscatalog.tests.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.vitorlucas.dscatalog.entities.Product;
import com.vitorlucas.dscatalog.repositories.ProductRepository;
import com.vitorlucas.dscatalog.tests.factory.ProductFactory;

@DataJpaTest 
//Anotação para um teste JPA que se concentra apenas nos componentes JPA.
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistId;
	private long countTotalProducts;
	private long countPcGamerProducts;
	private PageRequest pageRequest;
	//private long countProductCat1andCat3;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistId = 1000L;
		countTotalProducts = 25L;
		countPcGamerProducts = 21L;
		pageRequest = PageRequest.of(0, 10);
		//countProductCat1andCat3 = 23L;
	}
	
	/*@Test
	public void findShouldReturnProductsWhenHaveCategory() {
	
		Category cat1 = new Category(3L, "Computers");
		Category cat2 = new Category(1L, "Books");
		
		
		List<Category> categories = new ArrayList<>();
		
		categories.add(cat1);
		categories.add(cat2);
		
		Page<Product> result = repository.find(categories, "", pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		
		Assertions.assertEquals(countProductCat1andCat3, result.getTotalElements());
	}*/

	@Test
	public void findShouldReturnAllProductsWhenNameDoesNotExists() {
		String name = "Camera";
		
		Page<Product> result = repository.find(null, name, pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findShouldReturnAllProductsWhenNameIsEmpty() {
		String name = "";
		
		Page<Product> result = repository.find(null, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findShouldReturnProductsWhenNameExistsIgnoringCase() {
		String name = "pc gAMeR";
		
		Page<Product> result = repository.find(null, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
	}
	
	@Test
	public void findShouldReturnProductsWhenNameExists() {
		String name = "PC Gamer";
		
		Page<Product> result = repository.find(null, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product prod = ProductFactory.createProduct();
		prod.setId(null);

		prod = repository.save(prod);
		Optional<Product> result = repository.findById(prod.getId());
		
		Assertions.assertNotNull(prod.getId());
		Assertions.assertEquals(countTotalProducts + 1L, prod.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), prod);
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowsEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
			repository.deleteById(nonExistId);
		});
	}

}
