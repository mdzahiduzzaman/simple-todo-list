package com.example.thingstodo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.thingstodo.Database.DataBaseHelper;
import com.example.thingstodo.Model.ListModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewListItem extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewListItem";

    private EditText editText;
    private Button saveButton;
    private DataBaseHelper helper;

    public static AddNewListItem newInstance() {
        return new AddNewListItem();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_item_layout, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.et_add_item);
        editText.requestFocus();
        saveButton = view.findViewById(R.id.btn_save);
        helper = new DataBaseHelper(getActivity());

        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if(bundle != null) {
            isUpdate = true;
            String listItem = bundle.getString("listItem");
            editText.setText(listItem);

            if(listItem.length() > 0) {
                saveButton.setEnabled(true);
                saveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue));
            }
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }
        });

        final boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(v -> {
            String s = editText.getText().toString();
            if(finalIsUpdate) {
                helper.updateItem(bundle.getInt("id"), s);
            } else {
                ListModel listItem = new ListModel();
                listItem.setListItem(s);
                listItem.setCheckedStatus(0);
                helper.insertItem(listItem);
            }
            dismiss();
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
