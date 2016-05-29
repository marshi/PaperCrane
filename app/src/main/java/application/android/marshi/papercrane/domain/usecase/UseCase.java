package application.android.marshi.papercrane.domain.usecase;

import rx.Observable;
import twitter4j.TwitterException;

/**
 * 非同期処理.
 * 処理結果はsubscribeして使う.
 * @author marshi on 2016/05/06.
 */
abstract public class UseCase<P, R> {

	abstract protected R call(P param) throws TwitterException;

	public Observable<R> start(P param) {
		return Observable.create(subscriber -> {
			R result = null;
			try {
				result = call(param);
				subscriber.onNext(result);
				subscriber.onCompleted();
			} catch (TwitterException e) {
				subscriber.onError(e);
			}
		});
	}
}
