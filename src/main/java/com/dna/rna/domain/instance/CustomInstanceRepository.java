package com.dna.rna.domain.instance;

import java.util.List;

public interface CustomInstanceRepository {

    List<Instance> findByUserLoginInd(String loginId);
}
