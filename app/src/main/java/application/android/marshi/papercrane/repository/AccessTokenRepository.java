package application.android.marshi.papercrane.repository;

import android.content.Context;
import android.content.SharedPreferences;
import twitter4j.auth.AccessToken;

/**
 * @author marshi on 2016/04/17.
 */
public class AccessTokenRepository {

	private Context context;

	public AccessTokenRepository(Context context) {
		this.context = context;
	}

	public AccessToken getAccessToken() {
		SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
		String accessToken = preferences.getString("accessToken", "");
		String accessTokenSecret = preferences.getString("accessTokenSecret", "");
		return new AccessToken(accessToken, accessTokenSecret);
	}

	public void setAccessToken(AccessToken accessToken) {
		SharedPreferences.Editor editor = context.getSharedPreferences("token", Context.MODE_PRIVATE).edit();
		editor.putString("accessToken", accessToken.getToken());
		editor.putString("accessTokenSecret", accessToken.getTokenSecret());
		editor.apply();
	}

}
