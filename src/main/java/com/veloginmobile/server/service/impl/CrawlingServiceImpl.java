package com.veloginmobile.server.service.impl;

import com.veloginmobile.server.service.CrawlingService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class CrawlingServiceImpl implements CrawlingService {
    //사용법
    //crawlingService.crawling("https://velog.io/@morethanmin", "//*[@id=\"root\"]/div[2]/div[3]/div[4]/div[3]/div/div");

    private WebDriver driver;

    @Value("${chromeDriver.path}")
    private String chromeDriverPath;

    public void crawling(String url, String xpath){
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        options.addArguments("headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");			//gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        driver = new ChromeDriver(options);//open과 close함수를 따로 분리해서 사용하거나 getDataList에서 한번에 다 크롤링해서 DB에 박아넣기. 내부에서 PostDto객체 생성?

        try{
            getDataList(url, xpath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close();
        driver.quit();
    }

    private String getDataList(String url, String xpath) throws InterruptedException{

        List<String> list = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(url);
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))
        );

        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        
        //디버그
        for (WebElement element : elements) {
            System.out.println("crawling");
            System.out.println("----------------------------");
            System.out.println(element.getText());//이렇게 되어서 내부로직을 바꿔야 할것 같은 느낌.
        }

        return elements.toString();
    }
}
