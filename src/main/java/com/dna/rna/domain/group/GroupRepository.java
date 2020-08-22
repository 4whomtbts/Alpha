package com.dna.rna.domain.group;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long>, CustomGroupRepository {

    public Group findByGroupName(String groupName);
}
