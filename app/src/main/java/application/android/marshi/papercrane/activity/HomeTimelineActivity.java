package application.android.marshi.papercrane.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.service.auth.AccessTokenService;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

/**
 * タイムラインが表示される.
 * TwitterページでのOAuth認証後コールバックで呼び出されるアクティビティでもある.
 */
public class HomeTimelineActivity extends AppCompatActivity {

	@Inject
	AccessTokenService accessTokenService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_list);
		((App)getApplication()).getApplicationComponent().inject(this);
		Intent intent = getIntent();
		String action = intent.getAction();
		if (action != null && Intent.ACTION_VIEW.equals(action)) {
			Uri uri = intent.getData();
			accessTokenService.saveAccessToken(uri);
		}

	}

}
