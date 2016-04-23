package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.eventbus.Event;
import application.android.marshi.papercrane.eventbus.EventBusBroker;
import application.android.marshi.papercrane.repository.TweetRepository;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;

/**
 * @author marshi on 2016/04/09.
 */
public class GetTimelineUseCase extends UseCase<AccessToken> {

	@Inject
	public GetTimelineUseCase(){}

	@Inject
	TweetRepository tweetRepository;

	@Override
	protected void call(AccessToken accessToken) {
		List<TweetItem> tweetItemList = tweetRepository.getTweetItemList(accessToken);
		EventBusBroker.tweetListEventBus.set(Event.GetTweetList, tweetItemList);
	}
}
