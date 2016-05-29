package application.android.marshi.papercrane.di;

import android.app.Application;
import application.android.marshi.papercrane.di.component.AppComponent;
import application.android.marshi.papercrane.di.component.DaggerAppComponent;
import application.android.marshi.papercrane.di.module.AppModule;
import com.facebook.stetho.Stetho;

/**
 * @author marshi on 2016/04/17.
 */
public class App extends Application {

	private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        initializeStetho();
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void initializeStetho() {
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        );
    }

    public static AppComponent getApplicationComponent() {
        return appComponent;
    }
}

