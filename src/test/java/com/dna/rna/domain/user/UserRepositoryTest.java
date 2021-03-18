package com.dna.rna.domain.user;

import com.dna.rna.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // 가짜 DB 사용
@DataJpaTest // DB 관련된 객체만 메모리에 올라감
@EnableJpaAuditing
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save 성공")
    void save_O() {
        User save_want_user = User.of("GoodGoodGirl", "Jane Doe", "1234", "Soviet", "010-1234-5678", "good@a.com");

        userRepository.save2(save_want_user);
        User found_user = userRepository.findUserByLoginId("GoodGoodGirl");
        assertThat(found_user.getLoginId()).isEqualTo(save_want_user.getLoginId());
        assertThat(found_user.getUserName()).isEqualTo(save_want_user.getUserName());
        assertThat(found_user.getPassword()).isEqualTo(save_want_user.getPassword());
        assertThat(found_user.getOrganization()).isEqualTo(save_want_user.getOrganization());
        assertThat(found_user.getPhone()).isEqualTo(save_want_user.getPhone());
        assertThat(found_user.getEmail()).isEqualTo(save_want_user.getEmail());
        assertThat(found_user.getUserType()).isEqualTo(UserType.USER);
    }
    @Test
    @DisplayName("save 실패 - user가 null")
    void save_X_1() {
        User null_user = null;
        Exception e = assertThrows(NullPointerException.class, () -> userRepository.save2(null_user));
        assertThat(e.getMessage()).isEqualTo("user 은 null일 수 없습니다.");
    }
    @Test
    @DisplayName("save 실패 - loginId에 해당하는 유저가 이미")
    void save_X_2() {
        User save_want_user = User.of("GoodGoodGirl", "Jane Doe", "1234", "Soviet", "010-1234-5678", "good@a.com");
        userRepository.save2(save_want_user);
        User user2 = User.of("GoodGoodGirl", "Jame Doe", "1234", "Soviet", "010-1234-5678", "good@a.com");
        Exception e = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save2(user2));
        assertThat(e.getMessage()).isEqualTo(String.format("유저 loginId = [%s] 에 해당하는 유저가 이미 존재합니다.", user2.getLoginId()));
    }
}
