package in.sp.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sp.main.model.Cart;

public interface CartRepository extends JpaRepository<Cart,Integer>{

	public Cart findByProductIdAndUserId(Integer userId, Integer productId);
	
	public Integer countByUserId(Integer userId);
	
	public List<Cart> findByUserId(Integer userId);
	
}
