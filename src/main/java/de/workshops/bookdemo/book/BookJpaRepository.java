package de.workshops.bookdemo.book;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJpaRepository extends CrudRepository<Book, Long> {

	
	public Book findByIsbn(String isbn);

	@Query(nativeQuery = true, value = "SELECT * FROM books WHERE isbn = :theIsbn")
	public Book findByIsbnNative(@Param(value = "theIsbn") String isbn);
	
}
