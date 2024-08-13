package com.jhkim9824.coresns.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import com.jhkim9824.coresns.entity.Gender;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 50, message = "닉네임은 50자를 초과할 수 없습니다.")
    private String nickname;

    @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
    private String name;

    @Size(max = 11, message = "휴대폰 번호는 11자를 초과할 수 없습니다.")
    private String phoneNumber;

    private Gender gender;

    @Past(message = "생일은 과거 날짜여야 합니다.")
    private LocalDateTime birthdate;

    @Size(max = 500, message = "자기소개는 500자를 초과할 수 없습니다.")
    private String introduce;
}
