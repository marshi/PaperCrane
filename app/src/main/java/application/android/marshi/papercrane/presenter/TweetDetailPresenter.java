package application.android.marshi.papercrane.presenter;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;
import application.android.marshi.papercrane.activity.TweetEditorActivity;
import application.android.marshi.papercrane.databinding.ActivityTweetDetailBinding;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.enums.ExtraKeys;
import application.android.marshi.papercrane.service.ToastService;
import application.android.marshi.papercrane.service.auth.AccessTokenService;
import application.android.marshi.papercrane.service.twitter.TweetDetailService;
import application.android.marshi.papercrane.service.twitter.TwitterFavService;
import rx.functions.Action1;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * @author mukai_masaki on 2016/07/31.
 * Copyright: CYBER AGNET. INC
 */

public class TweetDetailPresenter {

	@Inject
	TweetDetailService tweetDetailService;

	@Inject
	TwitterFavService twitterFavService;

	@Inject
	AccessTokenService accessTokenService;

	@Inject
	ToastService toastService;

	@Inject
	public TweetDetailPresenter() {}

	public void applyLatestTweetItem(Activity activity, Long tweetId, ActivityTweetDetailBinding binding) {
		AccessToken accessToken = accessTokenService.getAccessToken();
		Action1<TweetItem> applyTweetItem = (TweetItem tweetItem) -> {
			binding.setTweet(tweetItem);
			binding.replyIcon.setOnClickListener(v -> {
				Intent intent = new Intent();
				intent.putExtra(ExtraKeys.USER_ID, binding.tweetDetailUserId.getText().toString());
				TweetEditorActivity.startActivity(activity, intent);
			});
			binding.retweetIcon.setOnClickListener(v -> {
				Intent intent = new Intent();
				intent.putExtra(ExtraKeys.TWEET_CONTENT, binding.tweetDetailContent.getText().toString());
				TweetEditorActivity.startActivity(activity, intent);
			});
			binding.favIcon.setOnClickListener(v -> fav(binding.getTweet(), binding.favIcon));
			binding.favIcon.setSelected(tweetItem.isFav());
		};
		tweetDetailService.detail(accessToken, tweetId, applyTweetItem, t -> {});
	}

	private void fav(TweetItem tweetItem, ImageView favImageView) {
		AccessToken accessToken = accessTokenService.getAccessToken();
		if (tweetItem.isFav()) {
			twitterFavService.removeFav(accessToken, tweetItem.getId(),
				b -> {
					if (b) {
						favImageView.setSelected(false);
						tweetItem.setFav(false);
					}
				},
				t -> {
					toastService.showToast("お気に入り解除に失敗しました", Toast.LENGTH_SHORT);
					t.printStackTrace();
				});
			return;
		}
		twitterFavService.addFav(accessToken, tweetItem.getId(),
			b -> {
				if (b) {
					favImageView.setSelected(true);
					tweetItem.setFav(true);
				}
			},
			t -> {
				toastService.showToast("お気に入りに失敗しました", Toast.LENGTH_SHORT);
				t.printStackTrace();
			}
		);
	}

}
