package in.edu.jspmjscoe.admissionportal.dtos.security;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}