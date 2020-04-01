package com.dna.rna.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public abstract class BaseAuditorEntity {

    // TODO custom serializer and deserializer 적용
    // https://tramyu.github.io/java/spring/jpa-auditing/
    @Column(nullable =  false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    // TODO custom serializer and deserializer 적용
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}
