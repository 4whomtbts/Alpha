package com.dna.rna.domain.clubBoard;

import java.util.List;

public interface CustomClubBoardRepository {

    public List<ClubBoard> findClubBoardsByClubAndBoardGroupIsNullOrderByDisplayOrderAsc(long clubId);

}
