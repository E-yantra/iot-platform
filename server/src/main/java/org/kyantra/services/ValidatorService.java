package org.kyantra.services;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidatorService {

    private static Validator validator;

    public static Validator getValidator() {
        if(validator == null) {
            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            validator = validatorFactory.getValidator();
        }
        return validator;
    }

}
