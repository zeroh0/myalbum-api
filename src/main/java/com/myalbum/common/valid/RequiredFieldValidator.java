package com.myalbum.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class RequiredFieldValidator implements ConstraintValidator<RequiredField, String> {

    private final MessageSource messageSource;
    private String messageKey;
    private String[] params;

    public RequiredFieldValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(RequiredField constraintAnnotation) {
        this.messageKey = constraintAnnotation.message();
        this.params = constraintAnnotation.params();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            Object[] resolvedParams = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                resolvedParams[i] = messageSource.getMessage(params[i], null, params[i], LocaleContextHolder.getLocale());
            }

            String errorMessage = messageSource.getMessage(messageKey, resolvedParams, LocaleContextHolder.getLocale());

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();

            return false;
        }
        return true;
    }

}
