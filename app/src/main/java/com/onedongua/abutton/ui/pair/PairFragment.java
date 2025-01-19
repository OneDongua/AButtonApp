package com.onedongua.abutton.ui.pair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.onedongua.abutton.databinding.FragmentPairBinding;

public class PairFragment extends Fragment {

    private FragmentPairBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PairViewModel pairViewModel =
                new ViewModelProvider(this).get(PairViewModel.class);

        binding = FragmentPairBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.text;
        pairViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}