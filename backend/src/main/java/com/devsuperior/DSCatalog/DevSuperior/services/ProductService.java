package com.devsuperior.DSCatalog.DevSuperior.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.DSCatalog.DevSuperior.dto.CategoryDTO;
import com.devsuperior.DSCatalog.DevSuperior.dto.ProductDTO;
import com.devsuperior.DSCatalog.DevSuperior.entities.Category;
import com.devsuperior.DSCatalog.DevSuperior.entities.Product;
import com.devsuperior.DSCatalog.DevSuperior.repositories.CategoryRepository;
import com.devsuperior.DSCatalog.DevSuperior.repositories.ProductRepository;
import com.devsuperior.DSCatalog.DevSuperior.services.exceptions.DataBaseException;
import com.devsuperior.DSCatalog.DevSuperior.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repository.findAll(pageRequest);

		return list.map(x -> new ProductDTO(x));

		/*
		 * Todo este código abaixo foi substituído pela linha acima (expressão lãmbida)
		 * cuja função é transformar uma lista de objetos do tipo Product para
		 * CatetoryDTO com o intuito de atender as especificações da arquiterura, na
		 * qual apenas os objetos do tipo ProductDTO devem ser movimentados entre as
		 * camadas List<ProductDTO> listDTO = new ArrayList<>(); for (Product cat :
		 * list) { listDTO.add(new ProductDTO(cat)); } return listDTO;
		 */
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));

		return new ProductDTO(entity, entity.getCategories());

	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);

	}


	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {

		try {
			Product entity = repository.getOne(id); 
			copyDtoToEntity(dto, entity); 
			entity = repository.save(entity); 
			return new ProductDTO(entity);
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
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {

		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());;
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for (CategoryDTO catDto: dto.getCategories()) {
			Category categor = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(categor);
			
		}
	
	}
}
