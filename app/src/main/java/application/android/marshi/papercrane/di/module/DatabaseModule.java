package application.android.marshi.papercrane.di.module;

import android.content.Context;
import application.android.marshi.papercrane.database.dto.OrmaDatabase;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author marshi on 2016/05/29.
 */
@Module
public class DatabaseModule {

	@Singleton
	@Provides
	public OrmaDatabase provideOrmaDatabase(Context context) {
		return OrmaDatabase.builder(context).build();
	}

}
