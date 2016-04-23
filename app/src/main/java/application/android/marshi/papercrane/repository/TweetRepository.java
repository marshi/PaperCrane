package application.android.marshi.papercrane.repository;

import application.android.marshi.papercrane.domain.model.TweetItem;
import lombok.Data;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * @author marshi on 2016/04/10.
 */
@Data
public class TweetRepository {

	private Twitter twitter;

	@Inject
	public TweetRepository() {
		twitter = TwitterFactory.getSingleton();
	}

	public List<TweetItem> getTweetItemList() {
		return null;
	}

}
