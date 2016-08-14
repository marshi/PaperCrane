package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.repository.TweetRepository;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import twitter4j.Paging;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marshi on 2016/04/09.
 */
public class GetTimelineUseCase extends UseCase<GetTimelineUseCase.TimelineRequest, List<TweetItem>> {

	@Inject
	TweetRepository tweetRepository;

	@Inject
	public GetTimelineUseCase(){}

	protected List<TweetItem> call(TimelineRequest request) throws TwitterException {
		List<TweetItem> rawTweetItemList = tweetRepository.getTweetItemList(request.getAccessToken(), request.getPaging(), request.getType());
		if (rawTweetItemList.isEmpty()) {
			return new ArrayList<>();
		}
		List<TweetItem> insertingTweetItems = Stream.of(rawTweetItemList).takeWhile(value ->
			!value.getId().equals(request.getLoadedLatestTweetId())
		).collect(Collectors.toList());
		if (insertingTweetItems.isEmpty()) {
			return new ArrayList<>();
		}
		boolean readMoreCondition = insertingTweetItems.size() == rawTweetItemList.size();
		if (readMoreCondition) {
			TweetItem lastTweet = rawTweetItemList.get(rawTweetItemList.size() - 1);
			rawTweetItemList.add(rawTweetItemList.size(), TweetItem.createReadMore(lastTweet.getId()));
			return rawTweetItemList;
		}

		return insertingTweetItems;
	}

	@Getter
	@AllArgsConstructor
	public static class TimelineRequest {

		private AccessToken accessToken;

		private Paging paging;

		private TweetPage type;

		private Long loadedLatestTweetId;

	}

}
