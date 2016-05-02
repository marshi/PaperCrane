package application.android.marshi.papercrane.di.component;

import application.android.marshi.papercrane.activity.HomeTimelineActivity;
import application.android.marshi.papercrane.activity.LoginActivity;
import application.android.marshi.papercrane.activity.MainActivity;
import application.android.marshi.papercrane.di.module.AppModule;
import application.android.marshi.papercrane.di.module.RepositoryModule;
import application.android.marshi.papercrane.fragment.TweetListFragment;
import dagger.Component;

import javax.inject.Singleton;

/**
 * @author marshi on 2016/04/17.
 */
@Singleton
@Component(modules = {RepositoryModule.class, AppModule.class})
public interface AppComponent {

	void inject(MainActivity mainActivity);

	void inject(LoginActivity loginActivity);

	void inject(HomeTimelineActivity homeTimelineActivity);

	void inject(TweetListFragment tweetListFragment);
}
