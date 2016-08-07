package application.android.marshi.papercrane.presenter;

import android.widget.ImageView;
import android.widget.Toast;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.service.ToastService;
import application.android.marshi.papercrane.service.auth.AccessTokenService;
import application.android.marshi.papercrane.service.twitter.TwitterFavService;
import rx.functions.Action1;

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
		Action1<Boolean> onNext;
		Action1<Throwable> onError;
		if (tweetItem.isFav()) {
			onNext = b -> {
				if (b) { favImageView.setSelected(false); }
			};
			onError = t -> {
				toastService.showToast("お気に入り解除に失敗しました", Toast.LENGTH_SHORT);
				t.printStackTrace();
			};
			twitterFavService.removeFav(accessTokenService.getAccessToken(), tweetItem.getId(), onNext, onError);
		} else {
			onNext = b -> {
				if (b) { favImageView.setSelected(true); }
			};
			onError = t -> {
				toastService.showToast("お気に入りに失敗しました", Toast.LENGTH_SHORT);
				t.printStackTrace();
			};
			twitterFavService.addFav(accessTokenService.getAccessToken(), tweetItem.getId(), onNext, onError);
		}

	}
}
