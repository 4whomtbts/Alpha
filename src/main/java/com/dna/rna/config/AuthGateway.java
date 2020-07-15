package com.dna.rna.config;

import com.dna.rna.domain.SchoolUser.SchoolUserRepository;
import com.dna.rna.domain.User.User;
import com.dna.rna.domain.User.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration("AuthGateway")
@AllArgsConstructor
public class AuthGateway {

    private final UserRepository userRepository;
    private final SchoolUserRepository schoolUserRepository;

    private boolean schoolUserImmigration(String URI) {
        User user = userRepository.findUserByLoginId("jun");
        System.out.println(user.getId());
        if (user != null) {
            return true;
        }
        return false;
    }

    public boolean immigration (String URI) {
        if (URI.startsWith("/schools")) {
            return schoolUserImmigration(URI);
        }
        return false;
    }
}
