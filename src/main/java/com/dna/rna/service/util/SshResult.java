package com.dna.rna.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SshResult<T> {

    private SshResultError error;
    private T result;

}
