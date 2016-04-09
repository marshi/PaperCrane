package application.android.marshi.papercrane.domain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author marshi on 2016/04/09.
 */
public abstract class UseCase<T> {

	private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

	public void start(final T param) {
		mExecutorService.submit(() -> call(param));
	}

	abstract protected void call(T param);

}
