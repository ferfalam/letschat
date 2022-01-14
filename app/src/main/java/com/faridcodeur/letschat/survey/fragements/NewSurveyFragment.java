package com.faridcodeur.letschat.survey.fragements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.databinding.NewSurveyFragmentBinding;
import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;
import com.faridcodeur.letschat.utiles.InputValidation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class NewSurveyFragment extends Fragment {

    private NewSurveyViewModel mViewModel;
    private NewSurveyFragmentBinding binding;
    private static NewSurveyFragment newSurveyFragment;
    private List<TextQuestion> textQuestionList = new ArrayList<>();
    private List<UniqueChoiceQuestion> uniqueChoiceQuestionList = new ArrayList<>();
    private List<MultipleChoiceQuestion> multipleChoiceQuestionList = new ArrayList<>();

    public static NewSurveyFragment newInstance() {
        if (newSurveyFragment==null){
            newSurveyFragment = new NewSurveyFragment();
        }
        return newSurveyFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewSurveyFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout layout = binding.surveyContentLayout;
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        ScrollView scrollView = binding.surveyContentLayoutScroll;

        AtomicInteger ids = new AtomicInteger();
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.textQuestion) {
                TextQuestion textQuestions = new TextQuestion(ids.getAndIncrement(), this, layout, textQuestionList);
                layout.addView(textQuestions.getView());
                textQuestionList.add(textQuestions);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }else if (item.getItemId() == R.id.uniqueChoice){
                UniqueChoiceQuestion uniqueChoiceQuestion = new UniqueChoiceQuestion(ids.getAndIncrement(), this, layout, uniqueChoiceQuestionList);
                layout.addView(uniqueChoiceQuestion.getView());
                uniqueChoiceQuestionList.add(uniqueChoiceQuestion);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }else if (item.getItemId() == R.id.multipleChoice){
                MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(ids.getAndIncrement(),this, layout, multipleChoiceQuestionList);
                layout.addView(multipleChoiceQuestion.getView());
                multipleChoiceQuestionList.add(multipleChoiceQuestion);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }else if (item.getItemId() == R.id.validate){
                if (InputValidation.isEmptyInput(binding.surveyTitle, false)){
                    binding.surveyTitle.setError("Aucun titre renseigner");
                }else if (InputValidation.isEmptyInput(binding.surveyDescription, false)){
                    binding.surveyDescription.setError("Aucune description renseigner");
                }else {
                    Surveys surveys = new Surveys(Objects.requireNonNull(binding.surveyTitle.getText()).toString(), Objects.requireNonNull(binding.surveyDescription.getText()).toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (mViewModel.createSurveys(surveys, textQuestionList, uniqueChoiceQuestionList, multipleChoiceQuestionList)){
                        Toast.makeText(getContext(), "Votre sondage a été créer avec succès", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                }
            }
            return true;
        });

        binding.newSurveyReturnButton.setOnClickListener(view1 -> requireActivity().finish());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewSurveyViewModel.class);
        // TODO: Use the ViewModel
    }

}