package com.myalbum.common.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver {

    private static MessageSource messageSource;

    @Autowired
    public MessageResolver(MessageSource messageSource) {
        MessageResolver.messageSource = messageSource;
    }

    public static String getMessage(AppError appError) {
        return messageSource.getMessage(appError.getErrorKey(), new String[]{}, LocaleContextHolder.getLocale());
    }

    public static String getMessage(AppError appError, Object[] args) {
        return messageSource.getMessage(appError.getErrorKey(), args, LocaleContextHolder.getLocale());
    }

}
