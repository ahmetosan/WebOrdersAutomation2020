package com.weborders.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.weborders.utilities.BrowserUtilities;
import com.weborders.utilities.ConfigurationReader;
import com.weborders.utilities.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.IOException;

public abstract class AbstractBaseTest {


protected WebDriver driver = Driver.getDriver();
protected static ExtentReports extentReports;
protected static ExtentHtmlReporter extentHtmlReporter;
protected static ExtentTest extentTest;





@BeforeTest
public void beforeTest(){
extentReports=new ExtentReports();

    String reportPath = "";
    //location of report file
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
        reportPath = System.getProperty("user.dir") + "\\test-output\\report.html"; //+ reportName;
    } else {
        reportPath = System.getProperty("user.dir") + "/test-output/report.html"; //" + reportName;
    }
    extentHtmlReporter =new ExtentHtmlReporter(reportPath);
    extentReports.attachReporter(extentHtmlReporter);
    extentHtmlReporter.config().setReportName("weborder automation");

}

@AfterTest
public void afterTest(){
extentReports.flush();
}



@BeforeMethod
    public void setup(){
    driver.get(ConfigurationReader.getProperty("url"));
    driver.manage().window().maximize();

}

@AfterMethod
    public void teardown(ITestResult testResult){
    if(testResult.getStatus()==ITestResult.FAILURE){
        String screenshotLocation = BrowserUtilities.getScreenshoot(testResult.getName());
        try {
            extentTest.fail(testResult.getName());
            extentTest.addScreenCaptureFromPath(screenshotLocation);
            extentTest.fail(testResult.getThrowable());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to attached screen shot");
        }
    }else if(testResult.getStatus()==ITestResult.SUCCESS){
        extentTest.pass(testResult.getName());
    }else if (testResult.getStatus()==ITestResult.SKIP){
        extentTest.skip(testResult.getName());
    }
Driver.closeDriver();
}





}
