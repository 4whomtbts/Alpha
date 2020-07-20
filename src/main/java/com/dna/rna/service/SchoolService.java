package com.dna.rna.service;

import com.dna.rna.domain.School.School;
import com.dna.rna.domain.School.SchoolRepositoryImpl;
import com.dna.rna.exception.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

/**
 * School에서 제공하는 service
 *
 * SchoolService.java
 * created 2020.4.1
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Service
public class SchoolService {

    private static final Logger logger= LoggerFactory.getLogger(SchoolService.class);

    private SchoolRepositoryImpl schoolRepository;

    @Autowired
    public SchoolService(SchoolRepositoryImpl schoolRepository) {
        this.schoolRepository = schoolRepository;
    }


    public School createSchool(String schoolName) {
        requireNonNull(schoolName, "schoolName is null");

        School existingSchool = schoolRepository.findBySchoolName(schoolName);
        if(existingSchool != null) {
            throw new AlreadyExistsException("schoolName", schoolName);
        }

        School newSchool = School.of(schoolName);
        schoolRepository.save(newSchool);
        return newSchool;
    }
}
