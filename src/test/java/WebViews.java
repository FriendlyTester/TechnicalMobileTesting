import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

public class WebViews
{
    public static class AndroidExample
    {
        @Test
        public void ExampleCheck() throws InterruptedException
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
            //capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_API_27");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Nexus_6_API_27");
            capabilities.setCapability(MobileCapabilityType.APP, "/Users/richard/Desktop/Technical Mobile Testing/app-alpha-debug.apk");
            capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "org.wikipedia.onboarding.InitialOnboardingActivity");
            capabilities.setCapability(AndroidMobileCapabilityType.AVD, "Nexus_6_API_27");

            AndroidDriver driver = new AndroidDriver(service.getUrl(), capabilities);


            driver.findElement(By.id("fragment_onboarding_skip_button")).click();
            WebDriverWait wait = new WebDriverWait(driver, 5000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_container")));

            driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.support.v4.view.ViewPager/android.view.ViewGroup/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.TextView"))
                    .click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_src_text")));

            driver.findElement(By.id("search_src_text")).sendKeys("Software Testing" + "\n");
            //driver.findElement(By.id("search_src_text")).sendKeys(Keys.ENTER);

            driver.getContextHandles();
            driver.context("WEBVIEW_org.wikipedia.alpha");


            driver.findElement(By.cssSelector(".mf-section-0 a")).click();

            driver.quit();
            service.stop();
        }
    }

    @Test
    public void iOSExample() throws InterruptedException
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
        capabilities.setCapability(MobileCapabilityType.APP, "/Users/richard/Desktop/Technical Mobile Testing/Wikipedia.app");
        capabilities.setCapability(IOSMobileCapabilityType.LAUNCH_TIMEOUT, 500000);

        IOSDriver driver = new IOSDriver(service.getUrl(), capabilities);


        WebDriverWait wait1 = new WebDriverWait(driver, 5000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Skip")));

        driver.findElementByAccessibilityId("Skip").click();

        driver.findElementByXPath("//XCUIElementTypeSearchField[@name=\"Search Wikipedia\"]").click();

        driver.findElement(By.xpath("//XCUIElementTypeNavigationBar[@name=\"Explore\"]/XCUIElementTypeStaticText"))
                .sendKeys("Software Testing");

        String webViewContext = "";
        Set<String> contexts = driver.getContextHandles();
        for(String context : contexts)
        {
            if(context.contains("WEB"))
            {
                webViewContext = context;
            }
        }

        driver.context(webViewContext);


        //Click Skip
        //Search for Software Testing
        //Click first result
        //Switch to WebView
        //Click first link






        driver.findElement(By.cssSelector("a")).click();

        driver.quit();
        service.stop();
    }
}
