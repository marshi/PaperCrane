package application.android.marshi.papercrane.service.twitter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import application.android.marshi.papercrane.database.Cache;
import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.timeline.GetStoredTimelineUseCase;
import application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase;
import application.android.marshi.papercrane.enums.TweetPage;
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
import java.util.ArrayList;
import java.util.List;

import static application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase.TimelineRequest;

/**
 * @author marshi on 2016/04/23.
 */
public class TimelineService {

	@Inject
	public TimelineService() {
	}

	@Inject
	GetTimelineUseCase getTimelineUseCase;

	@Inject
	GetStoredTimelineUseCase getStoredTimelineUseCase;

	@Inject
	Cache cache;

	/**
	 *
	 * @param activity
	 * @param onError
	 * @return
	 */
	private Action1<Throwable> onError(Activity activity, Action0 onError) {
		return throwable -> {
			if (onError != null) {
				onError.call();
			}
			if (throwable instanceof TwitterException) {
				TwitterException e = (TwitterException) throwable;
				if (e.getStatusCode() == TwitterException.TOO_MANY_REQUESTS) {
					Toast.makeText(
						activity,
						"リクエスト上限数に達しました。しばらく時間をあけてから再度取得してください。",
						Toast.LENGTH_LONG
					).show();
				}
				Log.e("", "error", throwable);
			} else {
				Log.e("", "error", throwable);
			}
		};
	}

	private Action0 onCompleted() {
		return () -> {

		};
	}

	private void loadTweetItems(
		RxFragment fragment,
		AccessToken accessToken,
		Paging paging,
		TweetPage pageType,
		Action1<List<TweetItem>> onNext,
		Action0 onError
	) {
		Observable<List<TweetItem>> timelineObservable = getTimelineUseCase
			.start(new TimelineRequest(accessToken, paging, pageType))
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.compose(fragment.bindToLifecycle());

		//onNextの処理
		timelineObservable
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(onNext, onError(fragment.getActivity(), onError));

		//DBへ保存
		timelineObservable
			.observeOn(Schedulers.computation())
			.map(tweetItemList -> {
				List<Tweet> tweetList = new ArrayList<>();
				for (TweetItem t : tweetItemList) {
					tweetList.add(new Tweet(
						t.getId(),
						t.getUserId(),
						t.getUserName(),
						t.getContent(),
						t.getProfileImageUrl(),
						t.getTweetAt(),
						pageType.name()
					));
				}
				return tweetList;
			})
			.observeOn(Schedulers.io())
			.subscribe(tweet -> cache.set(tweet, pageType), e -> {Log.e("error", "", e);});
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
		Paging paging = new Paging().maxId(maxId - 1).count(200);
		loadTweetItems(fragment, accessToken, paging, type, onNext, null);
	}

	/**
	 *
	 * @param fragment
	 * @param accessToken
	 * @param sinceId
	 * @param type
	 * @param onNext
	 * @param onError
	 */
	public void loadLatestTweetItems(RxFragment fragment, AccessToken accessToken, Long sinceId, TweetPage type, Action1<List<TweetItem>> onNext, Action0 onError) {
		Paging paging;
		if (sinceId != null) {
			paging = new Paging().sinceId(sinceId).count(200);
		} else {
			paging = new Paging().count(200);
		}
		loadTweetItems(fragment, accessToken, paging, type, onNext, onError);
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

}


