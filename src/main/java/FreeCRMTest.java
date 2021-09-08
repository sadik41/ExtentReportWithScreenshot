import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FreeCRMTest {

    public WebDriver driver;

    public ExtentReports extent;
    public ExtentTest extentTest;

    @BeforeTest
    public void setExtent(){
        extent=new ExtentReports(System.getProperty("user.dir")+"/test-output/Extent.html",true);
        extent.addSystemInfo("Host Name","Sadik");
        extent.addSystemInfo("User Name","Sadik Automation");
        extent.addSystemInfo("Environment","QA");
    }

    @AfterTest
    public void endReport(){
        extent.flush();
        extent.close();
    }

    public static String getScreenShot(WebDriver driver,String screenshotName) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddmmss").format(new Date());

        TakesScreenshot ts=(TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") +"/FailedTestedScreensots/"+ screenshotName + dateName +".png";

        File finalDestination=new File(destination);
        FileUtils.copyFile(source,finalDestination);
        return destination;

    }

    @BeforeMethod
    public void setUp(){

        System.setProperty("webdriver.chrome.driver","C:/WebDrivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        driver.get("https://www.freecrm.com/");
    }

    @Test
    public void freeCRMTitleTest(){
        extentTest=extent.startTest("freeCRMTitleTest");
        String title=driver.getTitle();
        System.out.println(title);
        Assert.assertEquals(title,"# Free CRM customer relationship management software cloud");
    }

    @Test
    public void freeCRMTLogoTest(){
        extentTest=extent.startTest("freeCRMTLogoTest");
        boolean b=driver.findElement(By.xpath("//h1[@class='txt-white']")).isDisplayed();
        Assert.assertTrue(b);
    }


    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus()==ITestResult.FAILURE){
            extentTest.log(LogStatus.FAIL,"TEST CASE FAILED IS "+result.getName()); //to add name in extent report
            extentTest.log(LogStatus.FAIL,"TEST CASE FAILED IS "+result.getThrowable()); // to add error/exception in extent report

            String screenshotPath=getScreenShot(driver,result.getName());
            extentTest.log(LogStatus.FAIL,extentTest.addScreenCapture(screenshotPath)); // to add screenshot in extent report
           //extentTest.log(LogStatus.FAIL,extentTest.addScreencast(screenshotPath)); // to add screenCast in extent report

        }
        else if (result.getStatus()==ITestResult.SUCCESS){
            extentTest.log(LogStatus.PASS,"TEST CASE PASSED IS "+result.getName());
        }

        else if (result.getStatus()==ITestResult.SKIP){
            extentTest.log(LogStatus.SKIP,"TEST CASE SKIPPED IS "+result.getName());
        }

        extent.endTest(extentTest); //ending test and ends the current test and prepare to create html report
        driver.quit();
    }
}
