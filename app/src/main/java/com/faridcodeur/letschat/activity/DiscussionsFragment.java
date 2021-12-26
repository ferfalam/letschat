package com.faridcodeur.letschat.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.adapters.DiscussionListAdapter;
import com.faridcodeur.letschat.databinding.FragmentDiscussionsBinding;
import com.faridcodeur.letschat.entities.Discussion;
import com.faridcodeur.letschat.entities.Surveys;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscussionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscussionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Discussion> discussions = new ArrayList<>();
    private DiscussionListAdapter discussionListAdapter;
    private FragmentDiscussionsBinding binding;
    private static DiscussionsFragment discussionsFragment;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscussionsFragment() {
    }

    public static DiscussionsFragment newInstance() {
        if (discussionsFragment==null){
            discussionsFragment = new DiscussionsFragment();
        }
        return discussionsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiscussionsBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        generateDiscussions();
        buildCustomAdapter();

        return binding.getRoot();
    }

    private void buildCustomAdapter() {
        discussionListAdapter = new DiscussionListAdapter(getContext(), discussions);
        binding.listDiscussions.setAdapter(discussionListAdapter);
    }

    private void generateDiscussions(){
        for (int i=0; i<=13; i++) {
            discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5min"));
        }
    }

    public FragmentDiscussionsBinding getBinding() {
        return binding;
    }
}