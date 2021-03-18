package com.dna.rna.domain.user;

import com.dna.rna.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserRepositoryTest {

    private User save_want_user;

    @BeforeEach
    void beforeEach() {

    }

    @Test
    @DisplayName("save 성공")
    void save_O() {

    }
    @Test
    @DisplayName("save 실패 - loginId에 해당하는 유저가 1개 존재")
    void save_X_1() {

    }
    @Test
    @DisplayName("save 실패 - loginId에 해당하는 유저가 2개 이상")
    void save_X_2() {

    }
}
