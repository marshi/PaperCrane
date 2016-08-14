package application.android.marshi.papercrane.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import application.android.marshi.papercrane.enums.TweetPage;
import application.android.marshi.papercrane.fragment.TweetListFragment;

/**
 * @author marshi on 2016/05/14.
 */
public class TweetPagerAdapter extends FragmentPagerAdapter {

	public TweetPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		TweetPage page = TweetPage.from(position);
		if (page == null) {
			return TweetListFragment.newInstance(TweetPage.HomeTimeline);
		}
		return TweetListFragment.newInstance(page);
	}

	@Override
	public int getCount() {
		return TweetPage.values().length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TweetPage.from(position).getTitle();
	}



}
