package application.android.marshi.papercrane.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.adapter.TweetPagerAdapter;
import application.android.marshi.papercrane.databinding.ActivityTweetListBinding;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.service.auth.AccessTokenService;

import javax.inject.Inject;

/**
 * タイムラインが表示される.
 * TwitterページでのOAuth認証後コールバックで呼び出されるアクティビティでもある.
 */
public class TimelineActivity extends AppCompatActivity {

	@Inject
	AccessTokenService accessTokenService;

	private ActivityTweetListBinding activityTweetListBinding;


	public static void startActivity(Context context) {
		Intent intent = new Intent(context, TimelineActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityTweetListBinding = DataBindingUtil.setContentView(this, R.layout.activity_tweet_list);
		App.getApplicationComponent().inject(this);
		Intent intent = getIntent();
		String action = intent.getAction();
		if (action != null && Intent.ACTION_VIEW.equals(action)) {
			Uri uri = intent.getData();
			accessTokenService.saveAccessToken(uri);
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentPagerAdapter fragmentPagerAdapter = new TweetPagerAdapter(fragmentManager);
		activityTweetListBinding.viewPager.setAdapter(fragmentPagerAdapter);

		activityTweetListBinding.setOnClickFab(v -> {
			Intent tweetEditorActivityIntent = new Intent(this, TweetEditorActivity.class);
			startActivity(tweetEditorActivityIntent);
		});

	}

}
