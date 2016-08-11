package application.android.marshi.papercrane.service.twitter.streaming;

import android.util.Log;
import application.android.marshi.papercrane.database.dto.Tweet;
import application.android.marshi.papercrane.database.dto.TweetAndPageRelation;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.repository.store.TweetAndPageRelationRepository;
import application.android.marshi.papercrane.repository.store.TweetStoreRepository;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.UserStreamAdapter;

import javax.inject.Inject;

public class TweetStreamingService extends UserStreamAdapter {

	@Inject
	TweetStoreRepository tweetStoreRepository;

	@Inject
	TweetAndPageRelationRepository tweetAndPageRelationRepository;

	@Inject
	public TweetStreamingService() {}

	@Override
	public void onStatus(Status status) {
		Tweet tweet = new Tweet(status);
		tweetStoreRepository.set(tweet);
		tweetAndPageRelationRepository.add(new TweetAndPageRelation(status.getId(), TweetPage.HomeTimeline.name()));
		if (status.getInReplyToScreenName() != null) {
			tweetAndPageRelationRepository.add(new TweetAndPageRelation(status.getId(), TweetPage.ReplyTimeline.name()));
		}
		Log.i("updated", status.getText());
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		long statusId = statusDeletionNotice.getStatusId();
		Log.i("statusid", ""+statusId);
	}
}
