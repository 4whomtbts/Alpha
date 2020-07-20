package com.dna.rna.service;

import com.dna.rna.domain.Board.BoardItem;
import com.dna.rna.domain.Club.Club;
import com.dna.rna.domain.Club.ClubRepository;
import com.dna.rna.domain.ClubBoard.ClubBoard;
import com.dna.rna.domain.ClubBoard.ClubBoardRepository;
import com.dna.rna.domain.boardGroup.BoardGroup;
import com.dna.rna.domain.boardGroup.BoardGroupRepository;
import com.dna.rna.dto.BoardItemDto;
import com.dna.rna.dto.ClubDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClubService {

    private static final Logger logger = LoggerFactory.getLogger(ClubService.class);

    private final ClubRepository clubRepository;
    private final ClubBoardRepository clubBoardRepository;
    private final BoardGroupRepository boardGroupRepository;


    public BoardItemDto.BoardList getBoardList(List<BoardGroup> boardGroups, List<ClubBoard> clubBoards) {

        List<BoardItemDto.Item> result = new ArrayList<>(boardGroups.size() + clubBoards.size());

        for (int i=0; i < boardGroups.size(); i++) {
            BoardGroup curr = boardGroups.get(i);
            result.add(curr.getDisplayOrder(),
                    new BoardItemDto.Item(
                            curr.getBoardGroupId(),
                            BoardItemDto.BoardItemType.GROUP,
                            curr.getBoardGroupName(),
                            curr.getClubBoards()));
        }

        for (int i=0; i < clubBoards.size(); i++) {
            ClubBoard currClubBoard = clubBoards.get(i);
            result.add(currClubBoard.getDisplayOrder(),
                    new BoardItemDto.Item(
                            currClubBoard.getClubBoardId(),
                            BoardItemDto.BoardItemType.BOARD,
                            currClubBoard.getBoard().getBoardName(),
                            null));
        }
        return new BoardItemDto.BoardList(result);
    }

    public ClubDto.Response getClubHome(final long clubId) {

        List<ClubBoard> clubBoards =
                clubBoardRepository.findClubBoardsByClub_ClubIdAndBoardGroupIsNullOrderByDisplayOrderAsc(clubId);
        List<BoardGroup> boardGroups =
                boardGroupRepository.findBoardGroupsByClubIdOOrderByDisplayOrder(clubId);

        Club club = clubRepository.findById(clubId)
                                  .orElseThrow();
        return new ClubDto.Response(
                club.getProfileImageUri(),
                club.getSchool().getSchoolName(),
                club.getLeader().getUserName(),
                clubRepository.fetchCurrentSeasonUserCount(clubId),
                clubRepository.fetchNumberOfAllMembers(clubId),
                club.getSince().format(DateTimeFormatter
                                        .ofPattern("yyyy.MM.dd")),
                club.getSeason(),
                club.getLocation(),
                getBoardList(boardGroups, clubBoards));
    }

}
