package net.thucydides.core.pages.integration;


import net.serenitybdd.core.webdriver.servicepools.ChromeServicePool;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.MockEnvironmentVariables;
import net.thucydides.core.webdriver.StaticTestSite;
import net.thucydides.core.webdriver.WebDriverFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

import static net.thucydides.core.webdriver.SupportedWebDriver.CHROME;

public class FluentElementAPITestsBaseClass {

    private static StaticTestSite staticTestSite;
    private static StaticSitePage chromePage;

    private static WebDriver driver;
    private static StaticSitePage staticSitePage;

    private static ChromeServicePool chromeService;

    @BeforeClass
    public static void openBrowsers() throws IOException {
        chromeService = new ChromeServicePool();
        chromeService.start();
        StepEventBus.getEventBus().clear();

        final DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        driver = chromeService.newDriver(desiredCapabilities);
        staticSitePage = new StaticSitePage(driver, 1000);
        staticSitePage.open();
    }

    @AfterClass
    public static void quitBrowsers() {
        driver.quit();
        //chromeService.shutdown();
    }

    protected WebDriver getDriver() { return driver; }

    protected StaticSitePage getPage() { return staticSitePage; }

    @BeforeClass
    public static void openStaticSite() {
        staticTestSite = new StaticTestSite();
    }

    protected static StaticTestSite getStaticTestSite() {
        return staticTestSite;
    }

    @After
    public void closeChrome() {
        if (chromePage != null) {
            chromePage.getDriver().close();
            chromePage.getDriver().quit();
            chromePage = null;
        }
    }

    @AfterClass
    public static void closeBrowsers() {
        getStaticTestSite().close();
    }

    protected void refresh(StaticSitePage page) {
        page.getDriver().navigate().refresh();
    }

    protected boolean runningOnLinux() {
        return System.getProperty("os.name").contains("Linux");
    }

}
