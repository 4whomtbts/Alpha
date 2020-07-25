package com.dna.rna.service;

import com.dna.rna.domain.article.Article;
import com.dna.rna.domain.article.ArticleRepository;
import com.dna.rna.domain.board.Board;
import com.dna.rna.domain.board.BoardRepository;
import com.dna.rna.domain.boardGroup.BoardGroupRepository;
import com.dna.rna.domain.club.Club;
import com.dna.rna.domain.club.ClubRepository;
import com.dna.rna.domain.clubBoard.ClubBoard;
import com.dna.rna.domain.clubBoard.ClubBoardRepository;
import com.dna.rna.domain.clubUser.ClubUserRepository;
import com.dna.rna.domain.school.School;
import com.dna.rna.domain.school.SchoolRepository;
import com.dna.rna.domain.testUtils.RNAJpaTestUtils;
import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import com.dna.rna.dto.ArticleDto;
import com.dna.rna.service.util.PageRequest;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardServiceTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BoardGroupRepository boardGroupRepository;

    @Autowired
    protected SchoolRepository schoolRepository;

    @Autowired
    protected ClubRepository clubRepository;

    @Autowired
    protected ClubUserRepository clubUserRepository;

    @Autowired
    protected BoardRepository boardRepository;

    @Autowired
    protected ClubBoardRepository clubBoardRepository;

    @Autowired
    protected ArticleRepository articleRepository;

    @InjectMocks
    private ClubService clubService;

    private BoardService boardService;

    @Before
    public void setUp() {
        boardService = new BoardService(boardRepository, articleRepository);
    }
    @Test
    public void Test_getArticlesByBoardId() {
        User author = User.of("4whomtbts", "jun", "1234");
        userRepository.save(author);
        School school = School.of("dongguk");
        schoolRepository.save(school);
        Club club = Club.of(school, "rna", author, LocalDate.now(), "1기", "dongguk", "short", "long", "URI");
        clubRepository.save(club);
        Board board = Board.of("board");
        boardRepository.save(board);
        List<Article> articleList = new ArrayList<>();
        for (int i=0; i < 30; i++) {
            Article article = Article.of(author, board, new ArrayList<>(), "title" + i, "content" + i);
            articleList.add(article);
            articleRepository.save(article);
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setDirection(Sort.Direction.ASC);
        pageRequest.setSize(10);
        pageRequest.setPage(0);
        Page<ArticleDto.ListPreview> result = boardService.getArticlesByBoardId(board.getId(), pageRequest.of());
        assertThat(result.getSize()).isEqualTo(10);
        List<ArticleDto.ListPreview> pageResult = result.getContent();
        assertThat(pageResult.get(0).getAuthor()).isEqualTo("jun");
        assertThat(pageResult.get(0).getTitle()).isEqualTo("title0");
    }


}
