package application.android.marshi.papercrane.domain.usecase.auth;

import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.TwitterAuthRepository;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/14.
 */
public class AuthorizationUseCase extends UseCase<Void, String> {

	@Inject
	TwitterAuthRepository twitterAuthRepository;

	@Inject
	public AuthorizationUseCase() {}

	@Override
	protected String call(Void param) {
		String authUrl = twitterAuthRepository.fetchAuthUrl();
//		EventBusBroker.stringEventBus.set(Event.LoginAuthorization, authUrl);
		return authUrl;
	}

}
