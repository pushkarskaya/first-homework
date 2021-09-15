import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class test {
    private Logger logger = LogManager.getLogger(test.class);
    private WebDriver wd;
    List<String> browserOptions = new ArrayList();
    String url = "https://otus.ru";

    //private WebDriver driver;
    private By catalogOfCourse = By.cssSelector("a[title='Каталог курсов']");

    public void clickButtonCatalogOfCourse() {
        wd.findElement(catalogOfCourse).click();
    }

    public void filterCourse(String keyword) {
        //Отбор всех курсов на вкладке Программирование,в названии которых есть keyword
        List<String> arrayOfCourse = new ArrayList<String>();
        int countOfCourses = wd.findElements(By.xpath("//div[contains(concat(' ',@class,' '),' lessons__new-item-title ')]")).size();
        logger.info("Количество курсов на странице всего: " + countOfCourses);
        List<WebElement> allCourses = wd.findElements(By.xpath("//div[contains(concat(' ',@class,' '),' lessons__new-item-title ')]"));
        int count = 0;
        for (int i = 0; i < allCourses.size() - 1; i++) { //logger.info(allCourses.get(i).getText()); //Записываем в лог все курсы
            arrayOfCourse.add(i, allCourses.get(i).getText());
            if (arrayOfCourse.get(i).contains(keyword)) {
                logger.info(arrayOfCourse.get(i));
                count++;
            }
        }
        logger.info("Найдено курсов, содержащих в названии текст " + keyword + ": " + count);
    }

    public void chooseCourse() throws ParseException {

        List<String> arrayofDates = new ArrayList<String>();
        List<Date> parsedDates = new ArrayList<Date>();
        int countOfAllDates = wd.findElements(By.cssSelector(".lessons__new-item-start")).size();
        List<WebElement> allDates = wd.findElements(By.cssSelector(".lessons__new-item-start"));
        for (int i = 0; i < countOfAllDates; i++) {
            if (allDates.get(i).getText().contains("2022")) {
                String newDate1 = allDates.get(i).getText().replace("С ", "");
                arrayofDates.add(i, newDate1);
            } else {
                String newDate2 = allDates.get(i).getText().replace("С ", "");
                arrayofDates.add(i, newDate2 + " 2021 года");
            }
            logger.info(arrayofDates.get(i));
            DateFormat date = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
            if ((arrayofDates.get(i).contains("В")) || (arrayofDates.get(i).contains("О дате старта будет объявлено позже"))) {
                logger.info("Курс не имеет точной даты начала");
            } else {
                String stringDate = arrayofDates.get(i);
                Date date1 = date.parse(stringDate);
                logger.info(date1.toLocaleString());
                parsedDates.add(date1);
            }
        }

        Date firstDate = parsedDates.stream().reduce(parsedDates.get(1), (date1, date2) -> date1.compareTo(date2) < 0 ? date1 : date2);
        Date lastDate = parsedDates.stream().reduce(parsedDates.get(1), (date1, date2) -> date1.compareTo(date2) < 0 ? date2 : date1);
        logger.info("Самая ранняя дата начала курсов");
        logger.info(firstDate.toLocaleString());
        logger.info("Самая поздняя дата начала курсов");
        logger.info(lastDate.toLocaleString());

        String nameOfChoosedCourseFirst = chooseCourseByDate(firstDate);
        logger.info("Выбран курс с самой ранней датой начала " + nameOfChoosedCourseFirst);
        clickButtonCatalogOfCourse();
        String nameOfChoosedCourseLast = chooseCourseByDate(lastDate);
        logger.info("Выбран курс с самой поздней датой начала " + nameOfChoosedCourseLast);

    }

    String chooseCourseByDate(Date dateOfCourse) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
        Date date = dateFormat.parse(dateOfCourse.toLocaleString());

        logger.info("Распарсили дату " + date.toLocaleString());

        SimpleDateFormat out = new SimpleDateFormat("dd MMMM");
        String formatedDate = out.format(date);

//        Date lastParsedDate = dateFormat.parse(lastDate.toLocaleString());
//        String newlastParsedDate = out.format(lastParsedDate);

        Actions builder = setActionsBuilder();
        WebElement courseFirst = wd.findElement(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + formatedDate + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]"));
        String nameOfCourse = wd.findElement(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + formatedDate + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]//descendant::div[contains(@class,'lessons__new-item-title')]")).getText();
        builder.contextClick(courseFirst).click().pause(1000).build().perform();

//*[contains(@class, 'lessons__new-item')]/*[contains(@class, 'lessons__new-item-start') and contains(normalize-space(), '31 августа')]
        //logger.info("Распарсили дату " + lastParsedDate.toLocaleString());
        return nameOfCourse;
    }

    public Actions setActionsBuilder() {
        return new Actions(wd);
    }

    @Test
    public void filterAndChooseCourse() throws InterruptedException, ParseException {
        logger.info("Старт теста");
        browserOptions.add("--start-fullscreen");
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        wd = WebDriverFactory.createNewDriver(webDriverName.CHROME, browserOptions);
        wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wd.get(url);
        Assert.assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным профессиям",
                wd.getTitle());
        clickButtonCatalogOfCourse();
        filterCourse("C#");
        chooseCourse();
    }
}