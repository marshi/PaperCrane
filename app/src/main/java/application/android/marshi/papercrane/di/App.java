package application.android.marshi.papercrane.di;

import android.app.Application;
import application.android.marshi.papercrane.di.component.AppComponent;
import application.android.marshi.papercrane.di.component.DaggerAppComponent;
import application.android.marshi.papercrane.di.module.AppModule;

/**
 * @author marshi on 2016/04/17.
 */
public class App extends Application {

	private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getApplicationComponent() {
        return appComponent;
    }
}

