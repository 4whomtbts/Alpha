package com.dna.rna.service;

import com.dna.rna.domain.club.Club;
import com.dna.rna.domain.club.ClubRepository;
import com.dna.rna.domain.clubBoard.ClubBoard;
import com.dna.rna.domain.clubBoard.ClubBoardRepository;
import com.dna.rna.domain.project.Project;
import com.dna.rna.domain.project.ProjectRepository;
import com.dna.rna.dto.*;
import com.dna.rna.domain.boardGroup.BoardGroup;
import com.dna.rna.domain.boardGroup.BoardGroupRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClubService {

    private static final Logger logger = LoggerFactory.getLogger(ClubService.class);

    private final ClubRepository clubRepository;
    private final ClubBoardRepository clubBoardRepository;
    private final BoardGroupRepository boardGroupRepository;
    private final ProjectRepository projectRepository;


    public BoardItemDto.BoardList getBoardList(List<BoardGroup> boardGroups, List<ClubBoard> clubBoards) {

        List<BoardItemDto.Item> result = new ArrayList<>(boardGroups.size() + clubBoards.size());

        for (int i=0; i < boardGroups.size(); i++) {
            BoardGroup curr = boardGroups.get(i);
            result.add(curr.getDisplayOrder(),
                    new BoardItemDto.Item(
                            curr.getBoardGroupId(),
                            BoardItemDto.BoardItemType.GROUP,
                            curr.getBoardGroupName(),
                            ClubBoardDto.clubBoardsToClubBoardDtos(clubBoards)));
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
                clubBoardRepository.findClubBoardsByClubAndBoardGroupIsNullOrderByDisplayOrderAsc(clubId);
        List<BoardGroup> boardGroups =
                boardGroupRepository.findBoardGroupsByClubIdOrderByDisplayOrder(clubId);

        List<Club> clubs = clubRepository.findAll();
        Club club = clubRepository.getOne(clubId);
        System.out.println(clubs.size());

        List<Project> projects = projectRepository.findProjectsByClub_Id(clubId);
        List<ProjectDto.Card> projectCards = new ArrayList<>();
        for (int i=0; i < projects.size(); i++) {
            Project project = projects.get(i);
            long remainDay = project.getHiring() ?
                    (ChronoUnit.DAYS.between(LocalDate.now(), project.getHiringDueDate())) :
                    -1;
            projectCards.add(
                    new ProjectDto.Card(
                            project.getProjectId(),
                            remainDay,
                            new ClubUserDto.Response(
                                    project.getMentor().getId(),
                                    project.getMentor().getUser().getId(),
                                    project.getMentor().getUser().getUserName()),
                            project.getCurrentMember(),
                            project.getTotalMember(),
                            project.getStartDate(),
                            project.getEndDate(),
                            project.getHiring()));
        }

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
                getBoardList(boardGroups, clubBoards),
                projectCards);
    }

}
