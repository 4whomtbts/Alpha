package com.dna.rna.controller;

import com.dna.rna.domain.user.User;
import com.dna.rna.dto.ApiResponse;
import com.dna.rna.exception.DCloudException;
import com.dna.rna.security.MainUserDetails;
import com.dna.rna.service.InstanceService;
import com.jcraft.jsch.JSchException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class InstanceController {

    private static final Logger logger = LoggerFactory.getLogger(InstanceController.class);

    private final InstanceService instanceService;

    @Secured(value = {"ROLE_MEMBER"})
    @DeleteMapping("/instances/instance/{instanceId}")
    public ResponseEntity deleteInstanceById(@PathVariable("instanceId") long instanceId,
                                             Authentication authentication) throws Exception {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        if (user == null) {
            logger.warn("심각: 로그인하지 않은 사용자가 인스턴스 [{}] 를 삭제하려 시도함", instanceId);
            return new ResponseEntity<ApiResponse>(ApiResponse.UNAUTHORIZED(), HttpStatus.UNAUTHORIZED);
        }
        instanceService.deleteInstance(instanceId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured(value = {"ROLE_MEMBER"})
    @RequestMapping(value = "/instances/instance/{instance_hash}", method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity food(@PathVariable("instance_hash") String instanceHash) throws Exception {
        //instanceService.deleteInstance(instanceHash);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/instances/instance/start/{instanceId}")
    // instanceId 를 받아서 인스턴스를 시작시켜준다.
    // 여기서 instanceId 는 database row의 pk 이다.
    public ResponseEntity startInstance(@PathVariable("instanceId") long instanceId,
                                        Authentication authentication) throws Exception {
        MainUserDetails details = (MainUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        if (user == null) {
            logger.warn("심각: 로그인하지 않은 사용자가 인스턴스 [{}] 를 시작하려 시도함", instanceId);
            return new ResponseEntity<ApiResponse>(ApiResponse.UNAUTHORIZED(), HttpStatus.UNAUTHORIZED);
        }

        try {
            instanceService.startInstance(instanceId, user.getLoginId());
        } catch (IOException ioException) {
            logger.warn("startInstance 과정에서 IOException 이 발생했습니다. [{}]",
                        ioException.getMessage());
            throw DCloudException.ofInternalServerError("startInstance IOException" + ioException.getMessage());
        } catch (JSchException sshException) {
            logger.warn("startInstance 과정에서 sshException 이 발생했습니다. [{}]",
                    sshException.getMessage());
            throw DCloudException.ofInternalServerError("sshException" + sshException.getMessage());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
