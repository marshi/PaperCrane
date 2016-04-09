package application.android.marshi.papercrane.domain.timeline;

import application.android.marshi.papercrane.domain.UseCase;
import application.android.marshi.papercrane.model.User;
import application.android.marshi.papercrane.repository.TweetRepository;

/**
 * @author marshi on 2016/04/09.
 */
public class TimelineListUseCase extends UseCase<User> {

	private TweetRepository tweetRepository;

	@Override
	protected void call(User user) {

	}
}
