
package com.timers.timetable.config;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class TimetableExceptionAdvice implements HandlerExceptionResolver {

/*
    @ExceptionHandler
    public ModelAndView handleException ( Exception e){

        Response response = new Response(e.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message",e.getMessage());
        modelAndView.setViewName("/error");
        return modelAndView;
        //return "error";
        //return  new ResponseEntity<>(response, HttpStatus.OK);
    }
*/

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        ModelAndView modelAndView = new ModelAndView("/error");
        modelAndView.addObject("message",e.getMessage());
        modelAndView.addObject("method",o.toString());
        return modelAndView;
    }

    public class Response{

        private String message;

        public Response() {

        }

        public Response(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

