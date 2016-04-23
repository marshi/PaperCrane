package application.android.marshi.papercrane.di.module;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author marshi on 2016/04/17.
 */
@Singleton
@Module
public class AppModule {

	private Application application;

	public AppModule(Application application) {
		this.application = application;
	}

	@Provides
	public Context provideContext() {
		return application;
	}

}
