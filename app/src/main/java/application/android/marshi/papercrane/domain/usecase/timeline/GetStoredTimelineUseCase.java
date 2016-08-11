package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.database.dto.ReadMore;
import application.android.marshi.papercrane.database.dto.TweetAndPageRelation;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.enums.ViewType;
import application.android.marshi.papercrane.repository.store.ReadMoreStorageRepository;
import application.android.marshi.papercrane.repository.store.TweetAndPageRelationRepository;
import application.android.marshi.papercrane.repository.store.TweetStoreRepository;
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
	TweetAndPageRelationRepository tweetAndPageRelationRepository;

	@Inject
	TweetStoreRepository tweetStoreRepository;

	@Inject
	ReadMoreStorageRepository readMoreStorageRepository;

	@Override
	protected List<TweetItem> call(TweetPage tweetPage) throws TwitterException {
		List<ReadMore> readMores = readMoreStorageRepository.get();
		Map<Long, ReadMore> readMoreMap = Stream.of(readMores).collect(Collectors.toMap(ReadMore::getJustAfterTweetId, v -> v));
		List<TweetAndPageRelation> tweetAndPageRelations = tweetAndPageRelationRepository.get(tweetPage);
		List<Long> ids = Stream.of(tweetAndPageRelations).map(t -> t.id).collect(Collectors.toList());
		List<Tweet> tweets = tweetStoreRepository.select(ids);
		AbstractList<TweetItem> tmpTweetItemList =
			Stream.of(tweets).map(tweet ->
				new TweetItem(
					tweet.getTweetId(),
					tweet.getUserId(),
					tweet.getContent(),
					tweet.getUserName(),
					tweet.getProfileImageUrl(),
					tweet.getFav(),
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
