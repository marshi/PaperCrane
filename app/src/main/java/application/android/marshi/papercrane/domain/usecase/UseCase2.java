package application.android.marshi.papercrane.domain.usecase;

import rx.Observable;
import twitter4j.TwitterException;

/**
 * @author marshi on 2016/05/06.
 */
abstract public class UseCase2<P, R> {

	abstract public R call(P param) throws TwitterException;

	public Observable<R> start(P param) {
		return Observable.create(subscriber -> {
			R result = null;
			try {
				result = call(param);
			} catch (TwitterException e) {
				subscriber.onError(e);
			}
			subscriber.onNext(result);
			subscriber.onCompleted();
		});
	}
}
