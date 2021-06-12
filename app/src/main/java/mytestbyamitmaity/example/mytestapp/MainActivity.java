package mytestbyamitmaity.example.mytestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference RootRef,MainRef;
    private ProgressDialog progressDialog;
    private EditText editText;
    private ImageView button;
    private TextView client__name;
    private String currentUserID;
    private GifImageView imageView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        client__name = findViewById(R.id.client_name);
        imageView = findViewById(R.id.exam_name_load_image);



        mAuth = FirebaseAuth.getInstance ();
        currentUserID = mAuth.getCurrentUser ().getUid ();

        button = findViewById(R.id.input_exam_name_button);
        editText = findViewById(R.id.input_exam_name);


        progressDialog = new ProgressDialog( MainActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        RootRef = FirebaseDatabase.getInstance ().getReference ().child("Users").child(currentUserID).child("Exam");
        MainRef = FirebaseDatabase.getInstance ().getReference ();

        progressDialog.setMessage ( "Tips: Please Check your Internet or Wi-fi Connection" );

        recyclerView = findViewById(R.id.ExamListReyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        MainRef.child ( "Users" ).child ( currentUserID )
                .addValueEventListener ( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String retrieveUserNAme = dataSnapshot.child ( "Name" ).getValue ().toString ();




                        client__name.setText ( retrieveUserNAme );


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = editText.getText().toString();

                if (TextUtils.isEmpty(string)){
                    Toast.makeText(MainActivity.this, "Enter Test Name that will unique", Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateANode(string);
                }
            }
        });



    }

    private void CreateANode(String string) {


        Map<String,Object> updatee = new HashMap<>();
        updatee.put("Name",string);
        updatee.put("Subjects","");
        RootRef.child(string).updateChildren(updatee).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText (MainActivity.this, "done", Toast.LENGTH_SHORT ).show ();
                    editText.setText(null);

                    progressDialog.dismiss();

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error ! Send Again.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart ();


        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);



        FirebaseRecyclerOptions<ExamClass> options =
                new FirebaseRecyclerOptions.Builder<ExamClass> ()
                        .setQuery ( RootRef,ExamClass.class )
                        .build ();


        FirebaseRecyclerAdapter<ExamClass, StudentViewHolder2> adapter =
                new FirebaseRecyclerAdapter<ExamClass, StudentViewHolder2> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolder2 holder, final int position, @NonNull final ExamClass model) {


                        holder.product_name.setText ( model.getName());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent1 = new Intent(MainActivity.this,SubjectsListActivity.class);
                                intent1.putExtra("EXAMID",model.getName());

                                startActivity(intent1);
                            }
                        });




                    }

                    @NonNull
                    @Override
                    public StudentViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.exam_layout_list,viewGroup,false );
                        StudentViewHolder2 viewHolder  = new StudentViewHolder2(  view);
                        return viewHolder;

                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();


                        imageView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    }


                };
        recyclerView.setAdapter ( adapter );
        adapter.startListening ();




    }


    public static class StudentViewHolder2 extends  RecyclerView.ViewHolder
    {

        TextView product_name;
        public StudentViewHolder2(@NonNull View itemView) {
            super ( itemView );
            product_name = itemView.findViewById ( R.id.exam_layout_exam_name);
        }
    }
}