package application.android.marshi.papercrane.domain.usecase.auth;

import application.android.marshi.papercrane.eventbus.Event;
import application.android.marshi.papercrane.eventbus.EventBusBroker;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.TwitterAuthRepository;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/14.
 */
public class AuthorizationUseCase extends UseCase<Void>{

	@Inject
	TwitterAuthRepository twitterAuthRepository;

	@Inject
	public AuthorizationUseCase() {}

	@Override
	protected void call(Void param) {
		String authUrl = twitterAuthRepository.fetchAuthUrl();
		EventBusBroker.stringEventBus.set(Event.LoginAuthorization, authUrl);
	}

}
