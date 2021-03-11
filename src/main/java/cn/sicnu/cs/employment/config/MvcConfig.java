package cn.sicnu.cs.employment.config;

import cn.sicnu.cs.employment.validation.PassayPropertiesMessageResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.passay.MessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
@RequiredArgsConstructor
public class MvcConfig {

    private final MessageSource messageSource;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModules(
                new ProblemModule(),
                new ConstraintViolationProblemModule());
    }

    /**
     * 配置自定义的 Passay 消息解析器
     *
     * @return MessageResolver
     */
    @Bean
    public MessageResolver messageResolver() {
        return new PassayPropertiesMessageResolver(messageSource);
    }

    /**
     * 配置 Java Validation 使用国际化的消息资源
     *
     * @return LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
