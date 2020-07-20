package com.dna.rna.domain.ClubBoard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubBoardRepository extends JpaRepository<ClubBoard, Long>, CustomClubBoardRepository {

    public List<ClubBoard> findClubBoardsByClub_ClubIdAndBoardGroupIsNullOrderByDisplayOrderAsc(long clubId);
}
