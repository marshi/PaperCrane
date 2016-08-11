package application.android.marshi.papercrane.domain.usecase.reaction;

import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.TweetApiRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * @author mukai_masaki on 2016/07/31.
 *         Copyright: CYBER AGNET. INC
 */
public class AddFavUseCase extends UseCase<AddFavUseCase.AddFavRequest, Boolean>{

	@Inject
	public AddFavUseCase() {}

	@Inject
	TweetApiRepository tweetApiRepository;

	@Override
	protected Boolean call(AddFavRequest param) throws TwitterException {
		return tweetApiRepository.addFav(param.getAccessToken(), param.getTweetId());
	}

	@Data
	@AllArgsConstructor
	public static class AddFavRequest {

		private AccessToken accessToken;

		private Long tweetId;

	}

}
