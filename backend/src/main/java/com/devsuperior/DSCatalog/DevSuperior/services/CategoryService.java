package com.devsuperior.DSCatalog.DevSuperior.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.DSCatalog.DevSuperior.dto.CategoryDTO;
import com.devsuperior.DSCatalog.DevSuperior.entities.Category;
import com.devsuperior.DSCatalog.DevSuperior.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
		/*Todo este código abaixo foi substituído pela linha acima (expressão lãmbida)
		 * cuja função é transformar uma lista de objetos do tipo Category para CatetoryDTO
		 * com o intuito de atender as especificações da arquiterura, na qual apenas os
		 * objetos do tipo CategoryDTO devem ser movimentados entre as camadas
		List<CategoryDTO> listDTO = new ArrayList<>();
		for (Category cat : list) {
			listDTO.add(new CategoryDTO(cat));
		}
		return listDTO;
		*/
	}
}
