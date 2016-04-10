package application.android.marshi.papercrane.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * twitterのIDとパスワード.
 * @author marshi on 2016/04/14.
 */
@AllArgsConstructor
@Getter
public class Identity {

	private Id id;
	private Password password;

}
