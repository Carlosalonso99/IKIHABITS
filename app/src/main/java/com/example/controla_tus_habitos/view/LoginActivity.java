package com.example.controla_tus_habitos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.services.UserManagerService;
import com.example.controla_tus_habitos.utils.AuthCallback;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerBtn = findViewById(R.id.registerBtn);

        Button loginBtn = findViewById(R.id.loginBtn);
        EditText emailEt = findViewById(R.id.editTextTextEmailAddress);
        EditText passwdEt = findViewById(R.id.editTextTextPassword);

        TextView emailTv = findViewById(R.id.emailLbl);
        TextView passwdTv = findViewById(R.id.passwdLbl);

        /**
         * TODO: tener en cuenta campos vacios y restringir la contraseña, expresion regular
         *
         */


        emailEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && ((EditText)v).getText().toString().trim().isEmpty()) {
                    hitnToLabel((EditText) v, emailTv);
                } else if (((EditText)v).getText().toString().trim().isEmpty()) {
                    labelToHint((EditText) v, emailTv);
                }
            }


        });
        passwdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && ((EditText)v).getText().toString().trim().isEmpty()) {
                    hitnToLabel((EditText) v, passwdTv);
                } else if (((EditText)v).getText().toString().isEmpty()) {
                    labelToHint((EditText) v, passwdTv);
                }
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwdEt.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    UserManagerService.login(email, password, new AuthCallback() {
                        @Override
                        public void onAuthSuccess() {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onAuthFailed() {
                            Toast.makeText(LoginActivity.this, "El registro no fue posible", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "El correo electrónico y la contraseña no pueden estar vacíos.", Toast.LENGTH_SHORT).show();
                }
            }

            ;
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEt.getText().toString().trim();
                String password = passwdEt.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    UserManagerService.register(email, password, new AuthCallback() {
                        @Override
                        public void onAuthSuccess() {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onAuthFailed() {
                            Toast.makeText(LoginActivity.this, "El registro no fue posible", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "El correo electrónico y la contraseña no pueden estar vacíos.", Toast.LENGTH_SHORT).show();
                }
            };
        });




    }

    private void labelToHint(EditText v, TextView emailTv) {
        v.setHint(emailTv.getText().toString().trim());
        emailTv.setText("");
    }

    private void hitnToLabel(EditText v, TextView lbl) {
        lbl.setText(v.getHint().toString().trim());
        v.setHint("");

    }


}