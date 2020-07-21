package com.dna.rna.domain.ClubBoard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubBoardRepository extends JpaRepository<ClubBoard, Long>, CustomClubBoardRepository {


}

