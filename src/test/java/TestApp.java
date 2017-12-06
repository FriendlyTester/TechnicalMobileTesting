import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

public class TestApp
{
    @Test
    public void TestAppIOS() throws InterruptedException
    {
        System.setProperty(AppiumServiceBuilder.NODE_PATH ,
                "/usr/local/bin/node");

        System.setProperty(AppiumServiceBuilder.APPIUM_PATH ,
                "/usr/local/lib/node_modules/appium/build/lib/main.js");

        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.usingAnyFreePort();

        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(appiumServiceBuilder);
        service.start();

        if (service == null || !service.isRunning()) {
            throw new RuntimeException("An appium server node is not started!");
        }

        //https://github.com/appium/appium/blob/master/docs/en/writing-running-appium/caps.md

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone X");
        capabilities.setCapability(MobileCapabilityType.APP, "/Users/richard/Desktop/Technical Mobile Testing/TestApp.app");
        capabilities.setCapability(IOSMobileCapabilityType.LAUNCH_TIMEOUT, 500000);

        IOSDriver driver = new IOSDriver(service.getUrl(), capabilities);

        //Populate textboxs
        driver.findElementByAccessibilityId("TextField1").sendKeys("10");
        driver.findElementByAccessibilityId("TextField2").sendKeys("12");
        driver.findElementByAccessibilityId("ComputeSumButton").click();
        String answer = driver.findElementByAccessibilityId("Answer").getText();
        Assert.assertThat(answer, is(equalTo("22")));

        driver.hideKeyboard();

        //Click alert
        driver.findElementByAccessibilityId("show alert").click();
        driver.switchTo().alert().accept();

        //Click alert not using swtichTo
        driver.findElementByAccessibilityId("show alert").click();
        driver.findElementByAccessibilityId("OK").click();

        //Move Slider
        driver.findElementByXPath("//XCUIElementTypeSlider[@name=\"AppElem\"]").sendKeys("0.1");

        //Is the button enabled?
        Assert.assertThat(driver.findElementByAccessibilityId("DisabledButton").isEnabled(), is(equalTo(false)));

        //PinchZoomOnMap
        driver.findElementByAccessibilityId("Test Gesture").click();

        WebDriverWait wait = new WebDriverWait(driver, 5000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Edinburgh")));

        WebElement el = driver.findElementByAccessibilityId("Edinburgh");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, String> tapObject = new HashMap<String, String>();
        tapObject.put("element", ((MobileElement) el).getId());
        js.executeScript("mobile:doubleTap", tapObject);

        Thread.sleep(5000);

        driver.quit();
    }

    @Test
    public void AndroidVodQA() throws InterruptedException
    {
        System.setProperty(AppiumServiceBuilder.NODE_PATH ,
                "/usr/local/bin/node");

        System.setProperty(AppiumServiceBuilder.APPIUM_PATH ,
                "/usr/local/lib/node_modules/appium/build/lib/main.js");

        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.usingAnyFreePort();

        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(appiumServiceBuilder);
        service.start();

        if (service == null || !service.isRunning()) {
            throw new RuntimeException("An appium server node is not started!");
        }

        //https://github.com/appium/appium/blob/master/docs/en/writing-running-appium/caps.md

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_API_27");
        capabilities.setCapability(MobileCapabilityType.APP, "/Users/richard/Desktop/Technical Mobile Testing/VodQA.apk");
        capabilities.setCapability(AndroidMobileCapabilityType.AVD, "Pixel_API_27");

        AndroidDriver driver = new AndroidDriver(service.getUrl(), capabilities);

        //Login
        driver.findElementByAccessibilityId("username").sendKeys("admin");
        driver.findElementByAccessibilityId("password").sendKeys("admin");
        driver.findElementByClassName("android.widget.Button").click();

        WebDriverWait waitForMenu = new WebDriverWait(driver, 5000);
        waitForMenu.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ScrollView[@content-desc=\"scrollView\"]/android.view.ViewGroup")));

        //Slider
        driver.findElementByXPath("//android.view.ViewGroup[@content-desc=\"slider1\"]/android.view.ViewGroup").click();
        WebDriverWait wait = new WebDriverWait(driver, 5000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("slider")));

        //Drag to 40%
        //Locating seekbar using resource id
        WebElement seek_bar=driver.findElementByAccessibilityId("slider");
        // get start co-ordinate of seekbar
        int start=seek_bar.getLocation().getX();
        //Get width of seekbar
        int end=seek_bar.getSize().getWidth();
        //get location of seekbar vertically
        int y=seek_bar.getLocation().getY();

        // Select till which position you want to move the seekbar
        TouchAction action=new TouchAction(driver);

        //Move it will the end
        action.press(start,y).moveTo(end,y).release().perform();

        //Move it 40%
        int moveTo=(int)(end*0.4);
        action.press(start,y).moveTo(moveTo,y).release().perform();

        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView").click();
        WebDriverWait waitForMenuAgain = new WebDriverWait(driver, 5000);
        waitForMenuAgain.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ScrollView[@content-desc=\"scrollView\"]/android.view.ViewGroup")));

        //Swipe
        driver.findElementByXPath("//android.view.ViewGroup[@content-desc=\"verticalSwipe\"]/android.view.ViewGroup").click();
        WebDriverWait waitForList = new WebDriverWait(driver, 5000);
        waitForList.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("listview")));

        new TouchAction(driver).press(515, 1525).moveTo(31, -1115).release();

        //LONG PAUSE
        //
        //
        //

        driver.quit();
    }
}
