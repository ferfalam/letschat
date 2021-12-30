package com.faridcodeur.letschat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.adapters.SurveyListAdapter;
import com.faridcodeur.letschat.databinding.FragmentSurveysBinding;
import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.utiles.Global;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SurveysFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurveysFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Surveys> surveys = new ArrayList<>();
    private SurveyListAdapter surveyListAdapter;
    private FragmentSurveysBinding binding;
    private static SurveysFragment surveysFragment;

    public SurveysFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static SurveysFragment newInstance() {
        if (surveysFragment==null){
            surveysFragment = new SurveysFragment();
        }
        return surveysFragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSurveysBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        getSurveys();
        buidCustomAdapter();

        return binding.getRoot();
    }

    private void buidCustomAdapter() {
        surveyListAdapter = new SurveyListAdapter(getContext(), surveys);
        binding.listSurveys.setAdapter(surveyListAdapter);
    }

    private void getSurveys(){
        //db.collection(Surveys.getCollectionPath()).document(documentSnapshot.getId()).delete();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Global.getSurveysCollectionPath())
                .orderBy("id", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            Surveys survey = documentSnapshot.toObject(Surveys.class);
                            surveys.add(survey);
                            surveyListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}