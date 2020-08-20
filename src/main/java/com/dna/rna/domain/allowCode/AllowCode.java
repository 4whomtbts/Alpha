package com.dna.rna.domain.allowCode;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "expired")
    private boolean expired;

    protected AllowCode() {}
    
    public AllowCode(String allowCode) {
        this.allowCode = allowCode;
        this.expired = false;
    }

}
