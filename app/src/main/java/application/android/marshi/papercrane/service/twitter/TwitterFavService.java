package application.android.marshi.papercrane.service.twitter;

import application.android.marshi.papercrane.domain.usecase.reaction.AddFavUseCase;
import application.android.marshi.papercrane.domain.usecase.reaction.RemoveFavUseCase;
import application.android.marshi.papercrane.repository.store.TweetStoreRepository;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * Copyright: CYBER AGNET. INC
 */

public class TwitterFavService {

	@Inject
	AddFavUseCase addFavUseCase;

	@Inject
	RemoveFavUseCase removeFavUseCase;

	@Inject
	TweetStoreRepository tweetStoreRepository;

	@Inject
	public TwitterFavService() {}

	public void addFav(AccessToken accessToken, Long tweetId, Action1<Boolean> onNext, Action1<Throwable> onError) {
		AddFavUseCase.AddFavRequest param = new AddFavUseCase.AddFavRequest(accessToken, tweetId);
		Observable<Boolean> observable = addFavUseCase.start(param);
		observable
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(onNext, onError);
	}


	public void removeFav(AccessToken accessToken, Long tweetId, Action1<Boolean> onNext, Action1<Throwable> onError) {
		RemoveFavUseCase.RemoveFavParameter param = new RemoveFavUseCase.RemoveFavParameter(accessToken, tweetId);
		Observable<Boolean> observable = removeFavUseCase.start(param);
		observable
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(onNext, onError);
	}

}
