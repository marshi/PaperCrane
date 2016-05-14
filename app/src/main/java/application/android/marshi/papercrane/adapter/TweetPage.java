package application.android.marshi.papercrane.adapter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author marshi on 2016/05/14.
 */
@AllArgsConstructor
@Getter
public enum TweetPage {

	HomeTimeline(1);

	private int code;

	public static TweetPage from(int code) {
		for (TweetPage page : values()) {
			if (code == page.getCode()) {
				return page;
			}
		}
		return null;
	}

}
