package com.github.khangtran.keystoresecuredemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.tntkhang.keystore_secure.KeystoreSecure;

public class MainActivity extends AppCompatActivity {
    private TextView tvValue1;
    private EditText edtInput1;
    private Button btnDecrypt1;
    private Button btnEncypt1;
    private TextView tvValue2;
    private EditText edtInput2;
    private Button btnDecrypt2;
    private Button btnEncypt2;

    private static final String STORE_KEY_1 = "STORE_KEY_1";
    private static final String STORE_KEY_2 = "STORE_KEY_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeystoreSecure.init(getApplicationContext());

        tvValue1 = findViewById(R.id.tv_value_1);
        btnDecrypt1 = findViewById(R.id.btn_get_1);
        btnEncypt1 = findViewById(R.id.btn_save_1);
        edtInput1 = findViewById(R.id.edt_input_1);

        tvValue2 = findViewById(R.id.tv_value_2);
        btnDecrypt2 = findViewById(R.id.btn_get_2);
        btnEncypt2 = findViewById(R.id.btn_save_2);
        edtInput2 = findViewById(R.id.edt_input_2);

        btnDecrypt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = KeystoreSecure.decrypt(STORE_KEY_1);
                tvValue1.setText(key);
            }
        });
        btnEncypt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeystoreSecure.encrypt(getApplicationContext(), STORE_KEY_1, edtInput1.getText().toString());
            }
        });
        btnDecrypt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = KeystoreSecure.decrypt(STORE_KEY_2);
                tvValue2.setText(key);
            }
        });
        btnEncypt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeystoreSecure.encrypt(getApplicationContext(), STORE_KEY_2, edtInput2.getText().toString());
            }
        });
    }
}
