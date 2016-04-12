package retkipaivakirja.redneck.wanderers.com.retkipaivakirja.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import retkipaivakirja.redneck.wanderers.com.retkipaivakirja.R;
import retkipaivakirja.redneck.wanderers.com.retkipaivakirja.adapter.PagerAdapter;

/**
 * Created by Ari Iivari on 23.3.2015.
 */
public class PointsDetailActivity extends Activity {
    public static final String TAG = "HikingDiary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle(R.string.point_activity);

        setContentView(R.layout.activity_point_pager);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(new PagerAdapter(getFragmentManager(), (java.util.List<retkipaivakirja.redneck.wanderers.com.retkipaivakirja.model.Point>) getIntent().getExtras().getSerializable("SWIPEACTIVITY")));
        pager.setCurrentItem(getIntent().getExtras().getInt("SWIPEACTIVITYID"));
    }
}
