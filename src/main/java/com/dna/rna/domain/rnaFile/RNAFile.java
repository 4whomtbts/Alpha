package com.dna.rna.domain.rnaFile;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.article.Article;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import java.time.LocalDate;

import static com.dna.rna.domain.article.Article.ARTICLE_ID;
import static java.util.Objects.requireNonNull;

// 앱 전반에서 파일을 관리하기 위해 사용하는 테이블
// 테이블명이 RNAFile 인 것은 Java의 File Class와의
// 이름충돌 및 혼란을 방지하기 위해서임.

@Getter
@Setter
@Entity
@Table(name = "rna_file")
public class RNAFile extends BaseAuditorEntity {

    private static final Logger logger = LoggerFactory.getLogger("rna_file");

    public static final String RNA_FILE_ID = "rna_file_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = RNA_FILE_ID)
    private long rnaFileId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ARTICLE_ID)
    private Article article;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "uri", nullable = false)
    private String uri;

    @Nullable
    @Column(name = "expired_at")
    private LocalDate expiredAt;

    private static RNAFile of(Article article, String fileName, String uri, LocalDate expiredAt) {
        return new RNAFile(article, fileName, uri, expiredAt);
    }

    private static RNAFile of(Article article, String fileName, String uri) {
        return new RNAFile(article, fileName, uri, null);
    }

    protected RNAFile() {

    }

    // TODO expiredAt 을 null로 해도 될까?
    private RNAFile(Article article, String fileName, String uri, @Nullable LocalDate expiredAt) {
        requireNonNull(fileName, "RNAFile 생성자에서 fileName은 null이 될 수 없습니다.");
        requireNonNull(uri, "RNAFile 생성자에서 uri는 null이 될 수 없습니다.");
        //requireNonNull(expiredAt, "RNAFile 생성자에서 expiredAt은 null이 될 수 없습니다.");
        this.fileName = fileName;
        this.uri = uri;
        this.expiredAt = expiredAt;
    }

    private RNAFile(Article article, String fileName, String uri) {
        this(article, fileName, uri, null);
    }

}
