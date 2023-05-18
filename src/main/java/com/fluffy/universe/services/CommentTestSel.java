package com.fluffy.universe.services;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CommentTestSel {
    private WebDriver driver;
    private String screenshotsDirectory;

    @BeforeEach
    public void setUp() {
        // Ініціалізація WebDriver (в даному випадку використовується ChromeDriver)
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Створення директорії для збереження скріншотів
        screenshotsDirectory = "screenshots";
        new File(screenshotsDirectory).mkdir();
    }

    @AfterEach
    public void tearDown() {
        // Закриття браузера після закінчення кожного тесту
        driver.quit();
    }

    @Test
    public void testAddCommentToPost() throws IOException {
        // Вхід в систему як зареєстрований користувач
        login("username", "password");

        // Перехід на домашню сторінку
        takeScreenshot("home-page");
        driver.get("http://localhost/home");

        // Клік на посилання зі списком блогів
        takeScreenshot("blog-listing");
        driver.findElement(By.linkText("Blog listing")).click();

        // Переконатися, що користувач перейшов на сторінку з детальною інформацією про блог/пост
        takeScreenshot("blog-details");
        Assertions.assertEquals("Blog Details", driver.getTitle());

        // Заповнення полів для коментаря
        takeScreenshot("comment-form");
        driver.findElement(By.name("name")).sendKeys("asdf");
        driver.findElement(By.name("message")).sendKeys("asdf");

        // Клік на кнопку "Add Comment"
        takeScreenshot("add-comment");
        driver.findElement(By.id("add-comment-button")).click();

        // Переконатися, що користувач повернувся на домашню сторінку
        takeScreenshot("home-page");
        Assertions.assertEquals("Home", driver.getTitle());

        // Перевірити наявність повідомлення про успішне додавання коментаря
        takeScreenshot("success-message");
        Assertions.assertEquals("Comment added to the Post successfully!",
                driver.findElement(By.id("success-message")).getText());

        // Перевірити, що коментар відображається на сторінці блогу/посту
        takeScreenshot("comment-displayed");
        Assertions.assertTrue(driver.getPageSource().contains("asdf"));
    }

    private void login(String username, String password) throws IOException {
        // Логін до системи
        takeScreenshot("login-page");
        driver.get("http://localhost/login");

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        takeScreenshot("login-submit");
        driver.findElement(By.id("login-button")).click();
    }

    private void takeScreenshot(String fileName) throws IOException {
        // Зняття скріншота і збереження його у відповідній директорії
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotPath = screenshotsDirectory + File.separator + fileName + ".png";
        FileHandler.copy(screenshot, new File(screenshotPath));

        // Копіювання скріншота у поточну робочу директорію (для зручності перегляду)
        Path currentDirectory = Path.of("").toAbsolutePath();
        Path destinationPath = Path.of(currentDirectory.toString(), fileName + ".png");
        Files.copy(Path.of(screenshotPath), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
