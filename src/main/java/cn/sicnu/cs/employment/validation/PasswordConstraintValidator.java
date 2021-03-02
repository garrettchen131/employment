package cn.sicnu.cs.employment.validation;

import cn.sicnu.cs.employment.validation.annotation.ValidPassword;
import lombok.val;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword,String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        val validator = new PasswordValidator(
                new LengthRule(8, 18),
                new CharacterRule(EnglishCharacterData.LowerCase, 2)
        );
        return validator.validate(new PasswordData(value)).isValid();
    }
}
