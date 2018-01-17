package com.michalenko.olga.simpleinsta.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.michalenko.olga.simpleinsta.R;
import com.michalenko.olga.simpleinsta.extensions.LoginValidator;
import com.michalenko.olga.simpleinsta.home.MainActivity;
import com.michalenko.olga.simpleinsta.model.AlertMessage;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email_edit_text;
    private EditText password_edit_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_edit_text = (EditText)findViewById(R.id.email);
        password_edit_text = (EditText) findViewById(R.id.password);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    public void navigateToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class );
        startActivity(intent);
        this.finish();
    }

    public void login(View view) {
        mAuth = FirebaseAuth.getInstance();
        String password = password_edit_text.getText().toString();
        String email = email_edit_text.getText().toString();
        LoginValidator validator = new LoginValidator(email,password);
        AlertMessage message = validator.validateForm();

        if(message.getAlert()){
            Toast.makeText(this, message.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class );
                                startActivity(intent);
                               LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Не удалось войти! Проверьте данные.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}