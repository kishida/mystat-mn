/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.stat.mn.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.security.Principal;
import java.util.Optional;
import javax.annotation.Nullable;
/**
 *
 * @author naoki
 */
@Controller("/")
@Secured(SecurityRule.IS_ANONYMOUS)
@Produces(MediaType.TEXT_PLAIN)
public class ErrorController {
    @Get("/unauthorized")
    public String unauthorized(@Nullable Principal principal) {
        var popt = Optional.ofNullable(principal);
        return "unauthorized " + popt.map(p -> p.getName()).orElse("no user");
    }
    
    @Get("/forbidden")
    public String forbidden() {
        return "forbidden";
    }
}
