package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.TweetRepository;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * @author marshi on 2016/04/09.
 */
public class GetTimelineUseCase extends UseCase<AccessToken> {

	@Inject
	public GetTimelineUseCase(){}

	@Inject
	private TweetRepository tweetRepository;

	@Override
	protected void call(AccessToken accessToken) {
		tweetRepository.getTweetItemList();
	}
}
