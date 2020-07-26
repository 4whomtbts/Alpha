package com.dna.rna.domain.clubBoard;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.board.Board;
import com.dna.rna.domain.board.BoardItem;
import com.dna.rna.domain.boardGroup.BoardGroup;
import com.dna.rna.domain.club.Club;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.*;

import static com.dna.rna.domain.board.Board.BOARD_ID;
import static com.dna.rna.domain.boardGroup.BoardGroup.BOARD_GROUP_ID;
import static com.dna.rna.domain.club.Club.CLUB_ID;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity
@Table(name = "club_board")
public class ClubBoard extends BaseAuditorEntity implements BoardItem {

    private static final Logger logger= LoggerFactory.getLogger(ClubBoard.class);

    public static final String CLUB_BOARD_ID = "club_board_id";

    @Id @GeneratedValue(strategy=  GenerationType.SEQUENCE)
    @Column(name = CLUB_BOARD_ID)
    private long clubBoardId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = BOARD_ID, nullable = false)
    private Board board;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CLUB_ID, nullable = false)
    private Club club;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BOARD_GROUP_ID)
    private BoardGroup boardGroup;

    @Column(name = "display_order")
    private int displayOrder;

    public static ClubBoard of(Club club, Board board, @Nullable BoardGroup boardGroup, int displayOrder) {
        requireNonNull(board, "board는 null이 될 수 없습니다.");
        requireNonNull(club, "club은 null이 될 수 없습니다.");
        return new ClubBoard(club, board, boardGroup, displayOrder);
    }

    protected ClubBoard() {}

    private ClubBoard(Club club, Board board, @Nullable BoardGroup boardGroup, int displayOrder) {
        this.board = board;
        this.club = club;
        this.boardGroup = boardGroup;
        this.displayOrder = displayOrder;
    }

}
