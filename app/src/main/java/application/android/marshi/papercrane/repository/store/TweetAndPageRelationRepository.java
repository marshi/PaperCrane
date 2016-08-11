package application.android.marshi.papercrane.repository.store;

import android.util.Log;
import application.android.marshi.papercrane.database.dto.OrmaDatabase;
import application.android.marshi.papercrane.database.dto.TweetAndPageRelation;
import application.android.marshi.papercrane.enums.TweetPage;
import com.github.gfx.android.orma.exception.OrmaException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mukai_masaki on 2016/08/11.
 *         Copyright: CYBER AGNET. INC
 */
public class TweetAndPageRelationRepository {

	@Inject
	OrmaDatabase ormaDatabase;

	@Inject
	public TweetAndPageRelationRepository() {}

	public Long add(TweetAndPageRelation tweetAndPageRelation) {
		try {
			return ormaDatabase.relationOfTweetAndPageRelation().inserter().execute(tweetAndPageRelation);
		} catch (Exception e) {
			Log.e("orma", "", e);
		}
		return null;
	}

	public List<TweetAndPageRelation> get(TweetPage page) {
		try {
			return ormaDatabase.relationOfTweetAndPageRelation().selector().tweetPageEq(page.name()).toList();
		} catch (OrmaException e) {
			Log.e("orma", "", e);
		}
		return null;
	}

}
