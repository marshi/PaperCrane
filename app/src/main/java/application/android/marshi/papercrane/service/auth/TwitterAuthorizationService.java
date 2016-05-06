package application.android.marshi.papercrane.service.auth;

import application.android.marshi.papercrane.domain.usecase.auth.AuthorizationUseCase;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/16.
 */
public class TwitterAuthorizationService {

	@Inject
	AuthorizationUseCase authorizationUseCase;

	@Inject
	public TwitterAuthorizationService() {}

	public void oAuthLogin() {
		authorizationUseCase.start(null);
	}

}
