package application.android.marshi.papercrane.service.twitter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.timeline.GetStoredTimelineUseCase;
import application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.repository.TweetStoreRepository;
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
	TweetStoreRepository tweetStoreRepository;

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
					return;
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
		TweetPage type,
		Action1<List<TweetItem>> onNext,
		Action0 onError
	) {
		Observable<List<TweetItem>> listObservable = getTimelineUseCase
			.start(new TimelineRequest(accessToken, paging, type))
			.observeOn(AndroidSchedulers.mainThread())
			.compose(fragment.bindToLifecycle());
		listObservable
			.subscribeOn(Schedulers.computation())
			.subscribe(onNext, onError(fragment.getActivity(), onError));
		listObservable
			.subscribeOn(Schedulers.computation())
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
						TweetPage.HomeTimeline.name()
					));
				}
				return tweetList;
			})
			.subscribeOn(Schedulers.io())
			.subscribe(tweet -> tweetStoreRepository.insertTweetList(tweet));
	}

	public void loadTweetItems(RxFragment fragment, AccessToken accessToken, TweetPage type, Action1<List<TweetItem>> onNext) {
		Paging paging = new Paging(1);
		loadTweetItems(fragment, accessToken, paging, type, onNext, null);
	}

	public void loadTweetItems(RxFragment fragment, AccessToken accessToken, long maxId, TweetPage type, Action1<List<TweetItem>> onNext) {
		Paging paging = new Paging().maxId(maxId - 1).count(20);
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
			paging = new Paging().sinceId(sinceId).count(20);
		} else {
			paging = new Paging().count(20);
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
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(onNext);
	}

}


