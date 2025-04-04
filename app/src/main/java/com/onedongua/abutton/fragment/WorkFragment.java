package com.onedongua.abutton.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.onedongua.abutton.databinding.FragmentWorkBinding;

public class WorkFragment extends Fragment {
    private FragmentWorkBinding binding;
    private RecyclerView workRecycler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        workRecycler = binding.workRecycler;

        // Initialize RecyclerView and set adapter here

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}