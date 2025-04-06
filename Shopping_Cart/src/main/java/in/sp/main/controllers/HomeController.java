package in.sp.main.controllers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.sp.main.model.Category;
import in.sp.main.model.Product;
import in.sp.main.model.UserDetailss;
import in.sp.main.repository.CategoryRepository;
import in.sp.main.services.CartService;
import in.sp.main.services.CategoryService;
import in.sp.main.services.ProductService;
import in.sp.main.services.UserService;
import in.sp.main.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
      @Autowired
	private CategoryService categoryService;

      @Autowired
      private ProductService productService;
	
      @Autowired
      private UserService userService;
            
      @Autowired
      private CommonUtil commonUtil;
      
      @Autowired
      private BCryptPasswordEncoder passwordEncoder;
      
      @Autowired
      private CartService cartService;
      
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
	@GetMapping("/")
	public String index() {
		
		return "index";
		
	}
	
	@GetMapping("/Register")
	public String Registration() {
		
		return "Register";
		
	}
	@GetMapping("/signin")
	public String login() {
		
		return "login";
		
	}   
	@GetMapping("/products")
	public String products(Model m, @RequestParam(value ="category",defaultValue="") String category) {
	List <Category> categories =	categoryService.getallActiveCategory();
    List <Product>  products =  productService.getAllActiveProducts(category);
             
    m.addAttribute("categories", categories);
    m.addAttribute("products", products);
		m.addAttribute("paramValue",category);
		return "product";
		
}
	@GetMapping("/product/{id}")
	public String product(@PathVariable int id,Model m) {
		Product productById=productService.getProductById(id);
		m.addAttribute("product",productById);
		return "view_product";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDetailss user,@RequestParam("img")MultipartFile file,HttpSession session) throws IOException {
		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
         user.setProfileImage(imageName);
		UserDetailss saveUser = userService.saveUser(user);
		
		if(!ObjectUtils.isEmpty(saveUser)) {
			if(!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/images").getFile();
				
				Path path =	Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + file.getOriginalFilename());	
				System.out.println(path);
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("succMsg", "Saved Successfully");

		
		}else {
			session.setAttribute("errorMsg", "Soemthing Error");
		}
		
		return "redirect:/Register";
	}
	
	
	//forgot Password
	@GetMapping("/forgot-password")
	public String showForgotPassword() {
		
		return "forgot_password";
		
	}
	//Reset Password
			@PostMapping("/forgot-password")
			public String processForgotPassword(@RequestParam String email, HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException 
			{
				
			UserDetailss userByEmail =	userService.getUserByEmail(email);
			
			if(ObjectUtils.isEmpty(userByEmail)) {
				session.setAttribute("errorMsg","Invalid Email");
				
			}else {
				 String resetToken =UUID.randomUUID().toString();
		    userService.updateUserResetToken(email,resetToken);
				
		    String url= CommonUtil.genrateUrl(request)+"/reset-password?token="+resetToken;

		     Boolean sendMail  =  commonUtil.sendMail(url, email);        
		       		    
		if(sendMail) {
			session.setAttribute("succMsg", "Please Check Your Mail");
		}else {
			session.setAttribute("errorMsg", "Something wrong On server");
		}
			}
			return "redirect:/forgot-password";
			
			}
			
	    //Reset Password
		@GetMapping("/reset-password")
		public String showResetPassword(@RequestParam String token,Model m) {
			
		UserDetailss userByToken =	userService.getUserByToken(token);
			if(userByToken==null) {

				m.addAttribute("msg","Your Link Is Invalid or Expired");
				return "message";
			}
			m.addAttribute("token", token);
			return "reset_password";
			
		}
		@PostMapping("/reset-password")
		public String resetPassword(@RequestParam String token,@RequestParam String password,HttpSession session ,Model m) {
			
		UserDetailss userByToken =	userService.getUserByToken(token);
		if(userByToken==null) {

			m.addAttribute("errorMsg","Your Link Is Invalid or Expired");
			return "message";
		}else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userService.updateUser(userByToken);
		session.setAttribute("succMsg","Password Changed Successfully");
		m.addAttribute("msg","Password Changed Successfully");
			return "message";
		}
		}
}