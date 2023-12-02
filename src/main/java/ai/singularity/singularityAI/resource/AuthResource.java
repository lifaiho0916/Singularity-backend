package ai.singularity.singularityAI.resource;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;


@RestController
@RequestMapping("v1/auth")
public class AuthResource {
	private final OAuth2AuthorizedClientService clientService;

	public AuthResource(OAuth2AuthorizedClientService clientService) {
	   this.clientService = clientService;
	}
	
	@GetMapping(
			value = "/{provider}",
            produces = {"application/json"}
	)
	public void SocialLogin(@PathVariable String provider) {
		System.out.println(provider);
	}

    @GetMapping(
            value = "/{provider}/code",
            produces = {"application/json"}
    )
    public RedirectView loginSuccess(@PathVariable String provider, OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
          authenticationToken.getAuthorizedClientRegistrationId(),
          authenticationToken.getName()
        );
        
        
        String userEmail = (String) client.getPrincipalName();
        System.out.println(userEmail);
//        String provider = authenticationToken.getAuthorizedClientRegistrationId();

        // Handle storing the user information and token, then redirect to a successful login page
        // For example, store user details in your database and generate a JWT token for further authentication.

        return new RedirectView("/login-success");
      }
}
