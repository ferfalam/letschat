package com.faridcodeur.letschat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.activity.MediaViewActivity;
import com.faridcodeur.letschat.adapters.GridViewCustomAdapter;
import com.faridcodeur.letschat.databinding.FragmentMediaBinding;

import java.util.ArrayList;

public class MediaFragment extends Fragment {
    ArrayList<String> images;
    FragmentMediaBinding fragmentMediaBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        images = getArguments().getStringArrayList("messages");
        fragmentMediaBinding = FragmentMediaBinding.inflate(inflater, container, false);
        GridViewCustomAdapter gridViewCustomAdapter = new GridViewCustomAdapter(getActivity().getApplicationContext(), images);
        fragmentMediaBinding.mediaGrid.setAdapter(gridViewCustomAdapter);
        fragmentMediaBinding.mediaGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =  new Intent(getActivity().getApplicationContext(), MediaViewActivity.class);
                intent.putExtra("extra1", images.get(position));
                startActivity(intent);
            }
        });
        return fragmentMediaBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}