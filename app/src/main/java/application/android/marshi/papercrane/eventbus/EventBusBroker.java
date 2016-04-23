package application.android.marshi.papercrane.eventbus;

import application.android.marshi.papercrane.domain.model.TweetItem;
import lombok.Getter;

import java.util.List;

/**
 * @author marshi on 2016/04/21.
 */
@Getter
public class EventBusBroker {

	public static final EventBus<String> stringEventBus = new EventBus<>();

	public static final EventBus<List<TweetItem>> tweetListEventBus = new EventBus<>();

}
