package application.android.marshi.papercrane.di.module;

import android.content.Context;
import application.android.marshi.papercrane.repository.AccessTokenRepository;
import application.android.marshi.papercrane.repository.LastTweetAccessTimeRepository;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author marshi on 2016/04/17.
 */
@Singleton
@Module
public class RepositoryModule {

	@Singleton
	@Provides
	public AccessTokenRepository provideAccessTokenRepository(Context context) {
		return new AccessTokenRepository(context);
	}

	@Singleton
	@Provides
	public LastTweetAccessTimeRepository lastTweetAccessTimeRepository() {
		return new LastTweetAccessTimeRepository();
	}

}
