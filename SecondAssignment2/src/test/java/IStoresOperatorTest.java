import edu.um.cps3230.IStoresOperator;
import enums.IStoresOperatorStates;
import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileNotFoundException;
import java.util.Random;

public class IStoresOperatorTest implements FsmModel {

    WebDriver driver;
    IStoresOperator sut;
    IStoresOperatorStates state;

    private boolean fromCategory, fromProduct;


    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void reset(final boolean b) {

        if (driver != null) driver.quit();

        if (b) {
            System.setProperty("webdriver.chrome.driver", "/Users/jenik/Downloads/webtesting/chromedriver");
            driver = new ChromeDriver();
            sut = new IStoresOperator(driver);
        }

        sut.initializeWelcomePage();
        state = IStoresOperatorStates.WELCOME_PAGE;
        fromCategory = false;
        fromProduct = false;
        Assertions.assertEquals(driver.getTitle(), "iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod");
    }

    public boolean goToCategoryGuard() {
        return getState().equals(IStoresOperatorStates.WELCOME_PAGE);
    }
    public @Action void goToCategory() {
        sut.goToCategory();
        state = IStoresOperatorStates.CATEGORY;
        Assertions.assertEquals(driver.getTitle(), "iPhone | iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod");

    }

    public boolean goToWelcomeGuard() {
        return getState().equals(IStoresOperatorStates.CATEGORY) || getState().equals(IStoresOperatorStates.PRODUCT) ||
                getState().equals(IStoresOperatorStates.CART);
    }
    public @Action void goToWelcome() {
        sut.goToWelcome();
        state = IStoresOperatorStates.WELCOME_PAGE;
        Assertions.assertEquals(driver.getTitle(), "iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod");
    }

    public boolean selectProductGuard() {
        return getState().equals(IStoresOperatorStates.CATEGORY);
    }
    public @Action void selectProduct() {
        sut.selectProduct();
        state = IStoresOperatorStates.PRODUCT;
        Assertions.assertEquals(sut.returnProductText() + " | iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod", driver.getTitle());

    }

    public boolean addToCartGuard() {
        return getState().equals(IStoresOperatorStates.PRODUCT);
    }
    public @Action void addToCart(){
        fromProduct = true;
        sut.addToCart();
        state = IStoresOperatorStates.CART_POPUP;
        Assertions.assertEquals(sut.returnProductText() + " | iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod", driver.getTitle());
    }

    public boolean addToCartFromCategoryGuard() {
        return getState().equals(IStoresOperatorStates.CATEGORY);
    }
    public @Action void addToCartFromCategory(){
        fromCategory = true;
        sut.addToCartFromCategory();
        state = IStoresOperatorStates.CART_POPUP;
        Assertions.assertEquals(driver.getTitle(), "iPhone | iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod");
    }

    public boolean stayOnSiteGuard() {
        return getState().equals(IStoresOperatorStates.CART_POPUP);
    }
    public @Action void stayOnSite() throws InterruptedException {
        sut.stayOnSite();
        String title = driver.getTitle();
        Assertions.assertEquals(fromCategory,title.equals("iPhone | iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod"));
        Assertions.assertEquals(fromProduct,title.equals(sut.returnProductText() + " | iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod"));
        if (fromProduct){
            state = IStoresOperatorStates.PRODUCT;
            fromProduct = false;
        }if(fromCategory){
            state = IStoresOperatorStates.CATEGORY;
            fromCategory = false;
        }
    }

    public boolean goToCartGuard() {
        return getState().equals(IStoresOperatorStates.CART_POPUP);
    }
    public @Action void goToCart() throws InterruptedException {
        sut.goToCart();
        state = IStoresOperatorStates.CART;
        fromProduct = false;
        fromCategory = false;
        Assertions.assertEquals(driver.getTitle(), "Nákupní košík | iStores - Apple Premium Reseller - iPhone, iPad, Mac, iPod");
    }


    @Test
    public void IStoresSystemModelRunner(){
        final Tester tester = new GreedyTester(new IStoresOperatorTest());
        tester.setRandom(new Random());
        tester.addListener(new StopOnFailureListener());
        tester.addListener("verbose");
        tester.addCoverageMetric(new TransitionPairCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());
        tester.generate(50);
        tester.printCoverage();
    }
}
