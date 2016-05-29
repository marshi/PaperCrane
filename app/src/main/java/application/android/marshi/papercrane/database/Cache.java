package application.android.marshi.papercrane.database;

import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.repository.TweetStoreRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marshi on 2016/05/29.
 */
public class Cache {

	private long cacheSize = 1000;

	@Inject
	public Cache() {}

	@Inject
	TweetStoreRepository tweetStoreRepository;

	public void set(List<Tweet> tweetList, TweetPage tweetPage) {
		tweetStoreRepository.insertTweetList(tweetList);
		List<Tweet> tweets = tweetStoreRepository.selectOrderYByDate(tweetPage, cacheSize);
		List<Long> ids = new ArrayList<>();
		for (Tweet t : tweets) {
			ids.add(t.getId());
		}
		tweetStoreRepository.deleteTweetList(ids);
	}

	public List<Tweet> get(TweetPage tweetPage) {
		return tweetStoreRepository.selectOrderYByDate(tweetPage);
	}

}
