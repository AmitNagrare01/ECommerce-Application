package in.sp.main.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import in.sp.main.model.Product;
import in.sp.main.repository.ProductRepository;


@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product saveProduct(Product product) {
		
		return productRepository.save(product);
	}

	@Override
	public List<Product> getallProduct() {
		
		
		return productRepository.findAll();
	}


	@Override
	public Boolean deleteProduct(Integer id) {
		 Product product = productRepository.findById(id).orElse(null);

	        if (!ObjectUtils.isEmpty(product)) {
	            productRepository.delete(product);
	            return true;
	        }
	        return false;
	    
	}

	@Override
	public Product getProductById(Integer id) {
Product product = productRepository.findById(id).orElse(null);
		
		return product;
	}

	@Override
	public Product updateProduct(Product product,MultipartFile image)  {
		
		Product product1 =	getProductById(product.getId());
		
		String imageName= image.isEmpty() ? product1.getImage() : image.getOriginalFilename();
		
		
		product1.setTitle(product.getTitle());
		product1.setDescription(product.getDescription());
		product1.setCategory(product.getCategory());
		product1.setPrice(product.getPrice());
		product1.setStock(product.getStock());
		product1.setImage(imageName);
		product1.setIsActive(product.getIsActive());
		product1.setDiscount(product.getDiscount());
		
	      Double discount    = 	product.getPrice()*(product.getDiscount()/100.0);
	    Double discountPrice =	product.getPrice()-discount;
	    
	    product1.setDiscountPrice(discountPrice);
	    
	Product updateProduct =	productRepository.save(product1);
		if (!ObjectUtils.isEmpty(updateProduct)) {

		if (!image.isEmpty()) {

			try {
				File saveFile = new ClassPathResource("static/images").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
						+ image.getOriginalFilename());
				Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return product;
	}
	return null;
	
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		List<Product>products=null;
		if(ObjectUtils.isEmpty(category)) {
			 products = productRepository.findByisActiveTrue();
		}else {
			products=productRepository.findByCategory(category);
		}
		 
		return products;
	}
}

