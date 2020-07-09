package ru.javawebinar.topjava.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

@RunWith(Parameterized.class)
public abstract class AbstractServiceTest {
    private static final Logger log = getLogger("result");
    private static final StringBuilder results = new StringBuilder();
    public static UserService userService;
    public static MealService mealService;
    //TODO • сводка по времени выполнения накапливается, посмотри вывод последнего тестового класса
    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };
    @Parameterized.Parameter(0)
    public String db;
    @Parameterized.Parameter(1)
    public String rep;
    private CacheManager cacheManager;
    private GenericXmlApplicationContext springContext;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = {{"hsqldb", "jdbc"}, {"hsqldb", "jpa"}, {"hsqldb", "datajpa"},
                {"postgres", "jdbc"}, {"postgres", "jpa"}, {"postgres", "datajpa"}};

        return Arrays.asList(data);
    }

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");

    }

    @Before
    public void setUp() throws Exception {
        log.info("Database: {} Repository: {}", db, rep);
        springContext = new GenericXmlApplicationContext();
        springContext.getEnvironment().setActiveProfiles(db, rep);
        springContext.load("spring/spring-app.xml", "spring/spring-db.xml");
        springContext.refresh();
        cacheManager = springContext.getBean(CacheManager.class);
        cacheManager.getCache("users").clear();
        userService = springContext.getBean(UserService.class);
        mealService = springContext.getBean(MealService.class);
    }

    @After
    public void end() throws Exception {
        springContext.close();
    }
}
