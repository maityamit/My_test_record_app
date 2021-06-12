package mytestbyamitmaity.example.mytestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private Button submit;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private EditText email;
    private TextView confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth= FirebaseAuth.getInstance ();

        submit = findViewById(R.id.forgot_email_button);
        email = findViewById(R.id.forgot_email);
        confirm = findViewById(R.id.confirm_sent);

        progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setMessage ( "Tips: Please Cheak your Internet or Wi-fi Connection" );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setCancelable(false);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string = email.getText().toString();
                if (TextUtils.isEmpty(string))
                {
                    Toast.makeText(ForgotPassword.this, "Enter the Email Address..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HItCHEAK(string);
                }
            }
        });

    }

    private void HItCHEAK(String string1) {

        progressDialog.show();

        Toast.makeText(ForgotPassword.this, string1, Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(string1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressDialog.dismiss();
                        email.setText(null);
                        confirm.setVisibility(View.VISIBLE);
                        submit.setAlpha(0.5f);
                    }
                });
    }
}