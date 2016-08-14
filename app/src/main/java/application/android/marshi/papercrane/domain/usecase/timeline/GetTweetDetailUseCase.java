package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.TweetRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * Copyright: marshi
 */

public class GetTweetDetailUseCase extends UseCase<GetTweetDetailUseCase.Parameter, TweetItem> {

	@Inject
	public GetTweetDetailUseCase() {}

	@Inject
	TweetRepository tweetRepository;

	@Override
	protected TweetItem call(Parameter param) throws TwitterException {
		return tweetRepository.detail(param.getAccessToken(), param.getTweetId());
	}

	@Getter
	@AllArgsConstructor
	public static class Parameter {
		private AccessToken accessToken;
		private Long tweetId;
	}

}
