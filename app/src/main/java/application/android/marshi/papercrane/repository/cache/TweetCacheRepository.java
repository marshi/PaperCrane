package application.android.marshi.papercrane.repository.cache;

import android.util.Log;
import application.android.marshi.papercrane.database.dto.OrmaDatabase;
import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.enums.TweetPage;
import com.github.gfx.android.orma.exception.OrmaException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marshi on 2016/05/29.
 */
public class TweetCacheRepository {

	private long cacheSize = 1000;

	@Inject
	OrmaDatabase ormaDatabase;

	@Inject
	public TweetCacheRepository() {}

	public void set(Tweet tweet, TweetPage tweetPage) {
		List<Tweet> tweetList = new ArrayList<>();
		tweetList.add(tweet);
		set(tweetList, tweetPage);
	}

	public void set(List<Tweet> tweetList, TweetPage tweetPage) {
		insert(tweetList);
		List<Tweet> tweets = selectOrderByDate(tweetPage, cacheSize);
		List<Long> ids = new ArrayList<>();
		for (Tweet t : tweets) {
			ids.add(t.getId());
		}
		delete(ids);
	}

	public List<Tweet> get(TweetPage tweetPage) {
		return selectOrderByDate(tweetPage);
	}

	private void insert(List<Tweet> tweetList) {
		try {
			ormaDatabase.relationOfTweet().inserter().executeAll(tweetList);
		} catch (OrmaException e) {
			Log.e("orma", "", e);
		}
	}

	private void delete(List<Long> tweetIdList) {
		for (Long tweetId : tweetIdList) {
			ormaDatabase.relationOfTweet().deleter().idEq(tweetId).execute();
		}
	}

	private List<Tweet> selectOrderByDate(TweetPage tweetPage, long offset) {
		return ormaDatabase
			.relationOfTweet()
			.selector()
			.tweetPageEq(tweetPage.name())
			.orderByTweetAtDesc()
			.offset(offset)
			.limit(1000)
			.toList();
	}

	private List<Tweet> selectOrderByDate(TweetPage tweetPage) {
		return ormaDatabase
			.relationOfTweet()
			.selector()
			.tweetPageEq(tweetPage.name())
			.orderByTweetAtDesc()
			.toList();
	}

}
