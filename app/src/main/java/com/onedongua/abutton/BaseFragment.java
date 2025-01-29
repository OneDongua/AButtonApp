package com.onedongua.abutton;

import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public void print(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void print(@StringRes int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
