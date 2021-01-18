package com.dna.rna.domain.groupUser;

import com.dna.rna.domain.user.User;

import java.util.List;

public interface CustomGroupUserRepository {

    public List<GroupUser> findByUser(User user);
}
