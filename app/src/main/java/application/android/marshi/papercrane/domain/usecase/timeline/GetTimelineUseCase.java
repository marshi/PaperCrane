package application.android.marshi.papercrane.domain.usecase.timeline;

import android.util.Log;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.eventbus.Event;
import application.android.marshi.papercrane.eventbus.EventBusBroker;
import application.android.marshi.papercrane.repository.TweetRepository;
import twitter4j.Paging;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;

/**
 * @author marshi on 2016/04/09.
 */
public class GetTimelineUseCase extends UseCase<GetTimelineUseCase.TimelineRequest> {

	@Inject
	public GetTimelineUseCase(){}

	@Inject
	TweetRepository tweetRepository;

	@Override
	protected void call(TimelineRequest request) {
		List<TweetItem> tweetItemList;
		try {
			tweetItemList = tweetRepository.getTweetItemList(request.getAccessToken(), request.getPaging());
		} catch (TwitterException e) {
			if (e.getStatusCode() == TwitterException.TOO_MANY_REQUESTS) {
				EventBusBroker.stringEventBus.set(
						Event.ShowToast,
						"リクエスト上限数に達しました。しばらく時間をあけてから再度取得してください。"
				);
			}
			return;
		} catch (Exception e) {
			Log.e("error", e.toString());
			return;
		}
		EventBusBroker.tweetListEventBus.set(Event.GetTweetList, tweetItemList);
	}

	public static class TimelineRequest {

		private AccessToken accessToken;

		private Paging paging;

		public TimelineRequest(AccessToken accessToken, Paging paging) {
			this.accessToken = accessToken;
			this.paging = paging;
		}

		public AccessToken getAccessToken() {
			return accessToken;
		}

		public Paging getPaging() {
			return paging;
		}
	}

}
