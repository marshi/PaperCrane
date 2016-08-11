package application.android.marshi.papercrane.database.dto;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import lombok.NoArgsConstructor;

/**
 * Copyright: CYBER AGNET. INC
 */
@Table
@NoArgsConstructor
public class TweetAndPageRelation {

	public TweetAndPageRelation(Long tweetId, String tweetPage) {
		this.tweetId = tweetId;
		this.tweetPage = tweetPage;
	}

	@PrimaryKey(autoincrement = true, auto = true)
	public long id;

	@Column(indexed = true)
	public Long tweetId;

	@Column(indexed = true)
	public String tweetPage;

}
