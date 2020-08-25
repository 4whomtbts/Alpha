package com.dna.rna.controller;

import com.dna.rna.service.InstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InstanceController {

    private final InstanceService instanceService;

    @DeleteMapping("/instances/instance/{instance_hash}")
    public ResponseEntity deleteInstanceById(@PathVariable("instance_hash") long instanceHash) throws Exception {
        instanceService.deleteInstance(instanceHash);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/instances/instance/{instance_hash}", method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity food(@PathVariable("instance_hash") String instanceHash) throws Exception {
        //instanceService.deleteInstance(instanceHash);
        return new ResponseEntity(HttpStatus.OK);
    }

}
