package application.android.marshi.papercrane.repository;

import application.android.marshi.papercrane.BuildConfig;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/15.
 */
public class TwitterAuthRepository {

	private static Twitter twitter;

	static {
		if (twitter == null) {
			twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.SECRET_KEY);
		}
	}

	@Inject
	public TwitterAuthRepository() {}

	public String fetchAuthUrl() {
		RequestToken oAuthRequestToken;
		try {
			oAuthRequestToken = twitter.getOAuthRequestToken("papercrane://LoginCallbackActivity");
			return oAuthRequestToken.getAuthorizationURL();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return "";
	}

	public AccessToken fetchAccessToken(String oauthVerifier) {
		try {
			return twitter.getOAuthAccessToken(oauthVerifier);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}

}