package com.dna.rna.domain.ClubBoard;

import com.dna.rna.domain.Board.Board;
import com.dna.rna.domain.Board.BoardRepositoryImpl;
import com.dna.rna.domain.Club.Club;
import com.dna.rna.domain.Club.ClubRepository;
import com.dna.rna.domain.Club.ClubRepositoryImpl;
import com.dna.rna.domain.School.School;
import com.dna.rna.domain.School.SchoolRepositoryImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClubBoardRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private SchoolRepositoryImpl schoolRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private BoardRepositoryImpl boardRepository;

    @Autowired
    private ClubBoardRepositoryImpl clubBoardRepository;

    public School makeSchool(String schoolName) {
        School school = School.of(schoolName);
        schoolRepository.save(school);
        return school;
    }

    public Club makeClub(School school, String clubName) {
        Club club = Club.of(school, clubName, LocalDate.now(),
                "1기", "dongguk univ", "hello", "loooong");
        clubRepository.save(club);
        return club;
    }

    @Test
    public void fetchAllBoardsOfClub() {
        School school = makeSchool("dgu");
        Club club = makeClub(school, "rna");
        String newBoardName = "foo_board";
        Board board = Board.of("foo_board");
        boardRepository.save(board);
        ClubBoard clubBoard = ClubBoard.of(club, board, null);
        clubBoardRepository.save(clubBoard);

        List<ClubBoard> aClubBoard = clubBoardRepository.fetchBoardsOfClub(club, board);
        assertThat(aClubBoard.size()).isEqualTo(1);
        assertThat(aClubBoard.get(0).getBoard().getBoardName()).isEqualTo(newBoardName);

    }
}
