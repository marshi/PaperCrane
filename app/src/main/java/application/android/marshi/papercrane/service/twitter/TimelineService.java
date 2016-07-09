package application.android.marshi.papercrane.service.twitter;

import android.util.Log;
import android.widget.Toast;
import application.android.marshi.papercrane.database.dto.ReadMore;
import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.timeline.GetStoredTimelineUseCase;
import application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.enums.TweetSettingValues;
import application.android.marshi.papercrane.enums.ViewType;
import application.android.marshi.papercrane.repository.cache.ReadMoreCacheRepository;
import application.android.marshi.papercrane.repository.cache.TweetCacheRepository;
import application.android.marshi.papercrane.service.ToastService;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.trello.rxlifecycle.components.support.RxFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import twitter4j.Paging;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;

import static application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase.TimelineRequest;

/**
 * @author marshi on 2016/04/23.
 */
public class TimelineService {

	@Inject
	public TimelineService() {}

	@Inject
	GetTimelineUseCase getTimelineUseCase;

	@Inject
	GetStoredTimelineUseCase getStoredTimelineUseCase;

	@Inject
	TweetCacheRepository tweetCacheRepository;

	@Inject
	ReadMoreCacheRepository readMoreCacheRepository;

	@Inject
	ToastService toastService;

	/**
	 *
	 * @param onError
	 * @return
	 */
	private Action1<Throwable> onErrorWithToast(Action0 onError) {
		return throwable -> {
			if (onError != null) {
				onError.call();
			}
			if (throwable instanceof TwitterException) {
				TwitterException e = (TwitterException) throwable;
				if (e.getStatusCode() == TwitterException.TOO_MANY_REQUESTS) {
					toastService.showToast("リクエスト上限数に達しました。しばらく時間をあけてから再度取得してください。", Toast.LENGTH_LONG);
				}
				Log.e("", "error", throwable);
			} else {
				Log.e("", "error", throwable);
			}
		};
	}

	private void loadTweetItems(
		RxFragment fragment,
		AccessToken accessToken,
		Paging paging,
		TweetPage tweetPage,
		Long latestTweetId,
		Action1<List<TweetItem>> onNext,
		Action0 onError
	) {
		Observable<List<TweetItem>> timelineObservable = getTimelineUseCase
			.start(new TimelineRequest(accessToken, paging, tweetPage, latestTweetId))
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(fragment.bindToLifecycle()).share();

		//onNextの処理
		timelineObservable
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(onNext, onErrorWithToast(onError));

		//DBへ保存
		timelineObservable
			.observeOn(Schedulers.computation())
			.map(tweetItemList -> Stream.of(tweetItemList)
				.filter(TweetItem.viewTypePredicate(ViewType.Normal))
				.map(t -> new Tweet(
					t.getId(),
					t.getUserId(),
					t.getUserName(),
					t.getContent(),
					t.getProfileImageUrl(),
					t.getTweetAt(),
					tweetPage.name()
				)).collect(Collectors.toList()))
			.observeOn(Schedulers.io())
			.subscribe(tweetList -> {
				tweetCacheRepository.set(tweetList, tweetPage);
				if (tweetList.size() == TweetSettingValues.TWEET_LOAD_SIZE) {
					Tweet oldTweetItem = tweetList.get(tweetList.size() - 1);
					readMoreCacheRepository.set(new ReadMore(oldTweetItem.getTweetId()));
				}
			}, e -> Log.e("error", "", e));
	}

	/**
	 *
	 * @param fragment
	 * @param accessToken
	 * @param maxId
	 * @param type
	 * @param onNext
	 */
	public void loadTweetItems(RxFragment fragment, AccessToken accessToken, long maxId, TweetPage type, Action1<List<TweetItem>> onNext) {
		Paging paging = new Paging().maxId(maxId - 1).count(TweetSettingValues.TWEET_LOAD_SIZE);
		loadTweetItems(fragment, accessToken, paging, type, null, onNext, null);
	}

	/**
	 *
	 * @param fragment
	 * @param accessToken
	 * @param maxId
	 * @param sinceId
	 * @param type
	 * @param onNext
	 */
	public void loadTweetItems(RxFragment fragment, AccessToken accessToken, long maxId, long sinceId, Long latestTweetId, TweetPage type, Action1<List<TweetItem>> onNext) {
		Paging paging = new Paging().maxId(maxId - 1).sinceId(sinceId).count(TweetSettingValues.TWEET_LOAD_SIZE);
		loadTweetItems(fragment, accessToken, paging, type, latestTweetId, onNext, null);
	}

	/**
	 *
	 * @param fragment
	 * @param accessToken
	 * @param sinceId
	 * @param tweetPage
	 * @param onNext
	 * @param onError
	 */
	public void loadLatestTweetItems(
		RxFragment fragment,
		AccessToken accessToken,
		Long sinceId,
		Long latestTweetId,
		TweetPage tweetPage,
		Action1<List<TweetItem>> onNext,
		Action0 onError
	) {
		Paging paging;
		if (sinceId != null) {
			paging = new Paging().sinceId(sinceId).count(TweetSettingValues.TWEET_LOAD_SIZE);
		} else {
			paging = new Paging().count(TweetSettingValues.TWEET_LOAD_SIZE);
		}
		loadTweetItems(fragment, accessToken, paging, tweetPage, latestTweetId, onNext, onError);
	}

	/**
	 *
	 * @param fragment
	 * @param tweetPage
	 * @param onNext
	 */
	public void loadStoredTweets(RxFragment fragment, TweetPage tweetPage, Action1<List<TweetItem>> onNext) {
		getStoredTimelineUseCase.start(tweetPage)
			.compose(fragment.bindToLifecycle())
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(onNext);
	}

	public void deleteStoredReadMore(Long id) {
		Observable
			.create((Observable.OnSubscribe<Long>) subscriber -> readMoreCacheRepository.delete(id))
			.subscribeOn(Schedulers.io())
			.subscribe();
	}

}


