package cn.sicnu.cs.employment.validation;

import cn.sicnu.cs.employment.domain.vo.UserVo;
import cn.sicnu.cs.employment.validation.annotation.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserVo> {

    @Override
    public void initialize(
            final PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(final UserVo userVo, final ConstraintValidatorContext context) {
//        return userVo.getPassword().equals(userVo.getMatchingPassword());
        return true;
    }
}
