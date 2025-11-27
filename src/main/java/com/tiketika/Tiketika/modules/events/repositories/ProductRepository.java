package com.tiketika.Tiketika.modules.events.repositories;

import com.tiketika.Tiketika.modules.events.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByVendorId(Long vendorId);

}