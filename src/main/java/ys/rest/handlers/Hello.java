package ys.rest.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    private final static Logger logger = LoggerFactory.getLogger(Hello.class);

    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public Response sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        logger.debug("got request for /sayHello with parameter name={}", name);
        return new Response("Hello, " + name + "!");
    }

    public static class Response {
        private String message;

        public Response() {
        }

        Response(String message) {
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
