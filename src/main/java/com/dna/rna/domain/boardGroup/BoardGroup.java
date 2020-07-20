package com.dna.rna.domain.boardGroup;

import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.Board.BoardItem;
import com.dna.rna.domain.Club.Club;
import com.dna.rna.domain.ClubBoard.ClubBoard;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import java.util.List;

import static com.dna.rna.domain.Club.Club.CLUB_ID;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity
@Table(name = "board_group")
public class BoardGroup extends BaseAuditorEntity implements BoardItem  {

    private static final Logger logger = LoggerFactory.getLogger(BoardGroup.class);

    public static final String BOARD_GROUP_ID = "board_group_id";
    public static final int MAX_BOARD_GROUP_NAME = 12;

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = BOARD_GROUP_ID)
    private long boardGroupId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CLUB_ID)
    private Club club;

    @Column(name = "board_group_name")
    private String boardGroupName;

    @Column(name = "display_order")
    private int displayOrder;

    @JsonManagedReference
    @OneToMany(mappedBy = "boardGroup",
               fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<ClubBoard> clubBoards;

    private BoardGroup(Club club, String boardGroupName, int displayOrder,
                       @Nullable List<ClubBoard> clubBoards) {
        this.club = club;
        this.boardGroupName = boardGroupName;
        this.displayOrder = displayOrder;
        this.clubBoards = clubBoards;
    }

    public static BoardGroup of(Club club, String boardGroupName, int displayOrder,
                                @Nullable List<ClubBoard> clubBoards) {
        requireNonNull(club, "club은 null이 될 수 없습니다.");
        requireNonNull(boardGroupName, "boardGroupName은 null이 될 수 없습니다.");
        return new BoardGroup(club, boardGroupName, displayOrder, clubBoards);
    }
}
