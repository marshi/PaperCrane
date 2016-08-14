package application.android.marshi.papercrane.di.module;

import android.content.Context;
import application.android.marshi.papercrane.service.ToastService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author marshi on 2016/07/09.
 */
@Singleton
@Module
public class ServiceModule {

	@Singleton
	@Provides
	public ToastService provideToastService(Context context) {
		return new ToastService(context);
	}

}
