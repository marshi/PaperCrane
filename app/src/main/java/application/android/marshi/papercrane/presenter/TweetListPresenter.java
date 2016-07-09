package application.android.marshi.papercrane.presenter;

import android.support.annotation.NonNull;
import application.android.marshi.papercrane.adapter.TweetRecyclerViewAdapter;
import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.repository.LastTweetAccessTimeRepository;
import application.android.marshi.papercrane.service.auth.AccessTokenService;
import application.android.marshi.papercrane.service.twitter.TimelineService;
import application.android.marshi.papercrane.view.TimelineSwipeRefreshView;
import com.trello.rxlifecycle.components.support.RxFragment;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.LinkedList;

/**
 * @author marshi on 2016/07/09.
 */
public class TweetListPresenter {

	@Inject
	public TweetListPresenter() {}

	@Inject
	LastTweetAccessTimeRepository lastTweetAccessTimeRepository;

	@Inject
	TimelineService timelineService;

	@Inject
	AccessTokenService accessTokenService;

	public void fetchLatestTimeline(
		RxFragment rxFragment,
		TimelineSwipeRefreshView timelineSwipeRefreshView,
		TweetRecyclerViewAdapter tweetRecyclerViewAdapter,
		TweetPage tweetPage
	) {
		AccessToken accessToken = accessTokenService.getAccessToken();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime lastAccessedTime = lastTweetAccessTimeRepository.get();
		int waitSeconds = 5;
		if (lastAccessedTime != null && lastAccessedTime.isAfter(now.minus(waitSeconds, ChronoUnit.SECONDS))) {
			timelineSwipeRefreshView.failToUpdateTimeline();
			return;
		}
		LinkedList<TweetItem> mValues = tweetRecyclerViewAdapter.getMValues();
		timelineService.loadLatestTweetItems(
			rxFragment,
			accessToken,
			//取得済の最新ツイートと新たに取得したツイートが一致することを調べるためにsinceIdは最新から2番目のツイートを利用する
			2 <= mValues.size() ? mValues.get(1).getId() : 1 == mValues.size() ? mValues.getFirst().getId() : null,
			!mValues.isEmpty() ? mValues.getFirst().getId() : null,
			tweetPage,
			timelineSwipeRefreshView::updateTimeline,
			timelineSwipeRefreshView::failToUpdateTimeline
		);
	}

	public void loadStoredTweetList(
		@NonNull RxFragment rxFragment,
		@NonNull TweetRecyclerViewAdapter tweetRecyclerViewAdapter,
		@NonNull TweetPage tweetPage
	) {
		if (tweetRecyclerViewAdapter.getMValues().isEmpty()) {
			timelineService.loadStoredTweets(
				rxFragment,
				tweetPage,
				tweetRecyclerViewAdapter::addLast
			);
		}
	}

}

