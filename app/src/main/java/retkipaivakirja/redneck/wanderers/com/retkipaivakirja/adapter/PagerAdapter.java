package retkipaivakirja.redneck.wanderers.com.retkipaivakirja.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import retkipaivakirja.redneck.wanderers.com.retkipaivakirja.activities.PointFragment;
import retkipaivakirja.redneck.wanderers.com.retkipaivakirja.model.Point;

/**
 * Created by Ari Iivari on 21.3.2015.
 */
public class PagerAdapter  extends FragmentPagerAdapter {
    private final List<Point> mList;

    public PagerAdapter(FragmentManager fm, List<Point> lp) {
        super(fm);
        mList = lp;
    }

    @Override
    public Fragment getItem(int i) {
        return (PointFragment.newInstance(mList.get(i)));
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
