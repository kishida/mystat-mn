package my.stat.mn.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.View;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author naoki
 */
@Controller(value="/")
@Produces(MediaType.TEXT_HTML)
public class IndexController {
    
    @View("index")
    @Get(uri="/")
    public Map<String, String> index() {
        Map<String, String> context = new HashMap<>();
        return context;
    }
}
