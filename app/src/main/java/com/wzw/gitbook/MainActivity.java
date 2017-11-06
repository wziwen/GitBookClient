package com.wzw.gitbook;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wzw.gitbook.base.SingleFragmentActivity;
import com.wzw.gitbook.download.DownloadFragment;
import com.wzw.gitbook.entity.Lang;
import com.wzw.gitbook.fragment.ExploreFragment;
import com.wzw.gitbook.fragment.SearchFragment;
import com.wzw.gitbook.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExploreFragment exploreFragment = new ExploreFragment();
    DownloadFragment downloadFragment = new DownloadFragment();
    SettingFragment settingFragment = new SettingFragment();

    Fragment currentFragment;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, downloadFragment)
                .hide(downloadFragment)
                .add(R.id.fl_container, settingFragment)
                .hide(settingFragment)
                .add(R.id.fl_container, exploreFragment)
                .commit();
        currentFragment = exploreFragment;
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

    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.main, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("keyword", query);
                SingleFragmentActivity.show(MainActivity.this, SearchFragment.class, bundle);

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_lang_all) {
            exploreFragment.setLanguage(Lang.ALL);
            return true;
        }
        if (id == R.id.action_lang_chinese) {
            exploreFragment.setLanguage(Lang.CHINESE);
            return true;
        }
        if (id == R.id.action_lang_english) {
            exploreFragment.setLanguage(Lang.ENGLISH);
            return true;
        }
        if (id == R.id.action_lang_french) {
            exploreFragment.setLanguage(Lang.FRENCH);
            return true;
        }
        if (id == R.id.action_lang_spanish) {
            exploreFragment.setLanguage(Lang.SPANISH);
            return true;
        }
        if (id == R.id.action_lang_arabic) {
            exploreFragment.setLanguage(Lang.ARABIC);
            return true;
        }

        if (id == R.id.action_search) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_explore) {
            showFragment(exploreFragment);
        } else if (id == R.id.nav_download) {
            showFragment(downloadFragment);
        } else if (id == R.id.nav_feedback) {
            try {
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.my_email_address)});
                data.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_title));
                data.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_content));
                startActivity(data);
                startActivity(Intent.createChooser(data, getString(R.string.feedback_choose)));
            } catch (ActivityNotFoundException e) {
                View view = findViewById(R.id.toolbar);
                Snackbar.make(view, R.string.not_email_client, Snackbar.LENGTH_LONG).show();
            }
//            showFragment((settingFragment));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment) {
        if (fragment == currentFragment) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .hide(currentFragment)
                .show(fragment)
                .commit();
        currentFragment = fragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // re-layout root view
        getWindow().getDecorView().requestLayout();
    }
}
