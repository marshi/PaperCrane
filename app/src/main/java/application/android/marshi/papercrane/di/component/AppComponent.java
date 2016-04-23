package application.android.marshi.papercrane.di.component;

import application.android.marshi.papercrane.activity.LoginActivity;
import application.android.marshi.papercrane.activity.MainTimelineActivity;
import application.android.marshi.papercrane.di.module.AppModule;
import application.android.marshi.papercrane.di.module.RepositoryModule;
import dagger.Component;

import javax.inject.Singleton;

/**
 * @author marshi on 2016/04/17.
 */
@Singleton
@Component(modules = {RepositoryModule.class, AppModule.class})
public interface AppComponent {

	void inject(LoginActivity loginActivity);

	void inject(MainTimelineActivity mainTimelineActivity);

}
