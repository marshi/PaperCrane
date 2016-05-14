package application.android.marshi.papercrane.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author marshi on 2016/05/14.
 */
@AllArgsConstructor
@Getter
public enum TweetPage {

	HomeTimeline(0, "Home"),
	MentionTimeline(1, "Mention"),
	DirectMessage(2, "Direct Message");

	public static String BUNDLE_KEY = "tweet_type";

	private int code;

	private String title;

	public static TweetPage from(int code) {
		for (TweetPage tweetPage : values()) {
			if (tweetPage.getCode() == code) {
				return tweetPage;
			}
		}
		return HomeTimeline;
	}

}
