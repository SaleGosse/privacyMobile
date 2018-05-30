package textotex.textotex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int mCurFragment = 0;
    private SecureClass sClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        sClass = new SecureClass(this, sharedPref);

        checkSession();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setCurFragment(new convListFragment());

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
    }

    private boolean checkSession() {
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPref.edit();

        //Resetting the isLogged boolean to check everything
        editor.putBoolean(getString(R.string.is_logged_key), false);
        editor.commit();

        //Getting the cookie
        final String cookie = sharedPref.getString(getString(R.string.cookie_key), "null");

        //If no cookie, put isLogged/cookie to "null"/false and start Logging Activity
        if(cookie.compareTo("null") == 0) {
            editor.putString(getString(R.string.cookie_key), "null");
            editor.commit();

            this.finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

            return false;
        }

        //Getting the login, if no, same
        final String login = sharedPref.getString(getString(R.string.login_key), "null");
        if(login.compareTo("null") == 0){
            editor.putString(getString(R.string.cookie_key), "null");
            editor.commit();

            this.finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

            return false;
        }

        //The request queue..
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(!sharedPref.getBoolean(getString(R.string.is_logged_key), false)) {
                    MainActivity.this.finish();
                   startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });

        //The request..
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_check_cookie),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("true")){
                            editor.putBoolean(getString(R.string.is_logged_key), true);
                            editor.commit();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", login);
                params.put("cookie", cookie);
                return params;
            }

        };

        queue.add(stringRequest);

        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.bt_home) {
            if(mCurFragment != R.id.nav_conv_list){
                setCurFragment(new convListFragment());
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
