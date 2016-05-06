package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.TweetRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import twitter4j.Paging;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;

/**
 * @author marshi on 2016/04/09.
 */
public class GetTimelineUseCase extends UseCase<GetTimelineUseCase.TimelineRequest, List<TweetItem>> {

	@Inject
	public GetTimelineUseCase(){}

	@Inject
	TweetRepository tweetRepository;

	protected List<TweetItem> call(TimelineRequest request) throws TwitterException {
		List<TweetItem> tweetItemList;
		tweetItemList = tweetRepository.getTweetItemList(request.getAccessToken(), request.getPaging());
		return tweetItemList;
	}

	@Getter
	@AllArgsConstructor
	public static class TimelineRequest {

		private AccessToken accessToken;

		private Paging paging;

	}

}
