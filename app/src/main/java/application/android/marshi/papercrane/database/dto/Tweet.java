package application.android.marshi.papercrane.database.dto;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author marshi on 2016/05/29.
 */
@Table
@NoArgsConstructor
@Getter
public class Tweet {

    public Tweet(Long tweetId, String userId, String userName, String content, String profileImageUrl, Boolean fav, Date tweetAt, String tweetPage) {
        this.tweetId = tweetId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.profileImageUrl = profileImageUrl;
        this.fav = fav;
        this.tweetAt = tweetAt;
        this.tweetPage = tweetPage;
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

	/**
	 * ページを表す文字列.
     */
    @Column(indexed = true)
    public String tweetPage;

}
