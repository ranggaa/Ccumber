package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
public static WebDriver driver;
public static Properties conpro;
//method for launch browser
public static WebDriver startBrowser()throws Throwable
{
	conpro = new Properties();
	//load property file here
	conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));
	if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
	{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
	{
		driver = new FirefoxDriver();
	}
	else
	{
		Reporter.log("Browser value is not matching",true);
	}
	return driver;
}
//method for launching url
public static void openUrl()
{
	driver.get(conpro.getProperty("Url"));
}
//method for to wait for any webelement in a page
public static void waitForElement(String LocatorType,String LocatorValue,String TestData)
{
	WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
	if(LocatorType.equalsIgnoreCase("id"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
	}
}
//method for any textbox
public static void typeAction(String LocatorType,String LocatorValue,String testData)
{
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).clear();
		driver.findElement(By.id(LocatorValue)).sendKeys(testData);
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).clear();
		driver.findElement(By.xpath(LocatorValue)).sendKeys(testData);
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).clear();
		driver.findElement(By.name(LocatorValue)).sendKeys(testData);
	}
}
//method for any buttons,checkboxes,radiobuttons,links and images
public static void clickAction(String LocatorType,String LocatorValue)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
	}
}
//method for validate title
public static void validateTitle(String Expected_Title)
{
	String Actual_Title = driver.getTitle();
	try {
	Assert.assertEquals(Actual_Title, Expected_Title,"Title is Not Matching");
	}catch(AssertionError a)
	{
		System.out.println(a.getMessage());
	}
}
//method for closing 
public static void closeBrowser()
{
	driver.quit();
}
//method for genereate data format
public static String generateDate()
{
	Date date = new Date();
	DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh_mm_ss");
	return df.format(date);
}
//method for listboxes
public static void dropDownAction(String LocatorType,String LocatorValue,String testData)
{
	if(LocatorType.equalsIgnoreCase("id"))
	{
		//convert testdata string type into integer
		int value =Integer.parseInt(testData);
		Select element = new Select(driver.findElement(By.id(LocatorValue)));
		element.selectByIndex(value);
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		//convert testdata string type into integer
		int value =Integer.parseInt(testData);
		Select element = new Select(driver.findElement(By.name(LocatorValue)));
		element.selectByIndex(value);
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		//convert testdata string type into integer
		int value =Integer.parseInt(testData);
		Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
		element.selectByIndex(value);
	}
}
//method for capturing stock number into notepad
public static void captuserStock(String LocatorType,String LocatorValue)throws Throwable
{
	String StockNum ="";
	if(LocatorType.equalsIgnoreCase("name"))
	{
		StockNum =driver.findElement(By.name(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		StockNum =driver.findElement(By.id(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		StockNum =driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		
	}
	//create notepad
	FileWriter fw = new FileWriter("./CaptureData/stockNumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(StockNum);
	bw.flush();
	bw.close();
}
//method validate stock number in table
public static void stockTable()throws Throwable
{
	//read stock number from notepad
	FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data =br.readLine();
	//if search textbox already displayed dont click search panel button
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		//if search textbox not displayed click search p[anel button
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	//clear text in search textbox
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	//enter sctock number into search textbox
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
	Reporter.log(Act_Data+"        "+Exp_Data,true);
	try {
	Assert.assertEquals(Act_Data, Exp_Data,"Stock number Should not Match");
	}catch(AssertionError a)
	{
		Reporter.log(a.getMessage(),true);
	}
}
//method for supplier number to capture into notepad
public static void captureSup(String LocatorType,String LocatorValue)throws Throwable 
{
	String SupplierNum ="";
	if(LocatorType.equalsIgnoreCase("name"))
	{
		SupplierNum =driver.findElement(By.name(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		SupplierNum =driver.findElement(By.id(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		SupplierNum =driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		
	}
	//create notepad
	FileWriter fw = new FileWriter("./CaptureData/supplierNumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(SupplierNum);
	bw.flush();
	bw.close();
}
//method for supplier table
public static void supplierTable()throws Throwable
{
	//read stock number from notepad
		FileReader fr = new FileReader("./CaptureData/supplierNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data =br.readLine();
		//if search textbox already displayed dont click search panel button
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			//if search textbox not displayed click search p[anel button
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		//clear text in search textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		//enter sctock number into search textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(Act_Data+"        "+Exp_Data,true);
		try {
		Assert.assertEquals(Act_Data, Exp_Data,"Stock number Should not Match");
		}catch(AssertionError a)
		{
			Reporter.log(a.getMessage(),true);
		}
}
//method for capture customer number into notepad
public static void captureCus(String LocatorType,String LocatorValue)throws Throwable
{
	String CustomerNum ="";
	if(LocatorType.equalsIgnoreCase("name"))
	{
		CustomerNum =driver.findElement(By.name(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		CustomerNum =driver.findElement(By.id(LocatorValue)).getAttribute("value");
		
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		CustomerNum =driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		
	}
	//create notepad
	FileWriter fw = new FileWriter("./CaptureData/CustomerNumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(CustomerNum);
	bw.flush();
	bw.close();
}
//verify customer number in customer table
public static void customerTable()throws Throwable
{
	//read stock number from notepad
			FileReader fr = new FileReader("./CaptureData/CustomerNumber.txt");
			BufferedReader br = new BufferedReader(fr);
			String Exp_Data =br.readLine();
			//if search textbox already displayed dont click search panel button
			if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
				//if search textbox not displayed click search p[anel button
				driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
			//clear text in search textbox
			driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
			//enter sctock number into search textbox
			driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
			driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
			Thread.sleep(3000);
			String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
			Reporter.log(Act_Data+"        "+Exp_Data,true);
			try {
			Assert.assertEquals(Act_Data, Exp_Data,"Stock number Should not Match");
			}catch(AssertionError a)
			{
				Reporter.log(a.getMessage(),true);
			}
}
}















