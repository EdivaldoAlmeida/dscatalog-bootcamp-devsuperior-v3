package com.devsuperior.DSCatalog.DevSuperior.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.DSCatalog.DevSuperior.dto.CategoryDTO;
import com.devsuperior.DSCatalog.DevSuperior.entities.Category;
import com.devsuperior.DSCatalog.DevSuperior.repositories.CategoryRepository;
import com.devsuperior.DSCatalog.DevSuperior.services.exceptions.DataBaseException;
import com.devsuperior.DSCatalog.DevSuperior.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();

		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

		/*
		 * Todo este código abaixo foi substituído pela linha acima (expressão lãmbida)
		 * cuja função é transformar uma lista de objetos do tipo Category para
		 * CatetoryDTO com o intuito de atender as especificações da arquiterura, na
		 * qual apenas os objetos do tipo CategoryDTO devem ser movimentados entre as
		 * camadas List<CategoryDTO> listDTO = new ArrayList<>(); for (Category cat :
		 * list) { listDTO.add(new CategoryDTO(cat)); } return listDTO;
		 */
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));

		return new CategoryDTO(entity);

	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);

	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {

		try {
			Category entity = repository.getOne(id); // instancia uma entity sem persistí-la no banco
			entity.setName(dto.getName()); // seta o nome vindo do dto na nova entity provisória
			entity = repository.save(entity); // salva a entity com o nome atualizado no bd
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
	}

	public void delete(Long id) {
						
			try {
				repository.deleteById(id);
			}
			catch(EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException("Id not found" + id);
			}
			catch(DataIntegrityViolationException e) {
				throw new DataBaseException("Integrit Violation");
			}
	}
}
