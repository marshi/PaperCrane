package application.android.marshi.papercrane.presenter.twitter;

import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase;
import application.android.marshi.papercrane.presenter.Presenter;
import twitter4j.Paging;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;

import static application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase.TimelineRequest;

/**
 * @author marshi on 2016/04/23.
 */
public class TimelinePresenter extends Presenter {

	@Inject
	public TimelinePresenter() {}

	@Inject
	GetTimelineUseCase getTimelineUseCase;

	public List<TweetItem> getTweetItems(AccessToken accessToken) {
		Paging paging = new Paging(1);
		getTimelineUseCase.start(new TimelineRequest(accessToken, paging));
		return null;
	}

	public List<TweetItem> getTweetItems(AccessToken accessToken, long maxId) {
		Paging paging = new Paging().maxId(maxId - 1).count(20);
		getTimelineUseCase.start(new TimelineRequest(accessToken, paging));
		return null;
	}

}


