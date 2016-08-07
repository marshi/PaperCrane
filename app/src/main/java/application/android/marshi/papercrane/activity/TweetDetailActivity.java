package application.android.marshi.papercrane.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import application.android.marshi.papercrane.R;
import application.android.marshi.papercrane.databinding.ActivityTweetDetailBinding;
import application.android.marshi.papercrane.di.App;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.enums.ExtraKeys;
import application.android.marshi.papercrane.presenter.TweetDetailPresenter;

import javax.inject.Inject;

public class TweetDetailActivity extends AppCompatActivity {

	private ActivityTweetDetailBinding binding;

	@Inject
	TweetDetailPresenter tweetDetailPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getApplicationComponent().inject(this);
		this.binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet_detail);
		TweetItem tweetItem = getIntent().getExtras().getParcelable(TweetItem.class.getName());
		binding.setTweet(tweetItem);
		binding.replyIcon.setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.putExtra(ExtraKeys.USER_ID, binding.tweetDetailUserId.getText().toString());
			TweetEditorActivity.startActivity(this, intent);
		});
		binding.retweetIcon.setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.putExtra(ExtraKeys.TWEET_CONTENT, binding.tweetDetailContent.getText().toString());
			TweetEditorActivity.startActivity(this, intent);
		});
		binding.favIcon.setOnClickListener(v ->
			tweetDetailPresenter.fav(binding.getTweet(), binding.favIcon));
		}

}
