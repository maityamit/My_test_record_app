package mytestbyamitmaity.example.mytestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SpalshActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference RootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        mAuth = FirebaseAuth.getInstance ();
        RootRef= FirebaseDatabase.getInstance ().getReference ();

    }

    @Override
    protected void onStart() {


        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser ();

        if (currentUser == null)
        {

            SendUserToLoginActivity();
        }
        else
        {
            currentUserID = mAuth.getCurrentUser ().getUid ();


            Intent loginIntentt = new Intent ( SpalshActivity.this,MainActivity.class );
            loginIntentt.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( loginIntentt );
            finish ();

        }
    }








    private void SendUserToLoginActivity() {


        Intent loginIntent = new Intent (SpalshActivity.this,LoginActivity.class );
        loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( loginIntent );
        finish ();
    }
}