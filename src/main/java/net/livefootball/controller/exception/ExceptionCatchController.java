package net.livefootball.controller.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionCatchController {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(Exception.class)
    public String exceptionCatch(Exception e, HttpServletResponse response){
        LOGGER.error(e.getMessage(),e);
        response.setStatus(500);
        return "redirect:/error";
    }
}