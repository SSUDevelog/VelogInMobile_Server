package com.veloginmobile.server.controller;

import com.veloginmobile.server.data.dto.test.ChangeTestDataNameDto;
import com.veloginmobile.server.data.dto.test.TestDataDto;
import com.veloginmobile.server.data.dto.test.TestDataResponseDto;
import com.veloginmobile.server.service.TestDataService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-api")
public class TestDataController {

    private final TestDataService testDataService;

    @Autowired
    public TestDataController(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    @GetMapping()
    public ResponseEntity<TestDataResponseDto> getTestData(Long number) {
        TestDataResponseDto testDataResponseDto = testDataService.getTestData(number);

        return ResponseEntity.status(HttpStatus.OK).body(testDataResponseDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @PostMapping()
    public ResponseEntity<TestDataResponseDto> createTestData(@RequestBody TestDataDto testDataDto) {
        TestDataResponseDto testDataResponseDto = testDataService.saveTestData(testDataDto);

        return ResponseEntity.status(HttpStatus.OK).body(testDataResponseDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @PutMapping()
    public ResponseEntity<TestDataResponseDto> changeTestData(
            @RequestBody ChangeTestDataNameDto changeTestDataNameDto) throws Exception {
        TestDataResponseDto testDataResponseDto = testDataService.changeTestDataName(
                changeTestDataNameDto.getNumber(),
                changeTestDataNameDto.getName());

        return ResponseEntity.status(HttpStatus.OK).body(testDataResponseDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @DeleteMapping()
    public ResponseEntity<String> deleteTestData(Long number) throws Exception {
        testDataService.deleteTestData(number);

        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }
}
