package in.sp.main.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import in.sp.main.model.UserDetailss;

public class CustomUser implements  org.springframework.security.core.userdetails.UserDetails{

	private UserDetailss user;

	public CustomUser(UserDetailss user) {
		super();
		this.user=user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority=new SimpleGrantedAuthority(user.getRole());

         return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {
      return user.getPassword();	

	}

	@Override
	public String getUsername() {

		return user.getEmail();
	}
	@Override
	public boolean isAccountNonExpired() {

		return true;

	}
	
	@Override
	public boolean isAccountNonLocked() {

		return user.getAccountNonLocked();
}
	
	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}
	
	@Override
	public boolean isEnabled() {

		return user.getIsEnable();
}
	
}
