package com.dna.rna.domain.allowCode;


import com.dna.rna.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.dna.rna.domain.user.User.USER_ID;

@Getter
@Setter
@Entity
@Table(name= "allow_code")
public class AllowCode {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "allow_code_id")
    private long allowCodeId;

    @Column(name = "allow_code")
    private String allowCode;

    @Column(name = "allow_code_type")
    private AllowCodeType allowCodeType;

    @Column(name = "expired")
    private boolean expired;

    // 인증코드의 대상 유저
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = USER_ID, nullable = false)
    private User user;


    protected AllowCode() {}
    
    public AllowCode(User user, String allowCode, AllowCodeType allowCodeType) {
        this.user = user;
        this.allowCode = allowCode;
        this.allowCodeType = allowCodeType;
        this.expired = false;
    }


}
