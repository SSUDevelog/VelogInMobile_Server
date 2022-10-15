package com.velogm.server_velogm.service;

import com.velogm.server_velogm.data.dto.TestDataDto;
import com.velogm.server_velogm.data.dto.TestDataResponseDto;

public interface TestDataService {

    TestDataResponseDto getTestData(Long number);

    TestDataResponseDto saveTestData(TestDataDto testDataDto);

    TestDataResponseDto changeTestDataName(Long number, String name) throws Exception;

    void deleteTestData(Long number) throws Exception;

}
