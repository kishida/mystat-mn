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

/**
 *
 * @author naoki
 */
@Controller("/")
@Secured(SecurityRule.IS_ANONYMOUS)
@Produces(MediaType.TEXT_PLAIN)
public class ErrorController {
    @Get("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
    
    @Get("/forbidden")
    public String forbidden() {
        return "forbidden";
    }
}
