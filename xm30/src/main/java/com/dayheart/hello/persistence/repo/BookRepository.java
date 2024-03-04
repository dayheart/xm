package com.dayheart.hello.persistence.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dayheart.hello.persistence.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
	
	List<Book> findByTitle(String title);

}
