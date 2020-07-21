package com.dna.rna.domain.boardGroup;

import com.dna.rna.domain.club.Club;
import com.dna.rna.domain.clubBoard.ClubBoard;
import com.dna.rna.domain.school.School;
import com.dna.rna.domain.testUtils.RNAJpaTestUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardGroupRepositoryTest extends RNAJpaTestUtils {



    @Test
    public void save() {
        School school = School.of("dgu");
        schoolRepository.save(school);
        school = schoolRepository.findSchoolBySchoolName("dgu");
        Club club = Club.of(school, "rna", LocalDate.now(), "1기", "학관", "short", "long", "uri");
        clubRepository.save(club);
        BoardGroup boardGroup = BoardGroup.of(club, "boardGroup", 0, Collections.emptyList());
        boardGroupRepository.save(boardGroup);
        List<BoardGroup> savedBoardGroup = boardGroupRepository.findAll();
        assertThat(savedBoardGroup.get(0).getBoardGroupName()).isEqualTo("boardGroup");
    }

    public BoardGroup buildAndSaveMockBoardGroup(String boardName) {
        Club club = buildAndSaveClub();
        BoardGroup boardGroup = buildAndSaveBoardGroup(club, "foo_group", 0, new ArrayList<>());
        assertThat(boardGroup.getBoardGroupName()).isEqualTo("foo_group");
        assertThat(boardGroup.getClubBoards().size()).isEqualTo(0);
        for (int i=0; i < 10; i++) {
            ClubBoard newBoard = ClubBoard.of(club, buildAndSaveBoard(boardName + i), boardGroup, 0);
            boardGroupRepository.insertBoard(boardGroup, newBoard, i);
        }
        return boardGroup;
    }

    @Test
    public void insertBoard() {
        String boardName = "boardName";
        BoardGroup boardGroup = buildAndSaveMockBoardGroup(boardName);
        BoardGroup savedBoardGroup =
                boardGroupRepository.findById(boardGroup.getBoardGroupId()).orElseThrow();
        List<ClubBoard> clubBoards = savedBoardGroup.getClubBoards();
        for (int i=0; i < 10; i++) {
            assertThat(clubBoards.get(i).getBoard().getBoardName()).isEqualTo(boardName + i);
        }
    }

    @Test
    public void insertBoardAtTheMidOfList() {
        String boardName = "boardName";
        BoardGroup boardGroup = buildAndSaveMockBoardGroup(boardName);
        BoardGroup savedBoardGroup =
                boardGroupRepository.findById(boardGroup.getBoardGroupId()).orElseThrow();
        ClubBoard a = buildAndSaveClubBoard(savedBoardGroup.getClub(), buildAndSaveBoard("a"), savedBoardGroup);
        ClubBoard b = buildAndSaveClubBoard(savedBoardGroup.getClub(), buildAndSaveBoard("b"), savedBoardGroup);
        ClubBoard c = buildAndSaveClubBoard(savedBoardGroup.getClub(), buildAndSaveBoard("c"), savedBoardGroup);
        savedBoardGroup.getClubBoards().add(1, a);
        savedBoardGroup.getClubBoards().add(4, b);
        savedBoardGroup.getClubBoards().add(9, c);

        BoardGroup newSaved = boardGroupRepository.findById(savedBoardGroup.getBoardGroupId()).orElseThrow();
        assertThat(newSaved.getClubBoards().get(1).getBoard().getBoardName()).isEqualTo("a");
        assertThat(newSaved.getClubBoards().get(4).getBoard().getBoardName()).isEqualTo("b");
        assertThat(newSaved.getClubBoards().get(9).getBoard().getBoardName()).isEqualTo("c");
    }

}
