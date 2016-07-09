package application.android.marshi.papercrane.di.module;

import android.content.Context;
import application.android.marshi.papercrane.domain.usecase.toast.ToastUseCase;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author marshi on 2016/07/09.
 */
@Singleton
@Module
public class UseCaseModule {

	@Singleton
	@Provides
	public ToastUseCase provideToastUseCase(Context context) {
		return new ToastUseCase(context);
	}

}
