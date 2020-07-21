package com.dna.rna.domain.admission;

/**
 * 지원현황에 대한 enum
 *
 * CandidateStateType.java
 * created 2020.4.1
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

public enum CandidateStateType {
    FAILED, // 불합격
    PASSED, // 합격
    EVALUATING, // 심사 중
    CANCELD // 기권
}
