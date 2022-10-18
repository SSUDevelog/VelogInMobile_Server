package com.veloginmobile.server.service;

import com.veloginmobile.server.data.dto.TestDataDto;
import com.veloginmobile.server.data.dto.TestDataResponseDto;

public interface TestDataService {

    TestDataResponseDto getTestData(Long number);

    TestDataResponseDto saveTestData(TestDataDto testDataDto);

    TestDataResponseDto changeTestDataName(Long number, String name) throws Exception;

    void deleteTestData(Long number) throws Exception;

}
