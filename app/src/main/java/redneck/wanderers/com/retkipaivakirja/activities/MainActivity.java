package redneck.wanderers.com.retkipaivakirja.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import redneck.wanderers.com.retkipaivakirja.R;
import redneck.wanderers.com.retkipaivakirja.adapter.ListRoutesAdapter;
import redneck.wanderers.com.retkipaivakirja.dao.RouteDAO;
import redneck.wanderers.com.retkipaivakirja.model.Route;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String TAG = "HikingDiary";

    public static final int REQUEST_CODE_ADD_ROUTE = 40;
    public static final int REQUEST_CODE_IMPORT_DB = 41;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public static final String EXTRA_ADDED_ROUTE = "extra_key_added_route";

    private ListView mListViewRoutes;
    private TextView mTxtEmptyListRoutes;
    private View mLayout;

    private ListRoutesAdapter mAdapter;
    private List<Route> mListRoutes;
    private RouteDAO mRouteDao;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = this.getSharedPreferences("com.wanderers.redneck.hikingdiary", Context.MODE_PRIVATE);
        Log.e(TAG, "SP = " + prefs.getString("rw_language", null));
        if(prefs.getString("rw_language", null) == null){
            SharedPreferences.Editor edit  = prefs.edit();
            edit.putString("rw_language", "en");
            edit.apply();
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        } else {
            Locale locale = new Locale(prefs.getString("rw_language", null));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        initViews();

        String DB_FOLDER = "/Android/data/redneck.wanderers.com.retkipaivakirja/databases";
        File direct = new File(Environment.getExternalStorageDirectory().getPath() + DB_FOLDER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestStoragePermission();

        } else {
            if (!direct.exists()) {
                Log.e(TAG, DB_FOLDER + " ei ole olemassa");
                if (direct.mkdirs()) {
                    Log.d(TAG, DB_FOLDER + " LUOTU!!!");
                }
            }
        }

        mRouteDao = new RouteDAO(this);
        mListRoutes = mRouteDao.getAllRoutes();
        if(mListRoutes != null && !mListRoutes.isEmpty()) {
            mAdapter = new ListRoutesAdapter(this, mListRoutes);
            mListViewRoutes.setAdapter(mAdapter);
        }
        else {
            mTxtEmptyListRoutes.setVisibility(View.VISIBLE);
            mListViewRoutes.setVisibility(View.GONE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityAddRoute.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ROUTE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(MainActivity.this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_routes) {
            // Handle the camera action
        } else if (id == R.id.nav_pois) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, ActivitySettings.class);
            startActivityForResult(intent, REQUEST_CODE_IMPORT_DB);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void requestStoragePermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(mLayout, "Storage permission",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void initViews() {
        this.mListViewRoutes = (ListView) findViewById(R.id.lvTrips);
        this.mTxtEmptyListRoutes = (TextView) findViewById(R.id.txt_empty_list_routes);
        this.mListViewRoutes.setOnItemClickListener(this);
        this.mListViewRoutes.setOnItemLongClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ADD_ROUTE || requestCode == REQUEST_CODE_IMPORT_DB) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    Route createdRoute = (Route) data.getSerializableExtra(EXTRA_ADDED_ROUTE);
                    if(createdRoute != null) {
                        if(mListRoutes == null)
                            mListRoutes = new ArrayList<Route>();
                        mListRoutes.add(createdRoute);
                        if(mListViewRoutes.getVisibility() != View.VISIBLE) {
                            mListViewRoutes.setVisibility(View.VISIBLE);
                            mTxtEmptyListRoutes.setVisibility(View.GONE);
                        }
                        if(mAdapter == null) {
                            mAdapter = new ListRoutesAdapter(this, mListRoutes);
                            mListViewRoutes.setAdapter(mAdapter);
                        } else {
                            mAdapter.setItems(mListRoutes);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showDeleteDialogConfirmation(final Route clickedRoute) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete the \"" + clickedRoute.getName() + "\" route ?");

        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mRouteDao != null) {
                    mRouteDao.deleteRoute(clickedRoute);
                    mListRoutes.remove(clickedRoute);

                    if (mListRoutes.isEmpty()) {
                        mListRoutes = null;
                        mListViewRoutes.setVisibility(View.GONE);
                        mTxtEmptyListRoutes.setVisibility(View.VISIBLE);
                    }
                    mAdapter.setItems(mListRoutes);
                    mAdapter.notifyDataSetChanged();
                }

                dialog.dismiss();
                Toast.makeText(MainActivity.this, R.string.route_deleted_successfully, Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Route clickedRoute = mAdapter.getItem(position);
        Log.d(TAG, "clickedItem : " + clickedRoute.getName());
        Intent intent = new Intent(this, PointsActivity.class);
        intent.putExtra(PointsActivity.EXTRA_SELECTED_ROUTE_ID, clickedRoute.getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Route clickedRoute = mAdapter.getItem(position);
        Log.d(TAG, "longClickedItem : "+clickedRoute.getName());
        showDeleteDialogConfirmation(clickedRoute);
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        mRouteDao.close();
    }
}
