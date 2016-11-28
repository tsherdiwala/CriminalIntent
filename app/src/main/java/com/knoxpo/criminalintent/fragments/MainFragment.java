package com.knoxpo.criminalintent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.knoxpo.criminalintent.R;
import com.knoxpo.criminalintent.activities.DetailActivity;
import com.knoxpo.criminalintent.activities.PagerActivity;
import com.knoxpo.criminalintent.models.Crime;
import com.knoxpo.criminalintent.models.CrimeLab;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by knoxpo on 24/11/16.
 */
public class MainFragment extends Fragment {

    private static final int
            REQUEST_NEW_CRIME = 0,
            REQUEST_UPDATE_CRIME = 1,
            REQUEST_UPDATE_CRIME_NEW = 2;

    private RecyclerView mCrimesRV;
    private TextView mEmptyTV;
    private ArrayList<Crime> mCrimes;
    private CrimeAdapter mAdapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_crime) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            startActivityForResult(intent, REQUEST_NEW_CRIME);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        init(v);

        mCrimesRV.setAdapter(mAdapter);
        mCrimesRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    private void init(View v) {
        mCrimesRV = (RecyclerView) v.findViewById(R.id.rv_crimes);
        mEmptyTV = (TextView) v.findViewById(R.id.tv_empty);
        mCrimes = CrimeLab.getInstance(getActivity()).getCrimes();
        mAdapter = new CrimeAdapter();
    }

    private void updateUI() {
        mEmptyTV.setVisibility(mCrimes.isEmpty() ? View.VISIBLE : View.GONE);
        mCrimesRV.setVisibility(!mCrimes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_CRIME && resultCode == Activity.RESULT_OK && data != null) {
            UUID crimeId = (UUID) data.getSerializableExtra(DetailActivity.EXTRA_CRIME_ID);
            Crime crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
            mAdapter.notifyItemInserted(mCrimes.indexOf(crime));
            updateUI();
        }else if(requestCode == REQUEST_UPDATE_CRIME && resultCode == Activity.RESULT_OK && data != null){
            UUID crimeId = (UUID) data.getSerializableExtra(DetailActivity.EXTRA_CRIME_ID);
            Crime crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
            mAdapter.notifyItemChanged(mCrimes.indexOf(crime));
            updateUI();
        } else if(requestCode == REQUEST_UPDATE_CRIME_NEW){
            mAdapter.notifyDataSetChanged();
            updateUI();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeVH> {

        private LayoutInflater mInflater;

        public CrimeAdapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public CrimeVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mInflater.inflate(R.layout.item_crime, parent, false);
            return new CrimeVH(v);
        }

        @Override
        public void onBindViewHolder(CrimeVH holder, int position) {
            holder.bindCrime(mCrimes.get(position));
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private class CrimeVH extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener,
            View.OnClickListener {

        private TextView mTitleTV, mDateTV;
        private CheckBox mIsSolved;

        private Crime mCrime;

        public CrimeVH(View itemView) {
            super(itemView);
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_title);
            mDateTV = (TextView) itemView.findViewById(R.id.tv_date);
            mIsSolved = (CheckBox) itemView.findViewById(R.id.cb_solved);
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;

            mTitleTV.setText(crime.getTitle());

            Date date = crime.getDate();
            if (date == null) {
                mDateTV.setText(R.string.no_date);
                mDateTV.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
            } else {
                mDateTV.setText(date.toString());
                mDateTV.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }
            mIsSolved.setChecked(crime.isSolved());
            mIsSolved.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            mCrime.setSolved(b);
        }

        @Override
        public void onClick(View view) {
            /*Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
            detailIntent.putExtra(DetailActivity.EXTRA_CRIME_ID, mCrime.getId());
            detailIntent.putExtra(DetailActivity.EXTRA_POSITION,getAdapterPosition());
            startActivityForResult(detailIntent, REQUEST_UPDATE_CRIME);*/

            Intent pageIntent = new Intent(getActivity(), PagerActivity.class);
            pageIntent.putExtra(PagerActivity.EXTRA_CRIME_ID, mCrime.getId());
            startActivityForResult(pageIntent,REQUEST_UPDATE_CRIME_NEW);
        }
    }

}
