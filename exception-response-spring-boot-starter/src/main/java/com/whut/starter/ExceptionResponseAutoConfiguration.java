package com.whut.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author whut2024
 * @since 2024-08-31
 */
@Configuration
@Import(GlobalExceptionHandler.class)
public class ExceptionResponseAutoConfiguration {


}
