package com.dna.rna.domain.board;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.articleFile.RNAFile;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Entity for General board of RNA service.
 *
 * board.java
 * created 2020.3.25
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
@Entity
@Table(name="board")
public class Board extends BaseAuditorEntity {

    private static final Logger logger= LoggerFactory.getLogger(Board.class);

    public static final String BOARD_ID ="board_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = BOARD_ID)
    private long id;

    @Column(name = "board_name", nullable = false)
    private String boardName;

    @OneToMany(mappedBy = BOARD_ID, orphanRemoval = true)
    @Column(name = "attached_files", nullable = false)
    private List<RNAFile> attachedFiles;

    protected Board() {}

    private Board(String boardName) {
        this.boardName = boardName;
    }

    public static Board of(String boardName) {
        requireNonNull(boardName, "boardName은 null이 될 수 없습니다.");
        if (boardName.equals("")) {
            IllegalArgumentException exception = new IllegalArgumentException("게시판 이름은 공백 문자가 될 수 없습니다.");
            logger.error("예외 : boardName이 공백문자가 될 수 없음", exception);
            throw exception;
        }
        return new Board(boardName);
    }

}
