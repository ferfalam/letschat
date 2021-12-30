package com.faridcodeur.letschat.utiles;

import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.annotations.NonNull;

public class InputValidation {

        public static boolean isEmptyInput(TextInputEditText textInputEditText, boolean mustContainNumber){
            if (mustContainNumber){
                boolean isvalid;
                try {
                    Double.parseDouble(Objects.requireNonNull(textInputEditText.getText()).toString());
                    isvalid = true;
                }catch (NumberFormatException e){
                    isvalid = false;
                }
                return Objects.requireNonNull(textInputEditText.getText()).toString().isEmpty() || textInputEditText.getText().toString().matches("^\\s+") || !isvalid;
            }else return Objects.requireNonNull(textInputEditText.getText()).toString().isEmpty() || textInputEditText.getText().toString().matches("^\\s+");

        }

        public static List<Map<String, String>> sortMapById(@NonNull List<Map<String, String>> mapList){
            List<Map<String, String>> list = new ArrayList<>();
            List<Integer> ids = new ArrayList<>();
            for (Map<String, String> question : mapList){
                ids.add(Integer.valueOf(Objects.requireNonNull(question.get("id"))));
            }
            Collections.sort(ids);
            for (int id : ids){
                for (Map<String, String> question : mapList){
                    if (Objects.equals(question.get("id"), String.valueOf(id))){
                        list.add(question);
                        break;
                    }
                }
            }
            return list;
        }
}
