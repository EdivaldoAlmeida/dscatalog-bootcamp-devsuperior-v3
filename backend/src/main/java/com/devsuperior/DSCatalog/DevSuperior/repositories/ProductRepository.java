package com.devsuperior.DSCatalog.DevSuperior.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.DSCatalog.DevSuperior.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
