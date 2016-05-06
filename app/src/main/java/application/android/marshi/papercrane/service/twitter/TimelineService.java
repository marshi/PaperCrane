package application.android.marshi.papercrane.service.twitter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase;
import com.trello.rxlifecycle.components.RxFragment;
import rx.android.schedulers.AndroidSchedulers;
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
	public TimelineService() {
	}

	@Inject
	GetTimelineUseCase getTimelineUseCase;

	private Action1<Throwable> onError(Activity activity) {
		return throwable -> {
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

	private void loadTweetItems(RxFragment fragment, AccessToken accessToken, Paging paging, Action1<List<TweetItem>> onNext) {
		getTimelineUseCase.start(new TimelineRequest(accessToken, paging))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.compose(fragment.bindToLifecycle())
				.subscribe(onNext, onError(fragment.getActivity()));
	}

	public void loadTweetItems(RxFragment fragment, AccessToken accessToken, Action1<List<TweetItem>> onNext) {
		Paging paging = new Paging(1);
		loadTweetItems(fragment, accessToken, paging, onNext);
	}

	public void loadTweetItems(RxFragment fragment, AccessToken accessToken, long maxId, Action1<List<TweetItem>> onNext) {
		Paging paging = new Paging().maxId(maxId - 1).count(20);
		loadTweetItems(fragment, accessToken, paging, onNext);
	}

	public void loadLatestTweetItems(RxFragment fragment, AccessToken accessToken, long sinceId, Action1<List<TweetItem>> onNext) {
		Paging paging = new Paging().sinceId(sinceId).count(20);
		loadTweetItems(fragment, accessToken, paging, onNext);
	}

}


