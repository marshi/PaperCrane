package application.android.marshi.papercrane.repository.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import application.android.marshi.papercrane.database.dto.OrmaDatabase;
import application.android.marshi.papercrane.database.dto.ReadMore;
import com.github.gfx.android.orma.exception.OrmaException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author marshi on 2016/06/25.
 */
public class ReadMoreStorageRepository {

	@Inject
	OrmaDatabase ormaDatabase;

	@Inject
	public ReadMoreStorageRepository() { }

	public List<ReadMore> get() {
		return selectAll();
	}

	public void set(ReadMore readMore) {
		insert(readMore);
	}

	public boolean delete(long id) {
		return ormaDatabase.deleteFromReadMore().idEq(id).execute() == 1;
	}

	@Nullable
	private Long insert(@NonNull ReadMore readMore) {
		try {
			return ormaDatabase.relationOfReadMore().inserter().execute(readMore);
		} catch (OrmaException e) {
			Log.e("orma", "", e);
		}
		return null;
	}

	private List<ReadMore> selectAll() {
		return ormaDatabase.selectFromReadMore().toList();
	}

}
