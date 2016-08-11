package application.android.marshi.papercrane.database.dto;

import application.android.marshi.papercrane.repository.LatestCacheTweetIdRepository;
import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import twitter4j.Status;

import java.util.Date;

/**
 * ツイートデータの管理.
 * Tweetテーブルではtwitter apiで一度取得してアプリで表示済みのツイートのキャッシュと、
 * APIから取得したがまだアプリで表示されていない先読みデータの管理を行う.
 * キャッシュ用のデータと先読み用のデータの境界は{@link LatestCacheTweetIdRepository}で管理される.
 * @author marshi on 2016/05/29.
 */
@Table
@NoArgsConstructor
@Getter
public class Tweet {

	public Tweet(Long tweetId, String userId, String userName, String content, String profileImageUrl, Boolean fav, Date tweetAt) {
		this.tweetId = tweetId;
		this.userId = userId;
		this.userName = userName;
		this.content = content;
		this.profileImageUrl = profileImageUrl;
		this.fav = fav;
		this.tweetAt = tweetAt;
	}

	public Tweet(Status status) {
		this (
			status.getId(),
			"@" + status.getUser().getScreenName(),
			status.getUser().getName(),
			status.getText(),
			status.getUser().getProfileImageURL(),
			status.isFavorited(),
			status.getCreatedAt()
		);
	}

	@PrimaryKey(autoincrement = true, auto = true)
	public long id;

	@Column
	public Long tweetId;

	@Column
	public String userId;

	@Column
	public String userName;

	@Column
	public String content;

	@Column
	public String profileImageUrl;

	@Column
	public Boolean fav;

	@Column(indexed = true)
	public Date tweetAt;

}
