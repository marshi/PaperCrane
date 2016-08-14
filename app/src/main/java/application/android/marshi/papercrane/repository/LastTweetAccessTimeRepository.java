package application.android.marshi.papercrane.repository;

import org.threeten.bp.LocalDateTime;

import javax.inject.Inject;

/**
 * @author marshi on 2016/05/30.
 */
public class LastTweetAccessTimeRepository {

	private LocalDateTime localDateTime;

	@Inject
	public LastTweetAccessTimeRepository() {}

	public void set() {
		localDateTime = LocalDateTime.now();
	}

	public LocalDateTime get() {
		return localDateTime;
	}

}
