/**
 * 
 */
package com.dayheart.hello.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dayheart.hello.jpa.Order;

/**
 * @author dayheart
 *
 */
public interface OrderDAO extends JpaRepository<Order, Long> {
	
	
	

}
