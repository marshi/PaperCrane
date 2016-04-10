package application.android.marshi.papercrane.eventbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author marshi on 2016/04/20.
 */
public class EventBus<T> {

	private Map<Event, Subject<T, T>> eventSubject = new HashMap<>();

	public void set(Event event, T value) {
		Subject<T, T> subject = eventSubject.get(event);
		subject.onNext(value);
	}

	public Observable<T> get(Event event) {
		Subject<T, T> subject = eventSubject.get(event);
		if (subject == null) {
			subject = new SerializedSubject<>(PublishSubject.create());
			eventSubject.put(event, subject);
		}
		return eventSubject.get(event);
	}

}
