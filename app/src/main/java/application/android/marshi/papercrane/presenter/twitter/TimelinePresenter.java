package application.android.marshi.papercrane.presenter.twitter;

import application.android.marshi.papercrane.domain.model.TweetItem;
import application.android.marshi.papercrane.domain.usecase.timeline.GetTimelineUseCase;
import application.android.marshi.papercrane.presenter.Presenter;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;

/**
 * @author marshi on 2016/04/23.
 */
public class TimelinePresenter extends Presenter {

	@Inject
	public TimelinePresenter() {}

	@Inject
	private GetTimelineUseCase getTimelineUseCase;

	public List<TweetItem> getTweetItems(AccessToken accessToken) {
		getTimelineUseCase.start(accessToken);
		return null;
	}

}


