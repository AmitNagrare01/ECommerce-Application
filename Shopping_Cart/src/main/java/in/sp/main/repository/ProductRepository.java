package in.sp.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sp.main.model.Product;

public interface ProductRepository  extends JpaRepository<Product,Integer>{

	List<Product> findByisActiveTrue();
	List<Product> findByCategory(String category);
	
}
