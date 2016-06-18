package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.database.dto.ReadMore;
import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.enums.ViewType;
import application.android.marshi.papercrane.repository.cache.ReadMoreCacheRepository;
import application.android.marshi.papercrane.repository.cache.TweetCacheRepository;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import twitter4j.TwitterException;

import javax.inject.Inject;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author marshi on 2016/05/29.
 */
public class GetStoredTimelineUseCase extends UseCase<TweetPage, List<TweetItem>> {

	@Inject
	public GetStoredTimelineUseCase() {}

	@Inject
	TweetCacheRepository tweetCacheRepository;

	@Inject
	ReadMoreCacheRepository readMoreCacheRepository;

	@Override
	protected List<TweetItem> call(TweetPage tweetPage) throws TwitterException {
		List<ReadMore> readMores = readMoreCacheRepository.get();
		Map<Long, ReadMore> readMoreMap = Stream.of(readMores).collect(Collectors.toMap(ReadMore::getJustAfterTweetId, v -> v));
		List<Tweet> tweets = tweetCacheRepository.get(tweetPage);
		AbstractList<TweetItem> tmpTweetItemList =
			Stream.of(tweets).map(tweet ->
				new TweetItem(
					tweet.getTweetId(),
					tweet.getUserId(),
					tweet.getContent(),
					tweet.getUserName(),
					tweet.getProfileImageUrl(),
					tweet.getTweetAt(),
					ViewType.Normal
				)).collect(Collectors.toCollection(ArrayList::new));
		List<TweetItem> tweetItems = new ArrayList<>();
		Stream.of(tmpTweetItemList).forEach(t -> {
			tweetItems.add(t);
			if (readMoreMap.containsKey(t.getId())) {
				ReadMore readMore = readMoreMap.get(t.getId());
				tweetItems.add(TweetItem.createReadMore(readMore.getId()));
				readMoreMap.remove(readMore.getJustAfterTweetId());
			}
		});
		return tweetItems;
	}

}
