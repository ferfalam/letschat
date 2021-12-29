package com.faridcodeur.letschat.utiles;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

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
}
