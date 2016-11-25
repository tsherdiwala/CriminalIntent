package com.knoxpo.criminalintent.activities;

import android.support.v4.app.Fragment;

import com.knoxpo.criminalintent.fragments.MainFragment;

/**
 * Created by knoxpo on 24/11/16.
 */

public class MainActivity extends ToolbarActivity {
    @Override
    public Fragment getContentFragment() {
        return new MainFragment();
    }
}
