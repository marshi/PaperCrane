package application.android.marshi.papercrane.repository;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Tweetテーブルに保存されているツイートのうち、cache用に保存されている最新ツイートのIDを管理する.
 */
public class LatestCacheTweetIdRepository {

	@Inject
	Context context;

	@Inject
	public LatestCacheTweetIdRepository() {}

	/**
	 *
	 * @param id キャッシュデータのうち最新のツイートID.
	 */
	public void set(Long id) {
		SharedPreferences preferences = context.getSharedPreferences("latest_cache_tweet_id", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = preferences.edit();
		edit.putLong("id", id);
		edit.apply();
	}

	/**
	 *
	 * @return キャッシュデータのうち最新のツイートID.
	 */
	public Long get() {
		SharedPreferences preferences = context.getSharedPreferences("latest_cache_tweet_id", Context.MODE_PRIVATE);
		return preferences.getLong("id", 0L);
	}

}
