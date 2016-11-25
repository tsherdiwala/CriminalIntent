package com.knoxpo.criminalintent.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.knoxpo.criminalintent.R;
import com.knoxpo.criminalintent.models.Crime;
import com.knoxpo.criminalintent.models.CrimeLab;

import java.util.Date;
import java.util.UUID;

/**
 * Created by knoxpo on 24/11/16.
 */
public class DetailFragment extends Fragment implements TextWatcher, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String
            TAG = DetailFragment.class.getSimpleName(),
            ARGS_CRIME_ID = TAG + ".ARGS_CRIME_ID",
            DATE_DIALOG_TAG = TAG + ".DATE_DIALOG_TAG";

    private static final int REQUEST_SELECT_DATE = 0;

    public static DetailFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID, crimeId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Crime mCrime;
    private EditText mTitleET;
    private CheckBox mSolvedCB;
    private Button mDateBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        init(v);

        mDateBtn.setOnClickListener(this);
        mTitleET.addTextChangedListener(this);
        mSolvedCB.setOnCheckedChangeListener(this);

        updateUI();

        return v;
    }

    private void init(View v) {
        Bundle args = getArguments();
        if (args != null && args.getSerializable(ARGS_CRIME_ID) != null) {
            mCrime =
                    CrimeLab.getInstance(getActivity())
                            .getCrime((UUID) args.getSerializable(ARGS_CRIME_ID));
        } else {
            mCrime = new Crime();
        }

        mTitleET = (EditText) v.findViewById(R.id.et_title);
        mDateBtn = (Button) v.findViewById(R.id.btn_choose_date);
        mSolvedCB = (CheckBox) v.findViewById(R.id.cb_solved);
    }

    private void updateUI(){
        mSolvedCB.setChecked(mCrime.isSolved());
        mTitleET.setText(mCrime.getTitle());

        Date date = mCrime.getDate();
        if(date!=null){
            mDateBtn.setText(date.toString());
        }
    }

    public Crime getCrime() {
        return mCrime;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mCrime.setTitle(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        DatePickerFragment fragment = DatePickerFragment.newInstance(mCrime.getDate());
        fragment.setTargetFragment(this, REQUEST_SELECT_DATE);
        fragment.show(getFragmentManager(), DATE_DIALOG_TAG);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCrime.setSolved(b);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_DATE && data != null) {
            Date date = (Date) data.getSerializableExtra(Intent.EXTRA_RETURN_RESULT);
            mCrime.setDate(date);
            updateUI();
        }
    }
}
