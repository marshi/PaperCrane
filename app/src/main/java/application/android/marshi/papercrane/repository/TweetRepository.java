package application.android.marshi.papercrane.repository;

import application.android.marshi.papercrane.TwitterClient;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.enums.TweetPage;
import lombok.Data;
import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
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

	public List<TweetItem> getTweetItemList(AccessToken accessToken, Paging paging, TweetPage tweetPage) throws TwitterException {
		Twitter twitter = TwitterClient.getInstance(accessToken);
		ResponseList<Status> statuses =  twitter.getHomeTimeline(paging);
		switch (tweetPage) {
			case MentionTimeline:
				statuses =  twitter.getMentionsTimeline(paging);
				break;
			case DirectMessage:
				ResponseList<DirectMessage> directMessages = twitter.getDirectMessages(paging);
				List<TweetItem> tweetItemList = new ArrayList<>();
				for (DirectMessage dm : directMessages) {
					tweetItemList.add(convertFrom(dm));
				}
				return tweetItemList;
			case HomeTimeline:
			default:
				statuses =  twitter.getHomeTimeline(paging);
		}
		List<TweetItem> tweetItemList = new ArrayList<>();
		for (Status status : statuses) {
			tweetItemList.add(convertFrom(status));
		}
		return tweetItemList;
	}

	private TweetItem convertFrom(Status status) {
		User user = status.getUser();
		return new TweetItem(
			status.getId(),
			"@" + user.getScreenName(),
			status.getText(),
			user.getName(),
			user.getProfileImageURL(),
			status.getCreatedAt()
		);
	}

	private TweetItem convertFrom(DirectMessage dm) {
		User user = dm.getSender();
		return new TweetItem(
			dm.getId(),
			"@" + user.getScreenName(),
			dm.getText(),
			user.getName(),
			user.getProfileImageURL(),
			dm.getCreatedAt()
		);
	}

}
