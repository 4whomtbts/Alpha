package com.dna.rna.domain.boardGroup;

import com.dna.rna.domain.ClubBoard.ClubBoard;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomBoardGroupRepository {
    public BoardGroup insertBoard(BoardGroup boardGroup, ClubBoard clubBoard, int index);
}
