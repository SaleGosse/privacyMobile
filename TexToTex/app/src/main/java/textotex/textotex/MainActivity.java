package textotex.textotex;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int mCurFragment = 0;
    private Context mContext;
    private SecureClass sClass;
    private int mUserID;
    private String mCookie;

    private cookieManager cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mContext = this;

        cm = new cookieManager(this);

        cm.checkSession();

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        sClass = new SecureClass(this, sharedPref);

        this.mUserID = sharedPref.getInt(getString(R.string.user_id_key), -1);
        this.mCookie = sharedPref.getString(getString(R.string.cookie_key), "null");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setCurFragment(new convListFragment(this.mUserID, this.mCookie));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Logging out...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.is_logged_key), false);
                editor.apply();

                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        startService(new Intent(this.mContext, NotificationService.class));

    }

    private ServiceConnection connectService() {
        return null;
    }


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

    public void beginService() {
        startService(new Intent(MainActivity.this, NotificationService.class));
    }

    public void endService() {
        stopService(new Intent(MainActivity.this, NotificationService.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.bt_home) {
            if(mCurFragment != R.id.nav_conv_list){
                setCurFragment(new convListFragment(this.mUserID, this.mCookie));
                mCurFragment = R.id.nav_conv_list;
            }

            return true;
        }
        else if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void setCurFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

   // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile && id != mCurFragment) {
            setCurFragment(new profileFragment());

            mCurFragment = id;
        } else if (id == R.id.nav_add_conv && id != mCurFragment) {
            setCurFragment(new addConvFragment());

            mCurFragment = id;
        } else if (id == R.id.nav_add_team_conv && id != mCurFragment) {
            setCurFragment(new addTeamConvFragment());
            mCurFragment = id;

            mCurFragment = id;
        } else if (id == R.id.nav_about && id != mCurFragment) {
            setCurFragment(new aboutFragment());

            mCurFragment = id;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}



/* TODO:
-Notification

*/

