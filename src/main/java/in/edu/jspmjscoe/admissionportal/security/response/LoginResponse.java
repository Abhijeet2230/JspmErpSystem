package in.edu.jspmjscoe.admissionportal.security.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoginResponse {
    private String jwtToken;

    private String username;
    private List<String> roles;
    private boolean firstLogin;

    public LoginResponse(String username, List<String> roles, String jwtToken, boolean firstLogin) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.firstLogin = firstLogin;
    }

}
