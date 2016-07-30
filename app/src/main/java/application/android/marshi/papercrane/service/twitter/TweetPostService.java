package application.android.marshi.papercrane.service.twitter;

import application.android.marshi.papercrane.domain.usecase.timeline.PostTweetUseCase;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * ツイート投稿系処理
 * @author mukai_masaki on 2016/07/24.
 * Copyright: CYBER AGNET. INC
 */
public class TweetPostService {

	@Inject
	PostTweetUseCase postTweetUseCase;

	@Inject
	public TweetPostService() {}

	public void post(AccessToken accessToken, String message, Action1<Boolean> onNext, Action1<Throwable> onError) {
		PostTweetUseCase.PostTweetRequest postTweetRequest = new PostTweetUseCase.PostTweetRequest(accessToken, message);
		Observable<Boolean> observable = postTweetUseCase.start(postTweetRequest);
		observable
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(onNext, onError);
	};

}
