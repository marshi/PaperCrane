package application.android.marshi.papercrane.repository;

import android.util.Log;
import application.android.marshi.papercrane.database.dto.OrmaDatabase;
import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.enums.TweetPage;
import com.github.gfx.android.orma.exception.OrmaException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author marshi on 2016/05/29.
 */
public class TweetStoreRepository {

	@Inject
	public TweetStoreRepository(){}

	@Inject
	public OrmaDatabase ormaDatabase;

	public void insertTweetList(List<Tweet> tweetList) {
		try {
			ormaDatabase.relationOfTweet().inserter().executeAll(tweetList);
		} catch (OrmaException e) {
			Log.e("orma", "", e);
		}
	}

	public void deleteTweetList(List<Long> tweetIdList) {
		for (Long tweetId : tweetIdList) {
			ormaDatabase.relationOfTweet().deleter().idEq(tweetId).execute();
		}
	}

	public List<Tweet> selectOrderYByDate(TweetPage tweetPage) {
		return ormaDatabase
			.relationOfTweet()
			.selector()
			.tweetPageEq(tweetPage.name())
			.orderByTweetAtDesc()
			.toList();
	}

}
