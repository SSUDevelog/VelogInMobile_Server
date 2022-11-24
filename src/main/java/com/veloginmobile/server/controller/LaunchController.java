package com.veloginmobile.server.controller;

import com.veloginmobile.server.data.dto.launch.VersionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("launch")
public class LaunchController {
    @GetMapping(value = "/version")
    public ResponseEntity<VersionDto> versionCheck() {
        VersionDto versionDto = new VersionDto("1.0.0");
        return ResponseEntity.status(HttpStatus.OK).body(versionDto);
    }
}