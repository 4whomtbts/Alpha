package com.dna.rna.service;

import com.dna.rna.domain.School.School;
import com.dna.rna.domain.School.SchoolRepository;
import com.dna.rna.domain.School.SchoolUserRepository;
import com.dna.rna.domain.User.UserRepository;
import com.dna.rna.exception.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
@Transactional
public class SchoolService {

    private final Logger logger= LoggerFactory.getLogger(getClass());

    private SchoolRepository schoolRepository;
    private SchoolUserRepository schoolUserRepository;
    private UserRepository userRepository;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository,
                         SchoolUserRepository schoolUserRepository,
                         UserRepository userRepository) {
        this.schoolRepository = schoolRepository;
        this.schoolUserRepository = schoolUserRepository;
        this.userRepository = userRepository;
    }


    public School createSchool(String schoolName) {
        requireNonNull(schoolName, "schoolName is null");

        School existingSchool = schoolRepository.findSchoolBySchoolName(schoolName);
        if(existingSchool != null) {
            throw new AlreadyExistsException("schoolName", schoolName);
        }

        School newSchool = School.of(schoolName);
        schoolRepository.save(newSchool);
        return newSchool;
    }
}
