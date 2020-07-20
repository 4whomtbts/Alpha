package com.dna.rna.domain.boardGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardGroupRepository extends JpaRepository<BoardGroup, Long>, CustomBoardGroupRepository {

}
