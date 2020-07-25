package com.dna.rna.controller;

import com.dna.rna.domain.article.Article;
import com.dna.rna.domain.article.ArticleRepository;
import com.dna.rna.domain.board.Board;
import com.dna.rna.domain.board.BoardRepository;
import com.dna.rna.domain.club.Club;
import com.dna.rna.domain.club.ClubRepository;
import com.dna.rna.domain.clubBoard.ClubBoard;
import com.dna.rna.domain.clubBoard.ClubBoardRepository;
import com.dna.rna.domain.clubUser.ClubUser;
import com.dna.rna.domain.clubUser.ClubUserRepository;
import com.dna.rna.domain.clubUser.ClubUserStatus;
import com.dna.rna.domain.project.Project;
import com.dna.rna.domain.project.ProjectRepository;
import com.dna.rna.domain.school.School;
import com.dna.rna.domain.school.SchoolRepository;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.domain.boardGroup.BoardGroup;
import com.dna.rna.domain.boardGroup.BoardGroupRepository;
import com.dna.rna.dto.ClubDto;
import com.dna.rna.service.ClubService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Api(value ="club 컨트롤러 v1")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
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
    private final ArticleRepository articleRepository;

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
        boardRepository.save(board);
        List<Article> articleList = new ArrayList<>();
        for (int i=0; i < 30; i++) {
            Article article = Article.of(user, board, new ArrayList<>(), "title" + i, "content" + i);
            articleList.add(article);
            articleRepository.save(article);
        }
        return club.getId().toString() + "," + board.getId();
    }

}
