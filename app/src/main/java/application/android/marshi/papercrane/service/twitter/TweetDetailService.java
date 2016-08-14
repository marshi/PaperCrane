package application.android.marshi.papercrane.service.twitter;

import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.timeline.GetTweetDetailUseCase;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * Copyright: marshi
 */
public class TweetDetailService {

	@Inject
	GetTweetDetailUseCase getTweetDetailUseCase;

	@Inject
	public TweetDetailService() {}

	public void detail(AccessToken accessToken, Long tweetId, Action1<TweetItem> onNext, Action1<Throwable> onError) {
		GetTweetDetailUseCase.Parameter param = new GetTweetDetailUseCase.Parameter(accessToken, tweetId);
		Observable<TweetItem> observable = getTweetDetailUseCase.start(param);
		observable
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(onNext, onError);
	}

}
