package com.faridcodeur.letschat.ui.auth;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.databinding.FragmentFirst2Binding;
import com.faridcodeur.letschat.ui.AuthCallback;

public class PhoneDefineFragment extends Fragment {

    private FragmentFirst2Binding binding;
    private String phoneNum;
    private AuthCallback authCallback;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirst2Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNum = binding.phoneNumber.getText().toString();
                if(phoneNum.trim().isEmpty()){
                    Toast.makeText(getContext(), "Le numero de telephone est obligatoire", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(authCallback!=null)
                    authCallback.sendMessage(phoneNum);
//                NavHostFragment.findNavController(PhoneDefineFragment.this)
//                        .navigate(R.id.action_First2Fragment_to_Second2Fragment);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AuthCallback) {
            authCallback = (AuthCallback) context;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}