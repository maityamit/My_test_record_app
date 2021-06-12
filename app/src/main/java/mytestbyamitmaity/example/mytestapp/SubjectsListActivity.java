package mytestbyamitmaity.example.mytestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class SubjectsListActivity extends AppCompatActivity {

    private String exam_id;
    private DatabaseReference RootRef;
    private EditText editText;
    private ImageView button;
    private ProgressDialog progressDialog;
    private String currentUserID;
    private GifImageView imageView;
    FirebaseAuth mAuth;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_list);

        exam_id = getIntent().getExtras().get("EXAMID").toString();
        mAuth = FirebaseAuth.getInstance ();
        currentUserID = mAuth.getCurrentUser ().getUid ();


        button = findViewById(R.id.input_suvject_name_button);
        editText = findViewById(R.id.input_subject_name);


        imageView = findViewById(R.id.subject_name_load_image);
        RootRef = FirebaseDatabase.getInstance ().getReference ().child("Users").child(currentUserID).child("Exam").child(exam_id).child("Subjects");

        progressDialog = new ProgressDialog( SubjectsListActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Check your Internet or Wi-fi Connection" );
        recyclerView = findViewById(R.id.Subject_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = editText.getText().toString();

                if (TextUtils.isEmpty(string)){
                    Toast.makeText(SubjectsListActivity.this, "Enter Subject Name that will unique", Toast.LENGTH_SHORT).show();
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
        updatee.put("Marks/000000/Date","28/05/2021");
        updatee.put("Marks/000000/FM","50");
        updatee.put("Marks/000000/OM","50");
        updatee.put("Marks/000000/Title","Testing Purpose");
        RootRef.child(string).updateChildren(updatee).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText (SubjectsListActivity.this, "done", Toast.LENGTH_SHORT ).show ();
                    editText.setText(null);

                    progressDialog.dismiss();

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(SubjectsListActivity.this, "Error ! Send Again.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    @Override
    public void onStart() {
        super.onStart ();


        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);


        FirebaseRecyclerOptions<Subjects> options =
                new FirebaseRecyclerOptions.Builder<Subjects> ()
                        .setQuery ( RootRef,Subjects.class )
                        .build ();


        FirebaseRecyclerAdapter<Subjects, StudentViewHolder2> adapter =
                new FirebaseRecyclerAdapter<Subjects, StudentViewHolder2> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolder2 holder, final int position, @NonNull final Subjects model) {


                        holder.product_name.setText ( model.getName() );
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String string = getRef(position).getKey();
                                Intent intent1 = new Intent(SubjectsListActivity.this,MarksListActivity.class);
                                intent1.putExtra("EXAMID",exam_id);
                                intent1.putExtra("SUBID",string);
                                startActivity(intent1);
                            }
                        });




                    }

                    @NonNull
                    @Override
                    public StudentViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.subcategory_layout,viewGroup,false );
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
            product_name = itemView.findViewById ( R.id.sub_category_textview);
        }
    }
}