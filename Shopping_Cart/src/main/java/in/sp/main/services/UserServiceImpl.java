package in.sp.main.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.sp.main.model.UserDetailss;
import in.sp.main.repository.UserRepository;
import in.sp.main.util.AppConstant;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	 private UserRepository userRepository;
	
	@Override
	public UserDetailss saveUser(UserDetailss user) {
		user.setRole("ROLE_USER");
		user.setIsEnable(true);
		user.setAccountNonLocked(true);
		user.setFailedAttemped(0);
		
		String encodePassword=passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
      UserDetailss saveUser = userRepository.save(user);
		return saveUser;
	}

	@Override
	public UserDetailss getUserByEmail(String email) {
		
		return userRepository.findByEmail(email);
		
	}

	@Override
	public List<UserDetailss> getUsers(String role) {

		return userRepository.findByRole(role);
		 
	}

	@Override
	public Boolean updateAccountStatus(Integer id, Boolean
			status) {

		Optional <UserDetailss> findByuser = userRepository.findById(id);
		
		if(findByuser.isPresent()) {
			UserDetailss userDetailss =findByuser.get();
			userDetailss.setIsEnable(status);
			userRepository.save(userDetailss);
			return true;
		}
		return false;
	}

	@Override
	public void increaseFailedAttmeped(UserDetailss user) {

		int attempt=user.getFailedAttemped()+1;
		user.setFailedAttemped(attempt);
		userRepository.save(user);
		
	}

	@Override
	public void userAccoountLock(UserDetailss user) {

		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		userRepository.save(user);
	}

	@Override
	public boolean unlockAccountTimeExpired(UserDetailss user) {

	            long lockTime =user.getLockTime().getTime();
	            long unlockTime=lockTime + AppConstant.UNLOCK_DURATION_TIME;
	            
	            long CurrentTime=System.currentTimeMillis();
	            if(unlockTime<CurrentTime) {
	            	
	            	user.setAccountNonLocked(true);
	            	user.setFailedAttemped(0);
	            	user.setLockTime(null);
	            	userRepository.save(user);
	            	return true;
	            	
	            }
				return false;
	}

	@Override
	public void resetAttempt(int userId) {

		
	}

	@Override
	public void updateUserResetToken(String email, String resetToken) {

		
		UserDetailss findByEmail =userRepository.findByEmail(email);
		findByEmail.setResetToken(resetToken);
		userRepository.save(findByEmail);
	}

	@Override
	public UserDetailss getUserByToken(String token) {

		return userRepository.findByResetToken(token);
	}

	@Override
	public UserDetailss updateUser(UserDetailss user) {

		return userRepository.save(user);

	}

	
	
}
