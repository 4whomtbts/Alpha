package com.dna.rna.domain.ClubBoard;

import java.util.List;

public interface CustomClubBoardRepository {

    public List<ClubBoard> findClubBoardsByClubAndBoardGroupIsNullOrderByDisplayOrderAsc(long clubId);

}
