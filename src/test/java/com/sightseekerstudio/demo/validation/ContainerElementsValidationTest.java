/*
 * (C) Sony Network Communications Inc. All Rights reserved.
 */
package com.sightseekerstudio.demo.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * 型引数のバリデーションテスト
 *
 * @author SightSeeker
 */
@RunWith(Theories.class)
public class ContainerElementsValidationTest {

    Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void listSizeViolationTest() {
        Set<ConstraintViolation<ContainerElements>> violations = validator.validate(new ContainerElements());
        assertEquals(1, violations.size());
        ConstraintViolation<ContainerElements> violation = violations.iterator().next();
        assertEquals("strings", violation.getPropertyPath().toString());
    }

    @DataPoints
    public static Object[][] stringInListViolationTestDataPoints = {
        {"", 1},
        {"ABC", 0}
    };

    @Theory
    public void stringInListViolationTest(Object[] data) {
        ContainerElements testBean = new ContainerElements();
        testBean.strings.add((String) data[0]);

        Set<ConstraintViolation<ContainerElements>> violations = validator.validate(testBean);
        assertEquals(data[1], violations.size());
    }

    private static class ContainerElements {

        @Size(min = 1)
        private List<@NotEmpty String> strings = new ArrayList<>();

    }

}
