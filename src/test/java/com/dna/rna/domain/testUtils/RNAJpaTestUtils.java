package com.dna.rna.domain.testUtils;

import com.dna.rna.domain.board.Board;
import com.dna.rna.domain.board.BoardRepository;
import com.dna.rna.domain.club.Club;
import com.dna.rna.domain.club.ClubRepository;
import com.dna.rna.domain.clubBoard.ClubBoard;
import com.dna.rna.domain.clubBoard.ClubBoardRepository;
import com.dna.rna.domain.school.School;
import com.dna.rna.domain.school.SchoolRepository;
import com.dna.rna.domain.boardGroup.BoardGroup;
import com.dna.rna.domain.boardGroup.BoardGroupRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RNAJpaTestUtils {

    @PersistenceContext
    EntityManager em;

    @Autowired
    protected BoardGroupRepository boardGroupRepository;

    @Autowired
    protected SchoolRepository schoolRepository;

    @Autowired
    protected ClubRepository clubRepository;

    @Autowired
    protected BoardRepository boardRepository;

    @Autowired
    protected ClubBoardRepository clubBoardRepository;

    protected final static String defaultSchoolName = "dongguk";
    protected final static String defaultClubName = "rna";
    protected final static String defaultSeason = "1기";

    public School buildAndSaveSchool(String schoolName) {
        School school = School.of(schoolName);
        return schoolRepository.save(school);
    }

    public Club buildAndSaveClub(String clubName) {
        School school = buildAndSaveSchool(clubName);
        Club club = Club.of(school, defaultClubName, LocalDate.now(), defaultSeason, "dongguk", "short", "long", "URI");
        return clubRepository.save(club);
    }

    public Club buildAndSaveClub() {
        School school = buildAndSaveSchool(defaultSchoolName);
        Club club = Club.of(school, defaultClubName, LocalDate.now(), defaultSeason, "dongguk", "short", "long", "URI");
        return clubRepository.save(club);
    }

    public Board buildAndSaveBoard(String boardName) {
        Board board = Board.of(boardName);
        return boardRepository.save(board);
    }

    public ClubBoard buildAndSaveClubBoard(Club club, Board board, BoardGroup boardGroup) {
        ClubBoard clubBoard = ClubBoard.of(club, board, boardGroup, 0);
        return clubBoardRepository.save(clubBoard);
    }

    public BoardGroup buildAndSaveBoardGroup(Club club, String boardGroupName, int displayOrder, List<ClubBoard> clubBoards) {
        BoardGroup boardGroup = BoardGroup.of(club, boardGroupName, displayOrder,clubBoards);
        return boardGroupRepository.save(boardGroup);
    }
}
