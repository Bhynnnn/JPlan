package com.example.jplan.Main;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jplan.Diary.DiaryFragment;
import com.example.jplan.Mypage.MypageFragment;
import com.example.jplan.Plan.PlanFragment;
import com.example.jplan.R;
import com.example.jplan.Today.TodayFragment;
import com.example.jplan.Todo.TodoFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    View navigation_header;
    ActionBarDrawerToggle drawerToggle;

    private Context context = this;

    // Firebase
    FirebaseAuth auth;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Toolbar toolbar;
    public TextView tb_title;
    public Button addBtn;
    private String[] arrStr = {"Today", "Plan", "Todo", "Diary","Mypage"};

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb_title = findViewById(R.id.tb_title);
        addBtn = findViewById(R.id.addBtn);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.BLACK);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.rgb(221, 234, 253));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        auth = FirebaseAuth.getInstance();
        drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );

        mDrawerLayout.addDrawerListener(drawerToggle);
//        navigationView.setNavigationItemSelectedListener(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigation_header = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.account:
                        break;
                    case R.id.setting:
//                        Toast.makeText(MainActivity.this, "개발자 이메일 : ", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Fragment Plan = new PlanFragment().newInstance();
        Fragment Today = new TodayFragment();
        Fragment Todo = new TodoFragment().newInstance();
        Fragment Mypage = new MypageFragment().newInstance();
        Fragment Diary = new DiaryFragment().newInstance();

        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(5);
        tabLayout = findViewById(R.id.tab_layout);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFrag(Today);
        viewPagerAdapter.addFrag(Plan);
        viewPagerAdapter.addFrag(Todo);
        viewPagerAdapter.addFrag(Diary);
        viewPagerAdapter.addFrag(Mypage);
        mViewPager.setUserInputEnabled(false);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(0, true);

        new TabLayoutMediator(tabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Log.d("MainActivity", String.valueOf(position));
//                tab.setIcon(arr[position]);
                tab.setText(arrStr[position]);


            }
        }).attach();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println("click drawerLayout");

        if (id == R.id.home) {
            System.out.println("open drawerLayout");

            mDrawerLayout.openDrawer(Gravity.LEFT);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}