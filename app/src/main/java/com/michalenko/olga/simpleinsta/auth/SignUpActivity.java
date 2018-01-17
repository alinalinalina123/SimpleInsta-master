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
import com.michalenko.olga.simpleinsta.R;
import com.michalenko.olga.simpleinsta.extensions.LoginValidator;
import com.michalenko.olga.simpleinsta.home.MainActivity;
import com.michalenko.olga.simpleinsta.model.AlertMessage;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email_edit_text;
    private EditText password_edit_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_edit_text = (EditText)findViewById(R.id.emailSign);
        password_edit_text = (EditText) findViewById(R.id.passwordSign);
    }

    public void signUp(View view) {
        String password = password_edit_text.getText().toString();
        String email = email_edit_text.getText().toString();
        LoginValidator validator = new LoginValidator(email,password);
        AlertMessage message = validator.validateForm();

        if(message.getAlert()){
            Toast.makeText(this, message.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class );
                                startActivity(intent);
                                SignUpActivity.this.finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Не удалось создать аккаунт, попробуйте позже.", Toast.LENGTH_SHORT).show();}
                        }
                    });
        }
    }
}