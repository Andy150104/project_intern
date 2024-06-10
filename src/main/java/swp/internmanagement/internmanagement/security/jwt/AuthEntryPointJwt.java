package swp.internmanagement.internmanagement.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint{
    private static final Logger logger=LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
                String errorMessage = "Unauthorized";
                int errorCode = HttpServletResponse.SC_UNAUTHORIZED;
        
                // Customize the response based on the exception type or message
                if (authException.getMessage().contains("expired")) {
                    errorMessage = "Token has expired";
                    errorCode = HttpServletResponse.SC_UNAUTHORIZED;
                } else if (authException.getMessage().contains("invalid")) {
                    errorMessage = "Invalid token";
                    errorCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (authException.getMessage().contains("missing")) {
                    errorMessage = "Token is missing";
                    errorCode = HttpServletResponse.SC_BAD_REQUEST;
                } else {
                    logger.error("Unauthorized: {}", authException.getMessage());
                }
        
                response.sendError(errorCode, "Error: " + errorMessage);
    }
    // logger.error("Unauthorized: {}", authException.getMessage());
    // response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Error: Unauthorized");


}
