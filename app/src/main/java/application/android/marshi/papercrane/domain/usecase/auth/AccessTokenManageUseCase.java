package application.android.marshi.papercrane.domain.usecase.auth;

import android.net.Uri;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.AccessTokenRepository;
import application.android.marshi.papercrane.repository.TwitterAuthRepository;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/17.
 */
public class AccessTokenManageUseCase extends UseCase<Uri> {

	@Inject
	AccessTokenRepository accessTokenRepository;

	@Inject
	public AccessTokenManageUseCase() {}

	public AccessToken getAccessToken() {
		return accessTokenRepository.getAccessToken();
	}

	@Override
	protected void call(Uri param) {
		String oauthVerifier = param.getQueryParameter("oauth_verifier");
		TwitterAuthRepository twitterAuthRepository = new TwitterAuthRepository();
		AccessToken accessToken = twitterAuthRepository.fetchAccessToken(oauthVerifier);
		accessTokenRepository.setAccessToken(accessToken);
	}

}
