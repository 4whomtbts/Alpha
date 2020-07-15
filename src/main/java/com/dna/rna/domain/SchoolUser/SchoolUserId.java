package com.dna.rna.domain.SchoolUser;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class SchoolUserId implements Serializable {

    private long school;
    private long user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolUserId that = (SchoolUserId) o;
        return school == that.school &&
                user == that.user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(school, user);
    }
}
