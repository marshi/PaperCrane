package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.domain.model.User;
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
