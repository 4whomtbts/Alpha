package com.dna.rna.domain.clubBoard;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubBoardRepository extends JpaRepository<ClubBoard, Long>, CustomClubBoardRepository {


}

