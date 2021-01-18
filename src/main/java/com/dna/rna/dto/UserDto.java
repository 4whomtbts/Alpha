package com.dna.rna.dto;

import com.dna.rna.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserDto {

    public static class MyPage {

    }

    @Getter
    @Setter
    @AllArgsConstructor
    // 글 작성자로서의 유저에 대한 response
    public static class ResAuthor {
        private long userId;
        private String userName;

        public ResAuthor(User user) {
            this(user.getId(), user.getUserName());
        }
    }

}
