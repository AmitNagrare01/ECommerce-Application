package in.sp.main.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import in.sp.main.model.UserDetailss;
import in.sp.main.repository.UserRepository;
import in.sp.main.services.UserService;
import in.sp.main.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

	String email =	request.getParameter("username");
		
	UserDetailss userDetailss =userRepository.findByEmail(email);
		if(userDetailss.getIsEnable()) {
			if(userDetailss.getAccountNonLocked()) {
				if(userDetailss.getFailedAttemped()<=AppConstant.ATTEMPT_TIME) {
					userService.increaseFailedAttmeped(userDetailss);
				}else {
					userService.userAccoountLock(userDetailss);
					exception= new LockedException("Your Account is Locked !! Failed Attemp 3");
				}
				
			}else {
				if(userService.unlockAccountTimeExpired(userDetailss)) {
		exception= new LockedException("Your Account is unLocked !! try to login");
                  }else {
      				exception =new LockedException("your acount is Locked !! try after sometime");

                  }
				exception =new LockedException("your acount is Locked");

			}
		}else {
			exception =new LockedException("your acount is inactive");
		}
		super.onAuthenticationFailure(request, response, exception);
	}

}
