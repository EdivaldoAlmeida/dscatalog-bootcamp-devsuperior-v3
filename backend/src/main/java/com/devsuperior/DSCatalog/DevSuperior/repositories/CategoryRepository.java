package com.devsuperior.DSCatalog.DevSuperior.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.DSCatalog.DevSuperior.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
