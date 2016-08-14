package application.android.marshi.papercrane.view;

import application.android.marshi.papercrane.domain.model.TweetItem;

import java.util.List;

/**
 * @author marshi on 2016/07/09.
 */
public interface TimelineSwipeRefreshView {

	void updateTimeline(List<TweetItem> tweetItems);

	void failToUpdateTimeline();

}
