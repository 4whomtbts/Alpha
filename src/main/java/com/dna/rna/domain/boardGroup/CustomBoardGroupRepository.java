package com.dna.rna.domain.boardGroup;

import com.dna.rna.domain.clubBoard.ClubBoard;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomBoardGroupRepository {
    public BoardGroup insertBoard(BoardGroup boardGroup, ClubBoard clubBoard, int index);
}
