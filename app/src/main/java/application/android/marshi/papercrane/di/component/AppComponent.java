package application.android.marshi.papercrane.di.component;

import application.android.marshi.papercrane.activity.TimelineActivity;
import application.android.marshi.papercrane.activity.LoginActivity;
import application.android.marshi.papercrane.activity.MainActivity;
import application.android.marshi.papercrane.activity.TweetDetailActivity;
import application.android.marshi.papercrane.di.module.AppModule;
import application.android.marshi.papercrane.di.module.DatabaseModule;
import application.android.marshi.papercrane.di.module.RepositoryModule;
import application.android.marshi.papercrane.di.module.ServiceModule;
import application.android.marshi.papercrane.fragment.TweetEditorFragment;
import application.android.marshi.papercrane.fragment.TweetListFragment;
import dagger.Component;

import javax.inject.Singleton;

/**
 * @author marshi on 2016/04/17.
 */
@Singleton
@Component(modules = {RepositoryModule.class, AppModule.class, ServiceModule.class, DatabaseModule.class})
public interface AppComponent {

	void inject(MainActivity mainActivity);

	void inject(LoginActivity loginActivity);

	void inject(TimelineActivity timelineActivity);

	void inject(TweetListFragment tweetListFragment);

	void inject(TweetEditorFragment tweetEditorFragment);

	void inject(TweetDetailActivity tweetDetailActivity);

}
