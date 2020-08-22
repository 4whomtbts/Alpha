package com.dna.rna.domain.groupUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long>, CustomGroupUserRepository {
}
