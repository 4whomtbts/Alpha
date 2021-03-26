package com.dna.rna.domain.allowCode;

import com.dna.rna.domain.user.User;
import com.dna.rna.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
@EnableJpaAuditing
@Transactional
public class AllowCodeRepositoryTest {
    private User user = User.of("GoodGoodGirl", "Jane Doe", "1234", "Soviet", "010-1234-5678", "good@a.com");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AllowCodeRepository allowCodeRepository;

    @Test
    @DisplayName("unExpired 상태인 allowCode만 불러오는지 확인")
    void findUnExpiredAllowCode_O() {
        // given
        userRepository.saveUser(user);
        User findUser = userRepository.findById(1L).get();

        AllowCode expiredAllowCode = new AllowCode(findUser, "code1", AllowCodeType.ofOrdinalStatus(0));
        expiredAllowCode.setExpired(true);
        AllowCode unExpiredAllowCode = new AllowCode(findUser, "code2", AllowCodeType.ofOrdinalStatus(1));

        allowCodeRepository.save(expiredAllowCode);
        allowCodeRepository.save(unExpiredAllowCode);

        // when
        AllowCode foundAllowCode = allowCodeRepository.findUnExpiredAllowCode();

        // then
//        System.out.println("unExpiredAllowCode = " + foundAllowCode);
        assertThat(foundAllowCode.getAllowCode()).isEqualTo("code2");
        assertThat(foundAllowCode.getAllowCodeType()).isEqualTo(AllowCodeType.GROUP_JOIN);
        assertThat(foundAllowCode.getUser()).isEqualTo(findUser);
    }

}
