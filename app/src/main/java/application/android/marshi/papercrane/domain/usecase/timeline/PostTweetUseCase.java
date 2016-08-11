package application.android.marshi.papercrane.domain.usecase.timeline;

import application.android.marshi.papercrane.domain.usecase.UseCase;
import application.android.marshi.papercrane.repository.TweetApiRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;

/**
 * @author mukai_masaki on 2016/07/24.
 *         Copyright: CYBER AGNET. INC
 */
public class PostTweetUseCase extends UseCase<PostTweetUseCase.PostTweetRequest, Boolean> {

	@Inject
	TweetApiRepository tweetApiRepository;

	@Inject
	public PostTweetUseCase() {}

	@Override
	protected Boolean call(PostTweetRequest request) throws TwitterException {
		return tweetApiRepository.postTweet(request.getAccessToken(), request.getMessage());
	}

	@AllArgsConstructor
	@Data
	public static class PostTweetRequest {

		private AccessToken accessToken;

		/**
		 * 投稿するツイート
		 */
		private String message;

	}
}
