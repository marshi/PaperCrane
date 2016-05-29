package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.database.Cache;
import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.enums.TweetPage;
import twitter4j.TwitterException;

import javax.inject.Inject;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marshi on 2016/05/29.
 */
public class GetStoredTimelineUseCase extends UseCase<TweetPage, List<TweetItem>> {

	@Inject
	public GetStoredTimelineUseCase() {}

	@Inject
	Cache cache;

	@Override
	protected List<TweetItem> call(TweetPage tweetPage) throws TwitterException {
		List<Tweet> tweets = cache.get(tweetPage);
		AbstractList<TweetItem> tweetItemList = new ArrayList<>();
		for (Tweet tweet : tweets) {
			tweetItemList.add(
				new TweetItem(
					tweet.getTweetId(),
					tweet.getUserId(),
					tweet.getContent(),
					tweet.getUserName(),
					tweet.getProfileImageUrl(),
					tweet.getTweetAt()
				)
			);
		}
		return tweetItemList;
	}
}
