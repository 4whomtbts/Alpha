package com.dna.rna.service;

import com.dna.rna.domain.board.Board;
import com.dna.rna.domain.club.Club;
import com.dna.rna.domain.club.ClubRepository;
import com.dna.rna.domain.clubBoard.ClubBoard;
import com.dna.rna.domain.clubBoard.ClubBoardRepository;
import com.dna.rna.domain.school.School;
import com.dna.rna.domain.boardGroup.BoardGroup;
import com.dna.rna.domain.boardGroup.BoardGroupRepository;
import com.dna.rna.domain.testUtils.RNAJpaTestUtils;
import com.dna.rna.domain.user.User;
import com.dna.rna.dto.BoardItemDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClubServiceTest extends RNAJpaTestUtils {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubBoardRepository clubBoardRepository;
    @Autowired
    private BoardGroupRepository boardGroupRepository;

    @InjectMocks
    private ClubService clubService;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, ObjectMapper::new);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getBoardList() throws JsonProcessingException {
        School school = School.of("dgu");
        User user = buildAndSaveUser("4whomtbts", "jun", "world");
        Club club = Club.of(school, "rna", user, LocalDate.now(), "1기", "학관", "short", "long", "uri");
        String boardGroupName = "boardGroup";
        List<BoardGroup> boardGroups = new ArrayList<>();
        List<ClubBoard> clubBoards = new ArrayList<>();
        BoardGroup boardGroup = BoardGroup.of(club, boardGroupName, 0, null);
        ClubBoard clubBoard = ClubBoard.of(club, Board.of("board1"), boardGroup, 0);
        ClubBoard clubBoard1 = ClubBoard.of(club, Board.of("board2"), boardGroup, 1);
        ClubBoard clubBoard2 = ClubBoard.of(club, Board.of("board3"), boardGroup, 2);
        ClubBoard depBoard  = ClubBoard.of(club, Board.of("dep"), null, 1);
        clubBoards.add(depBoard);
        List<ClubBoard> boardList = new ArrayList<>();
        boardList.add(clubBoard);
        boardList.add(clubBoard1);
        boardList.add(clubBoard2);
        boardGroup.setClubBoards(boardList);
        boardGroups.add(boardGroup);
        BoardItemDto.BoardList result = clubService.getBoardList(boardGroups, clubBoards);
        List<BoardItemDto.Item> item = result.getBoardItems();
        assertThat(item.get(0).getBoardItemType()).isEqualTo(BoardItemDto.BoardItemType.GROUP);
        assertThat(item.get(0).getNestedItems().size()).isEqualTo(3);
        assertThat(item.get(0).getNestedItems().get(0).getBoardName()).isEqualTo("board1");
        assertThat(item.get(0).getNestedItems().get(1).getBoardName()).isEqualTo("board2");
        assertThat(item.get(0).getNestedItems().get(2).getBoardName()).isEqualTo("board3");
        assertThat(item.get(1).getBoardItemType()).isEqualTo(BoardItemDto.BoardItemType.BOARD);
        assertThat(item.get(1).getNestedItems()).isNull();
        assertThat(item.get(1).getBoardItemName()).isEqualTo("dep");

    }
}
