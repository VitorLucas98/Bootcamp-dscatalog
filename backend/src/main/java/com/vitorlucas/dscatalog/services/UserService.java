package com.vitorlucas.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vitorlucas.dscatalog.dto.RoleDTO;
import com.vitorlucas.dscatalog.dto.UserDTO;
import com.vitorlucas.dscatalog.dto.UserInsertDTO;
import com.vitorlucas.dscatalog.entities.Role;
import com.vitorlucas.dscatalog.entities.User;
import com.vitorlucas.dscatalog.repositories.RoleRepository;
import com.vitorlucas.dscatalog.repositories.UserRepository;
import com.vitorlucas.dscatalog.services.exceptions.DatabaseException;
import com.vitorlucas.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repositoy;
	
	@Autowired
	private RoleRepository roleRepositoy;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(PageRequest pageRequest) {
		Page<User> list = repositoy.findAll(pageRequest);
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repositoy.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("User not Found"));
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repositoy.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try {
			User entity = repositoy.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repositoy.save(entity);
			return new UserDTO(entity);
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
	
	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		entity.getRoles().clear();
		
		for (RoleDTO roleDTO : dto.getRoles()) {
			Role role = roleRepositoy.getOne(roleDTO.getId());
			entity.getRoles().add(role);
		}
		
	}
}
