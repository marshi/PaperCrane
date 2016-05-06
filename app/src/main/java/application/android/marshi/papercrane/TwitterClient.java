package application.android.marshi.papercrane;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * @author marshi on 2016/04/23.
 */
public class TwitterClient {

	private static Twitter twitter;

	static {
		twitter  = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.SECRET_KEY);
	}

	private TwitterClient() { }

	public static Twitter getInstance(AccessToken accessToken) throws TwitterException {
		twitter.setOAuthAccessToken(accessToken);
		twitter.verifyCredentials();
		return twitter;
	}

	public static Twitter getInstance() {
		return twitter;
	}

}
