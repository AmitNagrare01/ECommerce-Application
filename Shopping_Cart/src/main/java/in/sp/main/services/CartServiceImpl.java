package in.sp.main.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import in.sp.main.model.Cart;
import in.sp.main.model.Product;
import in.sp.main.model.UserDetailss;
import in.sp.main.repository.CartRepository;
import in.sp.main.repository.ProductRepository;
import in.sp.main.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService{

	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private ProductRepository productRepository;
	@Override
	public Cart saveCart(Integer ProductId, Integer userId) {

		UserDetailss userDetails =userRepository.findById(userId).get();
           	Product product =	productRepository.findById(ProductId).get();
           	
           	Cart cartStatus =cartRepository.findByProductIdAndUserId(ProductId, userId);
           	
           	Cart cart=null;
           	
           	if(ObjectUtils.isEmpty(cartStatus)) {
           		
           		cart= new Cart();
           		cart.setProduct(product);
           		cart.setUser(userDetails);
           		cart.setQuantity(1);
           		cart.setTotalPrice(1*product.getDiscountPrice());
            	}else {
            		cart=cartStatus;
            		cart.setQuantity(cart.getQuantity()+1);
            		cart.setTotalPrice(cart.getQuantity()* cart.getProduct().getDiscountPrice());
            	}
           	Cart saveCart = cartRepository.save(cart);
           	
		    return saveCart;
	}

	@Override
	public List<Cart> getCartsByUser(Integer userId) {
      List <Cart> carts = cartRepository.findByUserId(userId);
      
      Double totalOrderPrice=0.0;
      
      List<Cart> updateCarts=new ArrayList<>();
      
      for(Cart c:carts) {
    	  
    Double totalPrice =(c.getProduct().getDiscountPrice()*c.getQuantity());
    	  c.setTotalPrice(totalPrice);
    	totalOrderPrice =  totalOrderPrice+totalPrice;
    	c.setTotalOrderPrice(totalOrderPrice);
    	updateCarts.add(c);
      }
       return updateCarts;
	}

	@Override
	public Integer getCountCart(Integer userId) {
   Integer countByuserId = cartRepository.countByUserId(userId);
   
		return countByuserId;
	}

	@Override
	public void updateQuantity(String sy, Integer cid) {
		
                     Cart cart  =  cartRepository.findById(cid).get();
		             int updateQuantity;
		             
                     if(sy.equalsIgnoreCase("de")) {
                         updateQuantity =cart.getQuantity() -1; 
                    	 
                     if(updateQuantity<=0) {
                    	 cartRepository.deleteById(cid);
                          
                     }else {
                    	 updateQuantity = cart.getQuantity()+1;
                     }
                     cart.setQuantity(updateQuantity);
                     cartRepository.save(cart);
                     
                     
                     }
		        
	}

}
