package com.dna.rna.domain.allowCode;

public interface CustomAllowCodeRepository {

    public AllowCode findUnExpiredAllowCode();

}
