package com.faridcodeur.letschat.survey.fragements;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.databinding.NewSurveyFragmentBinding;
import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.atomic.AtomicInteger;

public class NewSurveyFragment extends Fragment {

    private NewSurveyViewModel mViewModel;
    private NewSurveyFragmentBinding binding;

    public static NewSurveyFragment newInstance() {
        return new NewSurveyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewSurveyFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AtomicInteger ids = new AtomicInteger();

        LinearLayout layout = binding.surveyContentLayout;
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        ScrollView scrollView = binding.surveyContentLayoutScroll;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.textQuestion) {
                TextQuestion textQuestions = new TextQuestion(this, layout);
                textQuestions.setId(ids.getAndIncrement());
                layout.addView(textQuestions.getView());
                scrollView.fullScroll(View.FOCUS_DOWN);
            }else if (item.getItemId() == R.id.uniqueChoice){
                UniqueChoiceQuestion uniqueChoiceQuestion = new UniqueChoiceQuestion(this, layout);
                uniqueChoiceQuestion.setId(ids.getAndIncrement());
                layout.addView(uniqueChoiceQuestion.getView());
                scrollView.fullScroll(View.FOCUS_DOWN);
            }else if (item.getItemId() == R.id.multipleChoice){
                MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(this, layout);
                layout.addView(multipleChoiceQuestion.getView());
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
            return false;
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewSurveyViewModel.class);
        // TODO: Use the ViewModel
    }

}