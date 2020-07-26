package com.dna.rna.domain.boardGroup;

import com.dna.rna.domain.clubBoard.ClubBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BoardGroupRepositoryImpl extends QuerydslRepositorySupport implements
    CustomBoardGroupRepository {

    private static final Logger logger = LoggerFactory.getLogger(BoardGroupRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    public BoardGroupRepositoryImpl() {
        super(BoardGroup.class);
    }

    public BoardGroup insertBoard(BoardGroup boardGroup, ClubBoard clubBoard, int index) {
        List<ClubBoard> boards = boardGroup.getClubBoards();
        boards.add(index, clubBoard);
        boardGroup.setClubBoards(boards);
        em.persist(boardGroup);
        return boardGroup;
    }
    /*
    @Transactional
    public void save(BoardGroup boardGroup) {
        requireNonNull(boardGroup, "boardGroup은 null이 될 수 없습니다.");
        em.persist(boardGroup);
    }

    @Transactional
    public void insertBoard(long boardGroupId, long clubBoardId, int index) throws IllegalArgumentException {
        if (index < 0) {
            String error = String.format("심각 : index = [%d]는 유효하지 않은 값임");
            IllegalArgumentException exception = new IllegalArgumentException(error);
            logger.error(error, exception);
            throw exception;
        }
        if (boardGroupId < 0 || clubBoardId < 0) {
            String error = String.format("심각 : boardGroupId = [%d] 혹은 clubBoardId = [%d]는 유효하지 않은 값임");
            IllegalArgumentException exception = new IllegalArgumentException(error);
            logger.error(error, exception);
            throw exception;
        }

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBoardGroup qBoardGroup = QBoardGroup.boardGroup;
        BoardGroup boardGroup =
                queryFactory.selectFrom(qBoardGroup)
                            .where(qBoardGroup.boardGroupId.eq(boardGroupId)).fetchOne();
        if (boardGroup == null) {
            String error =
                    String.format("심각 : 요청받은 boardGroupId = [%d] 혹은 clubBoardId = [%d]에 해당하는" +
                            "boardGroup이 존재하지 않습니다.");
            IllegalArgumentException exception = new IllegalArgumentException(error);
            logger.error(error, exception);
            throw exception;
        }
        List<clubBoard> boards = boardGroup.getClubBoard();
        if (boards == null) {
            List<clubBoard> newBoards = new ArrayList<>();
        }

    }

    @Transactional
    public void insertBoard(BoardGroup boardGroup, clubBoard clubBoard, int index) throws IllegalArgumentException {
        requireNonNull(boardGroup, "boardGroup은 null이 될 수 없습니다.");
        requireNonNull(clubBoard, "board는 null이 될 수 없습니다,");
        insertBoard(boardGroup.getBoardGroupId(), clubBoard.getClubBoardId(), index);
    }
 */

}
