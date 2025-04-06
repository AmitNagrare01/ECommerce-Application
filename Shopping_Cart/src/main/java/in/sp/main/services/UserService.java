package in.sp.main.services;

import java.util.List;

import org.springframework.stereotype.Service;

import in.sp.main.model.UserDetailss;


public interface UserService {
	
	public UserDetailss saveUser(UserDetailss user);

	public UserDetailss getUserByEmail(String email);
	
	public List<UserDetailss> getUsers(String role);

	public Boolean updateAccountStatus(Integer id, Boolean status);
	
	public void increaseFailedAttmeped(UserDetailss user);
	
	public void userAccoountLock(UserDetailss user);
	
	public boolean unlockAccountTimeExpired(UserDetailss user);
	
	public void resetAttempt(int userId);

	public void updateUserResetToken(String email, String resetToken);
	
	public UserDetailss getUserByToken(String token);
	
	public UserDetailss updateUser(UserDetailss user);
	
}
