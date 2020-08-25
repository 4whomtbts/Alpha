package com.dna.rna.dto;


import com.dna.rna.domain.user.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignupForm {

    private Long id;
    private String loginId;
    private String userName;
    private String password;
    private String organization;
    private String allowCode;

    public User toUserEntity() {
        return User.of(this.loginId, this.userName, this.password,this.organization);
    }

}