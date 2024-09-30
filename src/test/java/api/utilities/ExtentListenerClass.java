package api.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentListenerClass implements ITestListener {

    ExtentSparkReporter extentSparkReporter;
    ExtentReports extentReport;
    ExtentTest extentTest;

    public void configureReport()
    {

        String timestamp = new SimpleDateFormat("yyyy.mm.dd.hh.mm.ss").format(new Date());
        String reportName = "Restful-BookerAutomationTestReport-" + timestamp + ".html";
        extentSparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "//Reports//" + reportName);
        extentReport = new ExtentReports();
        extentReport.attachReporter(extentSparkReporter);

        extentSparkReporter.config().setDocumentTitle("Restful-BookerAutomationTestReport");
        extentSparkReporter.config().setReportName("Restful-BookerAutomationTestReport");
    }

    //OnStart method is called when any Test starts.
    public void onStart(ITestContext Result)
    {
        configureReport();
        System.out.println("On Start method invoked....");
    }

    //onFinish method is called after all Tests are executed
    public void onFinish(ITestContext Result)
    {
        System.out.println("On Finished method invoked....");
        extentReport.flush();//it is mandatory to call flush method to ensure information is written to the started reporter.

    }


    // When Test case get failed, this method is called.
    public void onTestFailure(ITestResult Result)
    {
        System.out.println("Name of test method failed:" + Result.getName() );
        extentTest = extentReport.createTest(Result.getName());//create entry in html report
        extentTest.log(Status.FAIL, MarkupHelper.createLabel("Name of the failed test case is: " + Result.getName() , ExtentColor.RED));

        String screenShotPath = System.getProperty("user.dir") + "\\ScreenShots\\" + Result.getName() + ".png";

        File screenShotFile = new File(screenShotPath);

        if(screenShotFile.exists())
        {
            extentTest.fail("Captured Screenshot is below:" + extentTest.addScreenCaptureFromPath(screenShotPath));

        }
    }


    // When Test case get Skipped, this method is called.
    public void onTestSkipped(ITestResult Result)
    {
        System.out.println("Name of test method skipped:" + Result.getName() );

        extentTest = extentReport.createTest(Result.getName());
        extentTest.log(Status.SKIP, MarkupHelper.createLabel("Name of the skip test case is: " + Result.getName() ,ExtentColor.YELLOW));
    }


    // When Test case get Started, this method is called.
    public void onTestStart(ITestResult Result)
    {
        System.out.println("Name of test method started:" + Result.getName() );

    }


    // When Test case get passed, this method is called.
    public void onTestSuccess(ITestResult Result)
    {
        System.out.println("Name of test method sucessfully executed:" + Result.getName() );
        extentTest = extentReport.createTest(Result.getName());
        extentTest.log(Status.PASS, MarkupHelper.createLabel("Name of the passed test case is: " + Result.getName() ,ExtentColor.GREEN));
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult Result)
    {

    }


}
