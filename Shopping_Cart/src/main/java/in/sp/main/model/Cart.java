package in.sp.main.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
     @ManyToOne
	private UserDetailss user;
     
	private Integer Quantity;
	@ManyToOne
	private Product product;
	
	@Transient
	private Double totalPrice;
	
	@Transient
	private Double totalOrderPrice;
	
	
}
