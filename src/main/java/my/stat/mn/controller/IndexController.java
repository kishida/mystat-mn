package my.stat.mn.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 *
 * @author naoki
 */
@Controller(value="/")
@Produces(MediaType.TEXT_HTML)
@Secured(SecurityRule.IS_ANONYMOUS)
public class IndexController {
    
    @Get(uri="/")
    public ModelAndView index(@Nullable Principal principal) { // Optinalは使えない
        if (principal == null) {
            return new ModelAndView("index", null);
        }
        Map<String, String> context = new HashMap<>();
        context.put("name", principal.getName());
        return new ModelAndView("timeline", context);
    }
}
