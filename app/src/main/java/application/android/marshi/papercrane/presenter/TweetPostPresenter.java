package application.android.marshi.papercrane.presenter;

import android.widget.Toast;
import application.android.marshi.papercrane.fragment.TweetEditorFragment;
import application.android.marshi.papercrane.service.ToastService;
import application.android.marshi.papercrane.service.auth.AccessTokenService;
import application.android.marshi.papercrane.service.twitter.TweetPostService;

import javax.inject.Inject;

/**
 * @author mukai_masaki on 2016/07/24.
 * Copyright: CYBER AGNET. INC
 */
public class TweetPostPresenter {

	@Inject
	AccessTokenService accessTokenService;

	@Inject
	TweetPostService tweetPostService;

	@Inject
	ToastService toastService;

	@Inject
	public TweetPostPresenter() {}

	public void post(TweetEditorFragment tweetEditorFragment, String message) {

		tweetPostService.post(
				accessTokenService.getAccessToken(),
				message,
				b -> {
					if (b) {
						toastService.showToast("ツイートしました", Toast.LENGTH_SHORT);
						tweetEditorFragment.getActivity().finish();
					} else {
						toastService.showToast("ツイートに失敗しました", Toast.LENGTH_SHORT);
					}},
				Throwable::printStackTrace
		);
	}

}
