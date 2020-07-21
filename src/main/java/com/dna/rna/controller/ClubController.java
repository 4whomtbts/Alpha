package com.dna.rna.controller;

import com.dna.rna.domain.Board.Board;
import com.dna.rna.domain.Board.BoardRepository;
import com.dna.rna.domain.Club.Club;
import com.dna.rna.domain.Club.ClubRepository;
import com.dna.rna.domain.ClubBoard.ClubBoard;
import com.dna.rna.domain.ClubBoard.ClubBoardRepository;
import com.dna.rna.domain.ClubUser.ClubUser;
import com.dna.rna.domain.ClubUser.ClubUserRepository;
import com.dna.rna.domain.ClubUser.ClubUserStatus;
import com.dna.rna.domain.Project.Project;
import com.dna.rna.domain.Project.ProjectRepository;
import com.dna.rna.domain.School.School;
import com.dna.rna.domain.School.SchoolRepository;
import com.dna.rna.domain.User.User;
import com.dna.rna.domain.User.UserRepository;
import com.dna.rna.domain.User.UserRepositoryImpl;
import com.dna.rna.domain.boardGroup.BoardGroup;
import com.dna.rna.domain.boardGroup.BoardGroupRepository;
import com.dna.rna.dto.ClubDto;
import com.dna.rna.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;


@RestController
@RequiredArgsConstructor
public class ClubController {

    private static final Logger logger = LoggerFactory.getLogger(ClubController.class);

    private final ClubService clubService;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final BoardGroupRepository boardGroupRepository;
    private final ProjectRepository projectRepository;
    private final ClubUserRepository clubUserRepository;
    private final BoardRepository boardRepository;
    private final ClubBoardRepository clubBoardRepository;

    @ResponseBody
    @GetMapping("/clubs/club/{clubId}")
    public ClubDto.Response getClubIndex(@PathVariable("clubId") long clubId) {
         return clubService.getClubHome(clubId);
    }

    @ResponseBody
    @GetMapping("/init")
    public String init() {
        User user = userRepository.findUserByLoginId("4whomtbts");
        School school = School.of("동국대학교");
        school =schoolRepository.save(school);
        Club club = Club.of(school, "RNA", user, LocalDate.now(), "1기", "학생회관", "short", "long", "uri");
        club = clubRepository.save(club);
        ClubUser clubUser = clubUserRepository.save(ClubUser.of(user, club, "1기", ClubUserStatus.ACTIVE));
        BoardGroup boardGroup = BoardGroup.of(club, "소모임 게시판", 0, new ArrayList<>());
        boardGroup = boardGroupRepository.save(boardGroup);
        Board groupsBoard = boardRepository.save(Board.of("그룹소속게시판1"));
        ClubBoard gClubBoard = ClubBoard.of(club, groupsBoard, boardGroup, 0);
        gClubBoard = clubBoardRepository.save(gClubBoard);
        boardGroup.getClubBoards().add(gClubBoard);
        Board board = boardRepository.save(Board.of("게시판1"));
        ClubBoard clubBoard1 = ClubBoard.of(club, board, null, 1);
        clubBoard1 = clubBoardRepository.save(clubBoard1);
        Project project1 = Project.of("spring", "스프링 스터디", club, clubUser, 10, LocalDate.now(), LocalDate.now());
        project1 = projectRepository.save(project1);
        return club.getId().toString();
    }

}
