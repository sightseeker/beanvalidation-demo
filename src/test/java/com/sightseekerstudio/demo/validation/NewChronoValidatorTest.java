package com.sightseekerstudio.demo.validation;

import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Past;
import org.hibernate.beanvalidation.tck.tests.time.FixedClockProvider;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * BeanValidation 2.0 で追加された時間系のバリデーションのテスト
 *
 * @author SightSeeker
 */
@RunWith(Parameterized.class)
public class NewChronoValidatorTest {

    Validator validator;

    @Before
    public void init() {
        // ClockProvider をテスト用に固定日時を返すProviderに変更したValidatorFactoryを作成
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .clockProvider(
                        // 2018年1月1日でテストをする
                        new FixedClockProvider(
                                ZonedDateTime.of(2018, 7, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"))
                        )
                )
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    private Object testBean;
    private int expectedViolationYear;

    @Parameters
    public static List<Object[]> createTestParameters() {
        return Arrays.asList(
                new Object[][]{
                    {new PastYears(), 2018},
                    {new FutureYears(), 2018},
                    {new FutureOrPresentYears(), 2017}
                }
        );
    }

    /**
     * Set Parameters to field
     *
     * @param testBean
     * @param expectedViolationYear
     */
    public NewChronoValidatorTest(Object testBean, int expectedViolationYear) {
        this.testBean = testBean;
        this.expectedViolationYear = expectedViolationYear;
    }

    @Test
    public void PastYears_validate() {
        // バリデーション実施
        Set<ConstraintViolation<Object>> violations = validator.validate(testBean);

        // 検証 : Violations は 1件 だけ
        assertEquals(1, violations.size());

        // 検証 : Violation
        ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals(Year.of(expectedViolationYear), violation.getInvalidValue());
    }

    /**
     * Testing Bean for Past Year of Date and Time API
     */
    private static class PastYears {

        @Past
        Year _2017 = Year.of(2017);

        @Past
        Year _2016 = Year.of(2016);

        @Past
        Year _2018 = Year.of(2018);
    }

    /**
     * Testing Bean for Future Year of Date and Time API
     */
    private static class FutureYears {

        @Future
        Year _2018 = Year.of(2018);

        @Future
        Year _2019 = Year.of(2019);

        @Future
        Year _2020 = Year.of(2020);
    }

    /**
     * Testing Bean for FutureOrPresent Year of Date and Time API
     */
    private static class FutureOrPresentYears {

        @FutureOrPresent
        Year _2017 = Year.of(2017);

        @FutureOrPresent
        Year _2018 = Year.of(2018);

    }

}
