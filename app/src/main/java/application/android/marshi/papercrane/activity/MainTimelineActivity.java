package application.android.marshi.papercrane.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.presenter.auth.AccessTokenPresenter;
import application.android.marshi.papercrane.presenter.twitter.TimelinePresenter;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * タイムラインが表示される.
 * TwitterページでのOAuth認証後コールバックで呼び出されるアクティビティでもある.
 */
public class MainTimelineActivity extends AppCompatActivity {

	@Inject
	AccessTokenPresenter accessTokenPresenter;

	@Inject
	TimelinePresenter timelinePresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_callback);
		((App)getApplication()).getApplicationComponent().inject(this);
		Intent intent = getIntent();
		String action = intent.getAction();
		if (Intent.ACTION_VIEW.equals(action)) {
			Uri uri = intent.getData();
			accessTokenPresenter.saveAccessToken(uri);
		}
		AccessToken accessToken = accessTokenPresenter.getAccessToken();
		timelinePresenter.getTweetItems(accessToken);
//		EventBusBroker.tweetListEventBus.get(Event.GetTweetList).subscribe(tweetItems ->
//
//		);
	}

}
