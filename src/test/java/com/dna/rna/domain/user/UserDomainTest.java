package com.dna.rna.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class UserDomainTest {
    String[] params = { "GoodGoodGirl", "Jane Doe", "1234", "Soviet", "010-1234-5678", "good@a.com" };
    @Test
    @DisplayName("성공")
    void make_O() {
        User user = User.of("GoodGoodGirl", "Jane Doe", "1234", "Soviet", "010-1234-5678", "good@a.com");
        assertThat(user).isNotNull();
    }
    @Test
    @DisplayName("패착 - 필요한 파라미터 null일 때 NullPointerException")
    void make_X() {
        String[] what = {"loginId", "userName", "password", "organization", "phone", "email"};
        for (int i = 0; i < 6; i++){
            String[] tmp = new String[6];
            System.arraycopy(params, 0, tmp, 0, 6);
            tmp[i] = null;
            checkTroll(what[i] + " is null", tmp);
        }
    }
    void checkTroll(String message, String[] args) {
        Exception e = assertThrows(NullPointerException.class,
                () ->User.of(args[0], args[1], args[2], args[3], args[4], args[5]));
        assertThat(e.getMessage()).isEqualTo(message);
    }
}
