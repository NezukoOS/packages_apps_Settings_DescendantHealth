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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.settings.R;

public class DescendantHealth extends Fragment implements View.OnClickListener {

    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
            mContext = getActivity().getApplicationContext();
            getActivity().setTitle("Descendant Health");
            View view = inflater.inflate(R.layout.descendant_health, container, false);
            //Add views
            if (view != null) {
            }
            //add listeners
            //cardview2.setOnClickListener(this)
            return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {}
    }

}
