package com.velogm.server_velogm.service.impl;

import com.velogm.server_velogm.data.dto.TestDataDto;
import com.velogm.server_velogm.data.dto.TestDataResponseDto;
import com.velogm.server_velogm.data.entity.TestData;
import com.velogm.server_velogm.data.repository.TestDataRepository;
import com.velogm.server_velogm.service.TestDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestDataServiceImpl implements TestDataService {

    private final Logger LOGGER = LoggerFactory.getLogger(TestDataServiceImpl.class);
    private final TestDataRepository testDataRepository;

    @Autowired
    public TestDataServiceImpl(TestDataRepository testDataRepository){
        this.testDataRepository = testDataRepository;
    }

    @Override
    public TestDataResponseDto getTestData(Long number) {
        LOGGER.info("[getProduct] input number : {}", number);
        TestData testData = testDataRepository.findById(number).get();

        LOGGER.info("[getProduct] product number : {}, name : {}", testData.getNumber(), testData.getName());
        TestDataResponseDto testDataResponseDto = new TestDataResponseDto();
        testDataResponseDto.setNumber(testData.getNumber());
        testDataResponseDto.setName(testData.getName());
        testDataResponseDto.setPrice(testData.getPrice());
        testDataResponseDto.setStock(testData.getStock());

        return testDataResponseDto;
    }

    @Override
    public TestDataResponseDto saveTestData(TestDataDto testDataDto) {
        LOGGER.info("[saveProduct] productDTO : {}", testDataDto.toString());
        TestData testData = new TestData();
        testData.setName(testDataDto.getName());
        testData.setPrice(testDataDto.getPrice());
        testData.setStock(testDataDto.getStock());
        testData.setCreateAt(LocalDateTime.now());
        testData.setUpdatedAt(LocalDateTime.now());

        TestData savedTestData = testDataRepository.save(testData);
        LOGGER.info("[saveProduct] savedProduct : {}", savedTestData);

        TestDataResponseDto testDataResponseDto = new TestDataResponseDto();
        testDataResponseDto.setNumber(savedTestData.getNumber());
        testDataResponseDto.setName(savedTestData.getName());
        testDataResponseDto.setPrice(savedTestData.getPrice());
        testDataResponseDto.setStock(savedTestData.getStock());

        return testDataResponseDto;
    }

    @Override
    public TestDataResponseDto changeTestDataName(Long number, String name) {
        TestData foundTestData = testDataRepository.findById(number).get();
        foundTestData.setName(name);
        TestData changedTestData = testDataRepository.save(foundTestData);

        TestDataResponseDto testDataResponseDto = new TestDataResponseDto();
        testDataResponseDto.setNumber(changedTestData.getNumber());
        testDataResponseDto.setName(changedTestData.getName());
        testDataResponseDto.setPrice(changedTestData.getPrice());
        testDataResponseDto.setStock(changedTestData.getStock());

        return testDataResponseDto;
    }

    @Override
    public void deleteTestData(Long number) {
        testDataRepository.deleteById(number);
    }
}
