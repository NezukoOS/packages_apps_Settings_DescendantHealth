/*
 * Copyright (C) 2019 Descendant
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.descendant.health;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settings.R;

import java.util.Arrays;
import java.util.Random;

public class DescendantHealth extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int CORRECT_LENGTH = 7;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    public Context mContext;

    private boolean mDataError;
    private boolean mIsComparingView;
    private boolean mSleepSwitchStatus;
    private boolean mScreenSwitchStatus;

    private ImageView mCriticalCasesIcon;
    private ImageView mInfoCovid;
    private ImageView mTodayCasesIcon;
    private ImageView mTodayDeathsIcon;
    private ImageView mTotalCasesIcon;
    private ImageView mUpdateCovid;

    private String[] mCOVIDDataArray;
    private Switch mSleepNotif;
    private Switch mScreenOnNotif;

    private TextView mCovidDataSectionTitle;

    private TextView mTodayCasesTitle;
    private TextView mTodayDeathsTitle;
    private TextView mTotalCasesTitle;
    private TextView mCriticalCasesTitle;
    private TextView mTodayCasesNumber;
    private TextView mTodayDeathsNumber;
    private TextView mTotalCasesNumber;
    private TextView mCriticalCasesNumber;
    private TextView mCovidDataObtained;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
            mIsComparingView = false;
            mContext = getActivity().getApplicationContext();
            getActivity().setTitle("Descendant Health");
            //gather settings
            View view = inflater.inflate(R.layout.descendant_health, container, false);
            //Add views
            if (view != null) {
                mSleepNotif = (Switch) view.findViewById(R.id.first_switch);
                mScreenOnNotif =  (Switch) view.findViewById(R.id.second_switch);
                mInfoCovid = view.findViewById(R.id.info_covid);
                mUpdateCovid = view.findViewById(R.id.update_covid);
                mCovidDataSectionTitle = view.findViewById(R.id.covid_19_data);
                mTodayCasesNumber = view.findViewById(R.id.cases_number);
                mTodayDeathsNumber = view.findViewById(R.id.today_deaths_number);
                mTotalCasesNumber = view.findViewById(R.id.total_cases_number);
                mCriticalCasesNumber = view.findViewById(R.id.critical_cases_number);
                mTodayCasesTitle= view.findViewById(R.id.today_cases_text);
                mTodayDeathsTitle = view.findViewById(R.id.today_deaths_text);
                mTotalCasesTitle = view.findViewById(R.id.total_cases_text);
                mCriticalCasesTitle = view.findViewById(R.id.critical_cases_text);
                mCovidDataObtained = view.findViewById(R.id.covid_19_data_obtained);
                mTodayCasesIcon = view.findViewById(R.id.today_cases_icon);
                mTodayDeathsIcon = view.findViewById(R.id.today_deaths_icon);
                mTotalCasesIcon = view.findViewById(R.id.total_cases_icon);
                mCriticalCasesIcon = view.findViewById(R.id.critical_cases_icon);
            }

            //add listeners
            mSleepNotif.setOnCheckedChangeListener(this);
            mUpdateCovid.setOnClickListener(this);
            //mInfoCovid.setOnClickListener(this);
            setupCOVIDView();
            setCreateSwitchStatus();
            return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mUpdateCovid) {
            mUpdateCovid.setRotation(0);
            mUpdateCovid.animate().rotation(360).setDuration(500).withStartAction(new Runnable() {
                @Override
                public void run() {
                    mCovidDataObtained.setText("Updating...");
                }
            }).withEndAction(new Runnable() {
                @Override
                public void run() {
                    setupCOVIDView();
                    if (!mDataError) {
                        mCovidDataObtained.setText("Following data has been updated " + mCOVIDDataArray[6]);
                    } else {
                        if (mCovidDataObtained.getVisibility() == View.VISIBLE)
                            mCovidDataObtained.setVisibility(View.INVISIBLE);
                    }
                }
            }).start();
        }
        /*if (v == mInfoCovid) {
            Log.d("Dil3mm4", "iscomparisonshowable:  " + String.valueOf(isComparisonShowable()) + " " + "mIsComparingView: " + String.valueOf(mIsComparingView));
            if (isComparisonShowable() && !mIsComparingView) {
                Log.d("Dil3mm4", "comparing launched");
                swapIcons("comparing");
                fillData("comparing");
            } else {
                Log.d("Dil3mm4", "datacovid launched");
                swapIcons("datacovid");
                fillData("datacovid");
            }
        }*/
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mSleepNotif) {
            mSleepNotif.setChecked(isChecked);
            updateSwitchStatus(mSleepNotif, isChecked);
        }
        if (buttonView == mScreenOnNotif) {
            mScreenOnNotif.setChecked(isChecked);
            updateSwitchStatus(mScreenOnNotif, isChecked);
        }
    }

    private void setCreateSwitchStatus() {
        mScreenSwitchStatus = "1".equals(Settings.System.getString(mContext.getContentResolver(), "descendant_health_screen_on").trim());
        mSleepSwitchStatus = "1".equals(Settings.System.getString(mContext.getContentResolver(), "descendant_health_sleep").trim());
        mSleepNotif.setChecked(mSleepSwitchStatus);
        mScreenOnNotif.setChecked(mScreenSwitchStatus);
    }

    private void updateSwitchStatus(View v, boolean b) {
        if (v == mSleepNotif)
            Settings.System.putIntForUser(mContext.getContentResolver(),"descendant_health_sleep", b ? 1 : 0, UserHandle.USER_CURRENT);
        if (v == mScreenOnNotif)
            Settings.System.putIntForUser(mContext.getContentResolver(),"descendant_health_screen_on", b ? 1 : 0, UserHandle.USER_CURRENT);
    }

    private void setupCOVIDView() {
        Random r = new Random();
        Settings.System.putIntForUser(mContext.getContentResolver(),"covid_signaled_update", r.nextInt() ,UserHandle.USER_CURRENT);
        swapIcons("datacovid");
        fillData("datacovid");
    }

    private void setupComparingView() {
        swapIcons("comparing");
        fillData("comparing");
    }

    private void swapIcons(String state) {
        String status = state;
        switch (status) {
            case "datacovid":
                mIsComparingView = false;
                mTodayCasesIcon.setImageResource(R.drawable.ic_today);
                mTodayDeathsIcon.setImageResource(R.drawable.ic_deaths);
                mTotalCasesIcon.setImageResource(R.drawable.ic_total_cases);
                mCriticalCasesIcon.setImageResource(R.drawable.ic_hospital);
                break;
            /*case "comparing":
                mIsComparingView = true;
                mTodayCasesIcon.setImageResource(R.drawable.ic_tests);
                mTodayDeathsIcon.setImageResource(R.drawable.ic_deaths);
                mTotalCasesIcon.setImageResource(R.drawable.ic_total_cases);
                mCriticalCasesIcon.setImageResource(R.drawable.ic_recovered);
                break;*/
            default:
        }
    }

    private void fillData(String state) {
        String covidData = Settings.System.getString(mContext.getContentResolver(), "covid_data");
        mCOVIDDataArray = covidData.split("]");
        mDataError = mCOVIDDataArray.length != CORRECT_LENGTH;
        Log.d("Dil3mm4", "1st mdataerror: " + String.valueOf(mDataError) + " " + "array len " + mCOVIDDataArray.length);
        if (!mDataError) {
            int pos = Arrays.asList(mCOVIDDataArray).indexOf("-255");
            if (pos > -1 && pos < 4) {
                mDataError = true;
                Log.d("Dil3mm4", "2st mdataerror: " + String.valueOf(mDataError));
            } else {
                mDataError = false;
                Log.d("Dil3mm4", "2st mdataerror: " + String.valueOf(mDataError));
            }
        }
        switch (state) {
            /*case "comparing":
                Log.d("Dil3mm4", "JOINED COMPARING WITH FILLDATA");
                mTodayCasesTitle.setText("Tests increment");
                mTodayDeathsTitle.setText("Death increment");
                mTotalCasesTitle.setText("Cases increment");
                mCriticalCasesTitle.setText("Recovered");
                mCovidDataObtained.setText("Comparison data between today and yesterday");
                mTodayCasesNumber.setText(mCOVIDDataArray[7]);
                mTodayDeathsNumber.setText(mCOVIDDataArray[8]);
                mTotalCasesNumber.setText(mCOVIDDataArray[9]);
                mCriticalCasesNumber.setText(mCOVIDDataArray[10]);
                break;*/
            case "datacovid":
                Log.d("Dil3mm4", "JOINED DATA COVID WITH FILLDATA");
                mTodayCasesTitle.setText("Cases today");
                mTodayDeathsTitle.setText("Deaths today");
                mTotalCasesTitle.setText("Total cases");
                mCriticalCasesTitle.setText("Critical cases");
                mCovidDataObtained.setVisibility(mDataError ? View.INVISIBLE : View.VISIBLE);
                if (mCovidDataObtained.getVisibility() == View.VISIBLE) {
                    mCovidDataObtained.setText("Following data were obtained on " + mCOVIDDataArray[6]);
                }
                mCovidDataSectionTitle.setText(mDataError ? "COVID-19 Data is unavailable" : "COVID-19 Data in " + mCOVIDDataArray[4]);
                mTodayCasesNumber.setText(mDataError ?  "No data" : mCOVIDDataArray[0]);
                mTodayDeathsNumber.setText(mDataError ?  "No data" : mCOVIDDataArray[1]);
                mTotalCasesNumber.setText(mDataError ?  "No data" : mCOVIDDataArray[2]);
                mCriticalCasesNumber.setText(mDataError ?  "No data" : mCOVIDDataArray[3]);
                break;
            default:
        }
    }


    /*private boolean isComparisonShowable() {
        String covidData = Settings.System.getString(mContext.getContentResolver(), "covid_data");
        mCOVIDDataArray = covidData.split("]");
        mDataError = mCOVIDDataArray.length != CORRECT_LENGTH;

        if (!mDataError) {
            int pos = Arrays.asList(mCOVIDDataArray).indexOf("-255");
            if (pos > 6) {
                Log.d("Dil3mm4", "comparison isn't showable");
                return false;
            } else {
                return true;
            }
        }
        return mDataError;
    }*/

}
