package com.faridcodeur.letschat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.adapters.DiscussionListAdapter;
import com.faridcodeur.letschat.databinding.FragmentDiscussionsBinding;
import com.faridcodeur.letschat.entities.Discussion;
import com.faridcodeur.letschat.entities.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private FirebaseFirestore database;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = user.getUid();

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
        database = FirebaseFirestore.getInstance();
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
        database.collection(Discussion.collectionPath)
                .whereEqualTo("senderId", userId)
                .orderBy("lastTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snap: task.getResult()
                            ) {
                                Discussion discussion = snap.toObject(Discussion.class);
                                discussions.add(discussion);
                                discussionListAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
        for (int i=0; i<=13; i++) {
            //discussions.add(new Discussion(userId, "Ariel", new Message(userId, "Hi", 2, ""), "ID", "ff", "hier"));
        }
    }

    public FragmentDiscussionsBinding getBinding() {
        return binding;
    }
}