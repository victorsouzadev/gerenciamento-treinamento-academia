package br.com.victoor.treinamentos.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class ValidationFields {


    public static void ObserveEditToUpdateErrorAutoCompleteTextView(final TextInputLayout inputLayout, AutoCompleteTextView editTex) {
        editTex.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (inputLayout.getError() != null) {
                    inputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void fieldIsEmptyAutoCompleteTextView(final TextInputLayout inputLayout, AutoCompleteTextView editTex) {
        if (editTex.getText().toString().trim().isEmpty()) {
            inputLayout.setError("Campo vazio");
        }
    }

    public static void ObserveEditToUpdateError(final TextInputLayout inputLayout, EditText editTex) {
        editTex.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (inputLayout.getError() != null) {
                    inputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static boolean fieldIsEmpty(final TextInputLayout inputLayout, EditText editTex) {
        if (editTex.getText().toString().trim().isEmpty()) {
            inputLayout.setError("Campo vazio");
            return true;
        }
        return false;
    }
}
