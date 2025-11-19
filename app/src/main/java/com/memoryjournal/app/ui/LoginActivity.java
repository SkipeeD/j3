package com.memoryjournal.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.memoryjournal.app.R;
import com.memoryjournal.app.repository.AuthRepository;
import com.memoryjournal.app.repository.RepositoryCallback;

public class LoginActivity extends AppCompatActivity {
    private final AuthRepository authRepository = new AuthRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(v -> authRepository.login(
            emailInput.getText().toString(),
            passwordInput.getText().toString(),
            new RepositoryCallback<FirebaseUser>() {
                @Override
                public void onSuccess(FirebaseUser result) {
                    Toast.makeText(LoginActivity.this, R.string.login, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, CareProfileActivity.class));
                    finish();
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        ));

        registerButton.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}
