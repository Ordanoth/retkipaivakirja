package redneck.wanderers.com.retkipaivakirja.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import redneck.wanderers.com.retkipaivakirja.R;
import redneck.wanderers.com.retkipaivakirja.adapter.PagerAdapter;

/**
 * Created by Ari Iivari on 23.3.2015.
 */
public class PointsDetailActivity extends FragmentActivity {
    public static final String TAG = "HikingDiary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.point_activity);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_point_pager);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), (java.util.List<redneck.wanderers.com.retkipaivakirja.model.Point>) getIntent().getExtras().getSerializable("SWIPEACTIVITY")));
        pager.setCurrentItem(getIntent().getExtras().getInt("SWIPEACTIVITYID"));
    }
}
