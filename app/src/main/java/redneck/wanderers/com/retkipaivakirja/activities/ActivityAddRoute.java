package redneck.wanderers.com.retkipaivakirja.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import redneck.wanderers.com.retkipaivakirja.R;
import redneck.wanderers.com.retkipaivakirja.dao.RouteDAO;
import redneck.wanderers.com.retkipaivakirja.model.Route;

/**
 * Created by Ari Iivari on 26.2.2015.
 */

public class ActivityAddRoute extends AppCompatActivity implements OnClickListener {

    public static final String TAG = "HikingDiary";

    private EditText mTxtRouteName;
    private TextView mTvRouteDate;
//    private ImageButton mBtnChangeDate;
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    private RouteDAO mRouteDao;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mSelectedDay = dayOfMonth;
            mSelectedMonth = monthOfYear;
            mSelectedYear = year;

            updateDateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle(getString(R.string.route_add));
        initViews();
        this.mRouteDao = new RouteDAO(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSave);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable routeName = mTxtRouteName.getText();
                String routeDate = mTvRouteDate.getText().toString();
                if (!TextUtils.isEmpty(routeName) && !TextUtils.isEmpty(routeDate)) {
                    Route createdRoute = mRouteDao.createRoute(routeName.toString(), routeDate);
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.EXTRA_ADDED_ROUTE, createdRoute);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
//                    Toast.makeText(this, R.string.empty_fields_message, Toast.LENGTH_LONG).show();
                    Snackbar.make(view, R.string.empty_fields_message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_point_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                showAbout();
                break;
            default:
                break;
        }
        return true;
    }

    private void initViews() {
        this.mTxtRouteName = (EditText) findViewById(R.id.routeEditText);
        this.mTvRouteDate = (TextView) findViewById(R.id.dateEditText);

        Calendar calendar = Calendar.getInstance();
        this.mSelectedYear = calendar.get(Calendar.YEAR);
        this.mSelectedMonth = calendar.get(Calendar.MONTH);
        this.mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDateUI();

//        this.mBtnChangeDate.setOnClickListener(this);
        this.mTvRouteDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateEditText:
                showDatePickerDialog(mSelectedYear, mSelectedMonth, mSelectedDay, mOnDateSetListener);
                break;
            default:
                break;
        }
    }

    private void updateDateUI() {
        String month = ((mSelectedMonth + 1) > 9) ? "" + (mSelectedMonth + 1) : "0" + (mSelectedMonth + 1);
        String day = ((mSelectedDay) < 10) ? "0" + mSelectedDay : "" + mSelectedDay;
        mTvRouteDate.setText(day + "." + month + "." + mSelectedYear);
    }

    private DatePickerDialog showDatePickerDialog(int initialYear, int initialMonth, int initialDay, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, initialYear, initialMonth, initialDay);
        dialog.show();
        return dialog;
    }

    protected void showAbout() {
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.app_icon);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteDao.close();
    }

    public void onStart() {
        super.onStart();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    protected void onResume() {
        super.onResume();
    }
}