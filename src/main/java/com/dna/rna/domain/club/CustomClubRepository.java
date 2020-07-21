package com.dna.rna.domain.club;

import com.dna.rna.domain.user.User;

import javax.transaction.Transactional;

public interface CustomClubRepository {

    //@Transactional
    //public ClubDto save(final ClubDto club) throws DataIntegrityViolationException;

    @Transactional
    public Club findByClubName(final String clubName);

    @Transactional
    public boolean grantUserLeaderRole(final long clubId, final String loginId) throws IllegalArgumentException;

    @Transactional
    public User fetchLeader(final long clubId) throws IllegalArgumentException;

    @Transactional
    public long fetchCurrentSeasonUserCount(final long clubId);

    @Transactional
    // 존재하지않는 clubId 에 대해서도 0을 리턴함
    public long fetchNumberOfAllMembers(final long clubId);

}
