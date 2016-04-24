package application.android.marshi.papercrane.repository;

import android.util.Log;
import application.android.marshi.papercrane.TwitterClient;
import application.android.marshi.papercrane.domain.model.TweetItem;
import lombok.Data;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marshi on 2016/04/10.
 */
@Data
public class TweetRepository {

	@Inject
	public TweetRepository(){}

	public List<TweetItem> getTweetItemList(AccessToken accessToken) {
		try {
			Twitter twitter = TwitterClient.getInstance(accessToken);
			ResponseList<Status> statuses =  twitter.getHomeTimeline();
			List<TweetItem> tweetItemList = new ArrayList<>();
			for (Status status : statuses) {
				tweetItemList.add(convertFrom(status));
			}
			return tweetItemList;
		} catch (Exception e) {
			Log.e("tag", e.toString());
		}
		return null;
	}

	private TweetItem convertFrom(Status status) {
		return new TweetItem(status.getId(), status.getText(), status.getUser().getProfileImageURL());
	}

}
