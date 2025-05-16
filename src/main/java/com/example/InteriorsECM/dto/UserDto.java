package com.example.InteriorsECM.dto;

import com.example.InteriorsECM.model.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotEmpty(message = "Không được để trống")
    @Size(min=6, message = "Độ dài tên tối thiểu là 6 kí tự")
    @Pattern(regexp = "^\\S+$", message = "Tên không được chứa khoảng trống")
    String username;
    @NotEmpty(message = "Không được để trống")
    @Size(min=6, message = "Độ dài mật khẩu tối thiểu là 6 kí tự")
    String password;
    @NotEmpty(message = "Không được để trống")
    String email;
    Role role;
}
