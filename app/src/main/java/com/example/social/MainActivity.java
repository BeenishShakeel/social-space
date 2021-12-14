package com.example.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView createnewAccount;
    EditText inputEmail, inputPassword;
    Button btnLogin;
    String emailPattern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    GoogleSignInAccount mGoogleSignInClient;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createnewAccount = findViewById(R.id.CreateNewAccount);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnlogin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        createnewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformLogin();
            }
        });

    }

    private void PerformLogin() {
        String Email = inputEmail.getText().toString();
        String Password = inputPassword.getText().toString();
        if (!Email.matches(emailPattern)) {
            inputEmail.setError("Enter valid Email");
        } else if (Password.isEmpty() || Password.length() < 6) {
            inputPassword.setError("Enter Correct Password");
        } else {
            progressDialog.setMessage("logging...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this,"Successfully logged in", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, ""+task.getException(),Toast.LENGTH_SHORT).show();
                    }


                }

            });

        }
    }
    private void sendUserToNextActivity()
    {
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}