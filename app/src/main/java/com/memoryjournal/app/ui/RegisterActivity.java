package com.memoryjournal.app.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.memoryjournal.app.R;
import com.memoryjournal.app.repository.AuthRepository;
import com.memoryjournal.app.repository.RepositoryCallback;

public class RegisterActivity extends AppCompatActivity {
    private final AuthRepository authRepository = new AuthRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner roleSpinner = findViewById(R.id.roleSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this,
            R.array.roles,
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        EditText displayName = findViewById(R.id.displayNameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> authRepository.register(
            emailInput.getText().toString(),
            passwordInput.getText().toString(),
            displayName.getText().toString(),
            roleSpinner.getSelectedItem().toString(),
            new RepositoryCallback<FirebaseUser>() {
                @Override
                public void onSuccess(FirebaseUser result) {
                    Toast.makeText(RegisterActivity.this, R.string.register, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(RegisterActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        ));
    }
}
