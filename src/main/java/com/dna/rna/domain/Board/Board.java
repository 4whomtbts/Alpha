package com.dna.rna.domain.Board;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.Club.Club;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

/**
 * Entity for General Board of RNA service.
 *
 * Board.java
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

    @Column(nullable = false, name = "board_name")
    private String boardName;

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
