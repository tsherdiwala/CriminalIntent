package com.knoxpo.criminalintent.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.knoxpo.criminalintent.R;
import com.knoxpo.criminalintent.fragments.DetailFragment;
import com.knoxpo.criminalintent.models.Crime;
import com.knoxpo.criminalintent.models.CrimeLab;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by knoxpo on 28/11/16.
 */

public class PagerActivity extends AppCompatActivity {

    private static final String TAG = PagerActivity.class.getSimpleName();

    public static final String EXTRA_CRIME_ID = TAG + ".EXTRA_CRIME_ID";

    private ViewPager mViewPager;
    private Adapter mAdapter;

    private ArrayList<Crime> mCrimes;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        init();

        setSupportActionBar(mToolbar);

        mViewPager.setAdapter(mAdapter);

        int position = 0;

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        if(crimeId != null){
            Crime crime = CrimeLab.getInstance(this).getCrime(crimeId);
            position = mCrimes.indexOf(crime);
        }

        mViewPager.setCurrentItem(position);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setTitle(mCrimes.get(position).getTitle());
            }
        });

        setTitle(mCrimes.get(position).getTitle());

    }

    private void init() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mCrimes = CrimeLab.getInstance(this).getCrimes();
        mAdapter = new Adapter(getSupportFragmentManager());
    }


    private class Adapter extends FragmentStatePagerAdapter {


        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DetailFragment.newInstance(mCrimes.get(position).getId());
        }

        @Override
        public int getCount() {
            return mCrimes.size();
        }
    }
}
