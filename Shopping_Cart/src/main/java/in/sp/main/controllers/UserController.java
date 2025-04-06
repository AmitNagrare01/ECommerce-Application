package in.sp.main.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.sp.main.model.Cart;
import in.sp.main.model.Category;
import in.sp.main.model.UserDetailss;
import in.sp.main.repository.UserRepository;
import in.sp.main.services.CartService;
import in.sp.main.services.CategoryService;
import in.sp.main.services.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	
	@Autowired
    private UserService userService;
   
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CartService cartService;
	
	
	
	 @GetMapping("/")
	public String home() {
		return "user/home";
	}
	 @ModelAttribute
     public void getUserDetails(Principal p, Model m) {
   	  if(p!=null) {
   		 String email = p.getName();
   		UserDetailss userDetailss = userService.getUserByEmail(email);
   		 m.addAttribute("user",userDetailss);
   		Integer countCart =cartService.getCountCart(userDetailss.getId());
		m.addAttribute("countCart",countCart);
		
   	  }
   	 List <Category>allActiveCategory = categoryService.getallActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
}
	 @GetMapping("/addCart")
	 public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session ) {
		 
		Cart saveCart = cartService.saveCart(pid, uid); 
		 
		if(ObjectUtils.isEmpty(saveCart))
		{
			session.setAttribute("errorMsg", "Product Add To Cart Failed");
		}else {
			session.setAttribute("succMsg", "Product Add To Cart Successfully");

		}
		return "redirect:/product/"+pid;
	 }
	 
	 @GetMapping("/cart")
	 public String loadCartPage(Principal p,Model m) {
		
		 UserDetailss user = getLoggedInUserDetailss(p);
		 
		 List<Cart> carts = cartService.getCartsByUser(user.getId());
		
		 m.addAttribute("carts",carts);
		Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice();
		 m.addAttribute("totalOrderPrice",totalOrderPrice);
		 return "/user/cart";
	 }
	 
	 @GetMapping("/cartQuantityUpdate")
	 public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid) {
		 
	            cartService.updateQuantity(sy,cid);
		 
		return "redirect:/cart";
		 
		 
	 }
	 
	 private UserDetailss getLoggedInUserDetailss(Principal p) {
		 
		    String email = p.getName();
			UserDetailss userDetailss = userService.getUserByEmail(email);
			
		 return userDetailss ;
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}