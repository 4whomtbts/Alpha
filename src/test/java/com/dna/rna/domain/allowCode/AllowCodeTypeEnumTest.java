package com.dna.rna.domain.allowCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AllowCodeTypeEnumTest {
    
    @Test
    @DisplayName("0번과 1번에 대해서 제대로 만들어짐")
    void ofOrdinalStatus_O() {
        AllowCodeType type1 = AllowCodeType.ofOrdinalStatus(0);
        assertThat(type1).isEqualTo(AllowCodeType.ACCOUNT_REGISTRATION);

        AllowCodeType type2 = AllowCodeType.ofOrdinalStatus(1);
        assertThat(type2).isEqualTo(AllowCodeType.GROUP_JOIN);
    }

    @Test
    @DisplayName("존재하지 않는 숫자인 -1, 3을 넣을 경우 오류")
    void ofOrdinalStatus_X() {
        assertThrows(IllegalArgumentException.class, () -> AllowCodeType.ofOrdinalStatus(-1));
        assertThrows(IllegalArgumentException.class, () -> AllowCodeType.ofOrdinalStatus(3));
    }
}
