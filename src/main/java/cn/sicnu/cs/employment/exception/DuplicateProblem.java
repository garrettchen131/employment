package cn.sicnu.cs.employment.exception;

import cn.sicnu.cs.employment.common.Constants;
import org.springframework.context.MessageSource;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.Locale;

public class DuplicateProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create(Constants.PROBLEM_BASE_URI + "/duplicate");

    public DuplicateProblem(String msgCode, MessageSource messageSource, Locale locale) {
        super(
            TYPE,
            messageSource.getMessage("Exception.duplicate.title", null, locale),
            Status.CONFLICT,
            messageSource.getMessage(msgCode, null, locale));
    }
}
