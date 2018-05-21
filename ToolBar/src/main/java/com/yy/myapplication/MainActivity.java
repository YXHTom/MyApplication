package com.yy.myapplication;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DefaultFragment.OnFragmentInteractionListener {
    private Toolbar toolbar;
    private SearchView mSearchView;
    private ViewPager mVpContent;
    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "toolbar", Toast.LENGTH_SHORT).show();
            }
        });
        mVpContent = findViewById(R.id.mVpContent);
        List<Fragment> list = new ArrayList<>();
        DefaultFragment defaultFragment = new DefaultFragment();
        list.add(defaultFragment);
        mSearchFragment = new SearchFragment();
        list.add(mSearchFragment);
        MainVPAdapter adapter = new MainVPAdapter(getSupportFragmentManager(), list);

        mVpContent.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint("提示内容");
        mSearchView.setIconified(true);
        SearchView.SearchAutoComplete mSearchAutoComplete = mSearchView.findViewById(R.id.search_src_text);
        //设置输入框内提示文字样式
        mSearchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));//设置提示文字颜色
        mSearchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));//设置内容文字颜色


        //mSearchView.setIconifiedByDefault(true);
        //关闭X图标
        mSearchView.onActionViewCollapsed();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchFragment.setSearchStr(query);
                //文字提交的时候哦回调，newText是最后提交搜索的文字
                Toast.makeText(MainActivity.this, "onQueryTextSubmit", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //在文字改变的时候回调，query是改变之后的文字

                mSearchFragment.setSearchStr(newText);
                Toast.makeText(MainActivity.this, "onQueryTextChange", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始搜索的时候，设置显示搜索页面
                mVpContent.setCurrentItem(1);
                Toast.makeText(MainActivity.this, "setOnSearchClickListener", Toast.LENGTH_SHORT).show();
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //关闭搜索按钮的时候，设置显示默认页面
                mVpContent.setCurrentItem(0);
                Toast.makeText(MainActivity.this, "setOnCloseListener", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cycling:
                Toast.makeText(this, "骑行", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_other:
                Toast.makeText(this, "其他", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class MainVPAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList;

        public MainVPAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}
