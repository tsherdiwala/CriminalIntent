package com.knoxpo.criminalintent.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by knoxpo on 24/11/16.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    public static CrimeLab getInstance(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimeLab;
    }

    private ArrayList<Crime> mCrimes;
    private Context mAppContext;

    private CrimeLab(Context context){
        mAppContext = context.getApplicationContext();
        mCrimes = new ArrayList<>();
    }

    public void addCrime(Crime crime){
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime){
        mCrimes.remove(crime);
    }

    public Crime getCrime(UUID crimeId){
        for(int i=0;i<mCrimes.size();i++){
            Crime crime = mCrimes.get(i);
            if(crime.getId().equals(crimeId)){
                return crime;
            }
        }
        return null;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }
}
