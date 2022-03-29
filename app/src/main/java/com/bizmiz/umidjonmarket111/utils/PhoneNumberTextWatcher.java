package com.bizmiz.umidjonmarket111.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class PhoneNumberTextWatcher implements TextWatcher {

    private static final String TAG = PhoneNumberTextWatcher.class
            .getSimpleName();
    private EditText edTxt;
    private boolean isDelete;

    public PhoneNumberTextWatcher(EditText edTxtPhone) {
        this.edTxt = edTxtPhone;
        edTxt.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    isDelete = true;
                }
                return false;
            }
        });
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }
    @Override
    public void afterTextChanged(Editable s) {

        if (isDelete) {
            isDelete = false;
            return;
        }
        String val = s.toString();
        String a = "";
        String b = "";
        String c = "";
        String d = "";
        if (val.length() > 0) {
            val = val.replace("-", "");
            if (val.length() >= 2) {
                a = val.substring(0, 2);
            } else {
                val.length();
                a = val.substring(0, val.length());
            }
            if (val.length() >= 5) {
                b = val.substring(2, 5);
            } else {
                if (val.length() > 2) {
                    val.length();
                    b = val.substring(2, val.length());
                }
            }
            if (val.length() >= 7) {
                c = val.substring(5, 7);
            } else if (val.length() == 6) {
                c = val.substring(5, val.length());
            }
            if (val.length() >= 9) {
                d = val.substring(7, 9);
            } else if (val.length() == 8) {
                d = val.substring(7, val.length());
            }
            StringBuilder stringBuffer = new StringBuilder();
            if (a.length() > 0) {
                stringBuffer.append(a);
                if (a.length() == 2) {
                    stringBuffer.append("-");
                }
            }
            if (b.length() > 0) {
                stringBuffer.append(b);
                if (b.length() == 3) {
                    stringBuffer.append("-");
                }
            }
            if (c.length() > 0) {
                stringBuffer.append(c);
                if (c.length() == 2) {
                    stringBuffer.append("-");
                }
            }
            if (d.length() > 0) {
                stringBuffer.append(d);
            }
            edTxt.removeTextChangedListener(this);
            edTxt.setText(stringBuffer.toString());
            edTxt.setSelection(edTxt.getText().toString().length());
        } else {
            edTxt.removeTextChangedListener(this);
            edTxt.setText("");
        }
        edTxt.addTextChangedListener(this);

    }
}