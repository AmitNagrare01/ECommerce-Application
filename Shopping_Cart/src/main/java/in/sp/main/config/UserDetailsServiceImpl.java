package in.sp.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.sp.main.model.UserDetailss;
import in.sp.main.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	     UserDetailss user=userRepository.findByEmail(username);
	     if(user==null) {
	    	 throw new UsernameNotFoundException("User not found");
	     }
		return new CustomUser(user);
	}
	
	
}
