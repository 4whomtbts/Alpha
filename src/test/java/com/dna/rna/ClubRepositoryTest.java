package com.dna.rna;

import com.dna.rna.domain.Club.Club;
import com.dna.rna.domain.Club.ClubRepository;
import com.dna.rna.domain.ClubUser.ClubUser;
import com.dna.rna.domain.ClubUser.ClubUserRepository;
import com.dna.rna.domain.ClubUser.ClubUserStatus;
import com.dna.rna.domain.School.School;
import com.dna.rna.domain.School.SchoolRepository;
import com.dna.rna.domain.User.User;
import com.dna.rna.domain.User.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClubRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubUserRepository clubUserRepository;
    @Autowired
    private SchoolRepository schoolRepository;

    @Before
    public void setUp() {
        School school = School.of("univ");
        schoolRepository.save(school);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveDoesntAllowDuplicate() {
        School school = schoolRepository.findBySchoolName("univ");
        Club club = Club.of(school, "MOCK CLUB", LocalDate.now(), "1", "seoul", "hello", "toooo long");
        Club club1 = Club.of(school, "MOCK CLUB", LocalDate.now(), "1", "seoul", "hello", "toooo long");
        clubRepository.save(club);
        clubRepository.save(club1);
        thrown.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void findClubByName() {
        School school = schoolRepository.findBySchoolName("univ");
        Club club = Club.of(school, "MOCK CLUB", LocalDate.now(), "1", "seoul", "hello", "toooo long");
        clubRepository.save(club);
        Club result = clubRepository.findByClubName(club.getClubName());
        assertThat(result).isNotNull();
    }


    @Test
    public void grantUserLeaderRoleSuccess() {
        final String mockUserLoginId = "mock";
        User mockUser = User.of(mockUserLoginId, "hello", "password");
        userRepository.save(mockUser);
        School school = schoolRepository.findBySchoolName("univ");
        Club club = Club.of(school, "MOCK CLUB", LocalDate.now(), "1", "seoul", "hello", "toooo long");
        clubRepository.save(club);
        ClubUser clubUser = ClubUser.of(mockUser, club, "1", ClubUserStatus.ACTIVE);
        clubUserRepository.save(clubUser);
        assertThat(clubRepository.grantUserLeaderRole(club.getId(), mockUserLoginId)).isEqualTo(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void grantUserLeaderRoleFailWhenNoCorrespondClub() {
        final String mockUserLoginId = "mock";
        User mockUser = User.of(mockUserLoginId, "hello", "password");
        userRepository.save(mockUser);
        School school = schoolRepository.findBySchoolName("univ");
        Club club = Club.of(school, "MOCK CLUB", LocalDate.now(), "1기", "seoul", "hello", "toooo long");
        clubRepository.save(club);
        ClubUser clubUser = ClubUser.of(mockUser, club, "1기", ClubUserStatus.ACTIVE);
        clubUserRepository.save(clubUser);
        clubRepository.grantUserLeaderRole(Long.MAX_VALUE, mockUserLoginId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void grantUserLeaderRoleFailWhenNoCorrespondUser() {
        final String mockUserLoginId = "mock";
        final String invalidUserLoginId = "notexisits";
        User mockUser = User.of(mockUserLoginId, "hello", "password");
        userRepository.save(mockUser);
        School school = schoolRepository.findBySchoolName("univ");
        Club club = Club.of(school, "MOCK CLUB", LocalDate.now(), "1기", "seoul", "hello", "toooo long");
        clubRepository.save(club);
        ClubUser clubUser = ClubUser.of(mockUser, club, "1기", ClubUserStatus.ACTIVE);
        clubUserRepository.save(clubUser);
        clubRepository.grantUserLeaderRole(club.getId(), invalidUserLoginId);
        thrown.expect(IllegalArgumentException.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void grantUserLeaderRoleDoesntAllowNotExistingUser() {
        final String mockUserLoginId = "mock";
        User mockUser = User.of(mockUserLoginId, "hello", "password");
        userRepository.save(mockUser);
        School school = schoolRepository.findBySchoolName("univ");
        Club club = Club.of(school, "MOCK CLUB", LocalDate.now(), "1기", "seoul", "hello", "toooo long");
        clubRepository.save(club);
        Club savedClub = clubRepository.findByClubName(club.getClubName());
        clubRepository.grantUserLeaderRole(savedClub.getId(), mockUserLoginId);
        thrown.expect(IllegalArgumentException.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void grantUserLeaderRoleDoesntAllowNotClubMemberUserTobeLeader() {
        String invalidUserLoginId= "not existing user";
        School school = schoolRepository.findBySchoolName("univ");
        Club club = Club.of(school, "MOCK CLUB", LocalDate.now(), "1기", "seoul", "hello", "toooo long");
        clubRepository.save(club);
        Club savedClub = clubRepository.findByClubName(club.getClubName());
        clubRepository.grantUserLeaderRole(savedClub.getId(), invalidUserLoginId);
        thrown.expect(IllegalArgumentException.class);
    }

    private User buildUser(String mockUserName) {
        User mockUser = User.of(mockUserName, "hello", "password");
        userRepository.save(mockUser);
        return mockUser;
    }

    private Club buildClub(String mockClubName) {
        School school = schoolRepository.findBySchoolName("univ");
        Club club = Club.of(school, mockClubName, LocalDate.now(), "1기", "seoul", "hello", "toooo long");
        clubRepository.save(club);
        return club;
    }

    @Test
    public void fetchLeader() {
        final String mockUserLoginId = "MOCKUSER";
        final String mockClubName = "MOCK CLUB";
        User mockUser = buildUser(mockUserLoginId);
        Club club = buildClub(mockClubName);
        Club savedClub = clubRepository.findByClubName(mockClubName);
        assertThat(clubRepository.fetchLeader(savedClub.getId())).isNull();
        ClubUser clubUser = ClubUser.of(mockUser, club, "1기", ClubUserStatus.ACTIVE);
        clubUserRepository.save(clubUser);
        clubRepository.grantUserLeaderRole(club.getId(), mockUserLoginId);
        //assertThat(clubRepository.fetchLeader(club.getId()).getId()).isEqualTo(mockUser.getId());
        clubUserRepository.isUserMemberOfClub(club.getId(), mockUser.getLoginId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchLeaderFailWhenNoClubOfClubId() {
        final long invalidClubId = Long.MAX_VALUE;
        final String mockUserLoginId = "MOCKUSER";
        final String mockClubName = "MOCK CLUB";
        User mockUser = buildUser(mockUserLoginId);
        Club club = buildClub(mockClubName);
        Club savedClub = clubRepository.findByClubName(mockClubName);
        assertThat(clubRepository.fetchLeader(savedClub.getId())).isNull();
        ClubUser clubUser = ClubUser.of(mockUser, club, "1기", ClubUserStatus.ACTIVE);
        clubUserRepository.save(clubUser);
        clubRepository.fetchLeader(invalidClubId);
        thrown.expect(IllegalArgumentException.class);
    }

    private long buildMockClubUsers(String clubName, long numberOfUsers) {
        final String mockUserLoginId = "MOCKUSER";
        Club club = buildClub(clubName);
        Club savedClub = clubRepository.findByClubName(clubName);

        for (int i=0; i < numberOfUsers; i++) {
            String userLoginId = mockUserLoginId + i;
            User mockUser = buildUser(userLoginId);
            ClubUser clubUser = ClubUser.of(mockUser, club, "1기", ClubUserStatus.ACTIVE);
            clubUserRepository.save(clubUser);
        }
        return savedClub.getId();
    }

    // ClubId 에 매칭되는 club이 없을 시 테스트는 실패한다.
    @Test(expected = IllegalArgumentException.class)
    public void fetchSeasonUserCountFailWhenNoClubOfClubId() {
        long clubId = buildMockClubUsers("RNA", 10);
        long invalidClubId = Long.MAX_VALUE;
        clubRepository.fetchCurrentSeasonUserCount(invalidClubId);
    }

    @Test
    public void fetchSeasonUserCount() {
        long clubId = buildMockClubUsers("RNA", 10);
        assertThat(clubRepository.fetchCurrentSeasonUserCount(clubId)).isEqualTo(10);
    }
d
    @Test
    public void fetchNumberOfAllMembersFailWhenNoClubOfClubId() {
        long clubId = buildMockClubUsers("RNA", 10);
        long invalidClubId = Long.MAX_VALUE;
        assertThat(clubRepository.fetchNumberOfAllMembers(invalidClubId)).isEqualTo(0);
    }

    @Test
    public void fetchNumberOfAllMembers() {
        assertThat(clubRepository.fetchNumberOfAllMembers(Long.MAX_VALUE)).isEqualTo(0);
        long rnaClubId = buildMockClubUsers("RNA", 5);
        assertThat(clubRepository.fetchNumberOfAllMembers(rnaClubId)).isEqualTo(5);
    }

}
