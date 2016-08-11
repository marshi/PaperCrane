package application.android.marshi.papercrane.domain.usecase.reaction;

import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.TweetApiRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * Copyright: CYBER AGNET. INC
 */
public class RemoveFavUseCase extends UseCase<RemoveFavUseCase.RemoveFavParameter, Boolean>{

	@Inject
	TweetApiRepository tweetApiRepository;

	@Inject
	public RemoveFavUseCase() {}

	@Override
	protected Boolean call(RemoveFavParameter param) throws TwitterException {
		return tweetApiRepository.removeFav(param.getAccessToken(), param.getTweetId());
	}

	@Data
	@AllArgsConstructor
	public static class RemoveFavParameter {

		private AccessToken accessToken;

		private Long tweetId;

	}

}
