package application.android.marshi.papercrane.database.dto;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author marshi on 2016/06/25.
 */
@Table
@Getter
@NoArgsConstructor
public class ReadMore {

	public ReadMore(long justAfterTweetId) {
		this.justAfterTweetId = justAfterTweetId;
	}

	@PrimaryKey(autoincrement = true, auto = true)
	public long id;

	@Column(indexed = true)
	public long justAfterTweetId;

}
