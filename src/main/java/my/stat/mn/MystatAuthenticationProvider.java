package my.stat.mn;

import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Flowable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import my.stat.mn.service.UserService;
import org.reactivestreams.Publisher;

/**
 *
 * @author naoki
 */
@Singleton
public class MystatAuthenticationProvider implements AuthenticationProvider {

    @Inject
    UserService userService;
    
    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        String handle = request.getIdentity().toString();
        return userService.findByHandle(handle)
                .map(user -> {
                    return Flowable.<AuthenticationResponse>just(
                            new UserDetails(handle, List.of("user")));
                })
                .orElseGet(() -> Flowable.just(new AuthenticationFailed()));
    }
    
}
