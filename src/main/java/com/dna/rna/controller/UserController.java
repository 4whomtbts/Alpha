package com.dna.rna.controller;

        import com.dna.rna.exception.DCloudException;
        import com.dna.rna.service.UserService;
        import lombok.RequiredArgsConstructor;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.data.repository.query.Param;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.security.access.annotation.Secured;
        import org.springframework.security.access.prepost.PreAuthorize;
        import org.springframework.security.core.Authentication;
        import org.springframework.web.bind.annotation.*;

        import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PreAuthorize("isAuthenticated() and hasRole('UESR')")
    @ResponseBody
    @GetMapping("/user")
    public String getUser() {
        String result  = userService.getUserRoles("jun");
        return result;
    }

    @ResponseBody
    @DeleteMapping("/users/user/{userId}")
    public ResponseEntity<String> deleteUser(Authentication authentication, @PathVariable("userId") long userId) {
        System.out.println("userId=  " + userId);
        throw DCloudException.ofInternalServerError("hello", "world");
//        return new ResponseEntity<String>("hello world!", HttpStatus.OK);
    }

}
