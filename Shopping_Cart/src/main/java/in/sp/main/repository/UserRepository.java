package in.sp.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sp.main.model.UserDetailss;

public interface UserRepository extends JpaRepository<UserDetailss,Integer >{ 

	public UserDetailss findByEmail(String email);

	public List<UserDetailss>findByRole(String role);
	
	public UserDetailss findByResetToken(String token);
}
