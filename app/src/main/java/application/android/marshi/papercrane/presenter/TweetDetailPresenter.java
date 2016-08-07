package application.android.marshi.papercrane.presenter;

import android.widget.ImageView;
import android.widget.Toast;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.service.ToastService;
import application.android.marshi.papercrane.service.auth.AccessTokenService;
import application.android.marshi.papercrane.service.twitter.TwitterFavService;

import javax.inject.Inject;

/**
 * @author mukai_masaki on 2016/07/31.
 * Copyright: CYBER AGNET. INC
 */

public class TweetDetailPresenter {

	@Inject
	TwitterFavService twitterFavService;

	@Inject
	AccessTokenService accessTokenService;

	@Inject
	ToastService toastService;

	@Inject
	public TweetDetailPresenter() {}

	public void fav(TweetItem tweetItem, ImageView favImageView) {
		if (tweetItem.isFav()) {
			twitterFavService.removeFav(accessTokenService.getAccessToken(), tweetItem.getId(),
				b -> {if (b) { favImageView.setSelected(false); }},
				t -> {
					toastService.showToast("お気に入り解除に失敗しました", Toast.LENGTH_SHORT);
					t.printStackTrace();
				});
			return;
		}
		twitterFavService.addFav(accessTokenService.getAccessToken(), tweetItem.getId(),
			b -> { if (b) { favImageView.setSelected(true); }},
			t -> {
				toastService.showToast("お気に入りに失敗しました", Toast.LENGTH_SHORT);
				t.printStackTrace();
			}
		);
	}
}
