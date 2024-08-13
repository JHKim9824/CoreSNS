package com.jhkim9824.coresns.dto;

import com.jhkim9824.coresns.entity.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String name;
    private String phoneNumber;
    private Gender gender;
    private LocalDateTime birthdate;
    private String introduce;
    private String profileImage;
    private LocalDateTime createdDate;
}
