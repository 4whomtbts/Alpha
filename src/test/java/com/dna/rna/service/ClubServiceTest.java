package com.dna.rna.service;

import com.dna.rna.domain.Board.Board;
import com.dna.rna.domain.Club.Club;
import com.dna.rna.domain.Club.ClubRepository;
import com.dna.rna.domain.ClubBoard.ClubBoard;
import com.dna.rna.domain.ClubBoard.ClubBoardRepository;
import com.dna.rna.domain.School.School;
import com.dna.rna.domain.boardGroup.BoardGroup;
import com.dna.rna.domain.boardGroup.BoardGroupRepository;
import com.dna.rna.dto.BoardItemDto;
import com.dna.rna.dto.ClubDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@ExtendWith(MockitoExtension.class)
@JsonTest
public class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;
    @Mock
    private ClubBoardRepository clubBoardRepository;
    @Mock
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
        Club club = Club.of(school, "rna", LocalDate.now(), "1기", "학관", "short", "long", "uri");
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
        assertThat(item.get(0).getNestedItems().get(0).getBoard().getBoardName()).isEqualTo("board1");
        assertThat(item.get(0).getNestedItems().get(1).getBoard().getBoardName()).isEqualTo("board2");
        assertThat(item.get(0).getNestedItems().get(2).getBoard().getBoardName()).isEqualTo("board3");
        assertThat(item.get(1).getBoardItemType()).isEqualTo(BoardItemDto.BoardItemType.BOARD);
        assertThat(item.get(1).getNestedItems()).isNull();
        assertThat(item.get(1).getBoardItemName()).isEqualTo("dep");

    }
}
