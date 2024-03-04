/**
 * 
 */
package com.dayheart.hello.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dayheart.hello.jpa.Product;
import com.dayheart.hello.jpa.ProductId;

/**
 * @author dayheart
 *
 */
public interface ProductDAO extends JpaRepository<Product, ProductId> {
	
	@Query("SELECT p FROM products p where p.mfrId = :mfrId and p.productId = :productId")
	Product retrieveByProduct(@Param("mfrId") String mfrId, @Param("productId") String productId);
	

}
