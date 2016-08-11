package application.android.marshi.papercrane.repository.store;

import android.util.Log;
import application.android.marshi.papercrane.database.dto.OrmaDatabase;
import application.android.marshi.papercrane.database.dto.Tweet;
import com.github.gfx.android.orma.exception.OrmaException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marshi on 2016/05/29.
 */
public class TweetStoreRepository {

	private long storeSize = 1000;

	@Inject
	OrmaDatabase ormaDatabase;

	@Inject
	public TweetStoreRepository() {}

	public void set(Tweet tweet) {
		List<Tweet> tweetList = new ArrayList<>();
		tweetList.add(tweet);
		set(tweetList);
	}

	public void set(List<Tweet> tweetList) {
		insert(tweetList);
		List<Tweet> tweets = selectOrderByDateWithOffset(storeSize);
		List<Long> ids = new ArrayList<>();
		for (Tweet t : tweets) {
			ids.add(t.getId());
		}
		delete(ids);
	}

	public List<Tweet> select(List<Long> ids) {
		return ormaDatabase.relationOfTweet().selector().idIn(ids).orderByTweetAtDesc().toList();
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

	private List<Tweet> selectOrderByDateWithOffset(long offset) {
		return ormaDatabase
			.relationOfTweet()
			.selector()
			.orderByTweetAtDesc()
			.offset(offset)
			.limit(1000)
			.toList();
	}

}
