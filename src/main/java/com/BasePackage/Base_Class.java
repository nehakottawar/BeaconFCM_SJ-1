package com.BasePackage;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Page_Repositary.LoginPageRepo;
import com.Utility.Log;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Base_Class {

	public static RemoteWebDriver driver = null;
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public static String Pagetitle;

	public static Properties configloader() throws IOException {
		FileInputStream File = new FileInputStream(".\\src\\test\\resources\\config.properties");
		Properties properties = new Properties();
		properties.load(File);
		return properties;
	}

	public void SetUp() throws IOException, InterruptedException {
		
		String Browser = configloader().getProperty("Browser");
		String Url = configloader().getProperty("URL");
		
		switch (Browser.toUpperCase()) {

		case "CHROME":
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			break;
		case "FIREFOX":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();			
			break;
		default:
			System.err.println("The Driver is not defined");
		}
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		Log.info("Driver has initialized successfully for "+Browser+" browser");
		driver.get(Url);
        Common.setDriver(driver);
        Common.fluentWait("LoginHyperlink2Banner",LoginPageRepo.LoginHyperlink2Banner);
        
		Thread.sleep(9000);
		Pagetitle = driver.getTitle();
		Log.info("Title is displayed : "+Pagetitle);
	}

	public static Connection OracleDBConnection() throws IOException {
		
		Connection connection = null;
        try {
        	String DB_URL = configloader().getProperty("DatabaseURL");
        	String DB_UserName = configloader().getProperty("DB_UserName");
        	String DB_Password = configloader().getProperty("DB_Password");
        	
            // JDBC URL for Oracle database
            String URL = "jdbc:oracle:thin:@"+ DB_URL.trim();
            String username = DB_UserName.trim();
            String password = DB_Password.trim();
            // Establish connection
            System.out.println("URL="+URL);
            System.out.println("username="+username);
            System.out.println("password="+password);
            connection = DriverManager.getConnection(URL, username, password);
            
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        } 
		return connection;
		
	}
	
	public static  void click(By element) throws InterruptedException {

		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Updated constructor
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		Thread.sleep(2000);

	}
	
	public static void ForLoopClick(By ClickElement) {
		try
		{
			for(int i=0; i<60; i++)
			{
				try
				{
					WebElement element = driver.findElement(ClickElement);
					if (element.isDisplayed() == true)
					{
						element.click();
						element.click();
						System.out.println("ForLoopWaitPlusClick: Element clicked");
						break;
					}
					else
					{
						System.out.println("Element to be click is not found");
					}
				}
				catch(Exception e1)
				{
					System.out.println("Catch exception");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Error occurred: " + e);
		}
		
	}
	
}
