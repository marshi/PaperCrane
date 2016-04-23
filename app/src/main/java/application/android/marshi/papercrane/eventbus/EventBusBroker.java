package application.android.marshi.papercrane.eventbus;

import lombok.Getter;

/**
 * @author marshi on 2016/04/21.
 */
@Getter
public class EventBusBroker {

	public static final EventBus<String> stringEventBus = new EventBus<>();

}
