package in.sp.main.services;

import java.util.List;

import in.sp.main.model.Cart;

public interface CartService {

	public Cart saveCart(Integer ProductId, Integer userId);
	
	public List<Cart> getCartsByUser(Integer userId);
	
	public Integer getCountCart(Integer userId);

	public void updateQuantity(String sy, Integer cid);
}
