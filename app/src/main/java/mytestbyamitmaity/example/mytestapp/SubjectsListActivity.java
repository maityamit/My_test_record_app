package mytestbyamitmaity.example.mytestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SubjectsListActivity extends AppCompatActivity {

    private String exam_id;
    private DatabaseReference RootRef;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_list);

        exam_id = getIntent().getExtras().get("EXAMID").toString();

        RootRef = FirebaseDatabase.getInstance ().getReference ().child("Class").child(exam_id).child("Subjects");

        progressDialog = new ProgressDialog( SubjectsListActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Check your Internet or Wi-fi Connection" );
        recyclerView = findViewById(R.id.Subject_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStart() {
        super.onStart ();

        progressDialog.show ();


        FirebaseRecyclerOptions<Subjects> options =
                new FirebaseRecyclerOptions.Builder<Subjects> ()
                        .setQuery ( RootRef,Subjects.class )
                        .build ();


        FirebaseRecyclerAdapter<Subjects, StudentViewHolder2> adapter =
                new FirebaseRecyclerAdapter<Subjects, StudentViewHolder2> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolder2 holder, final int position, @NonNull final Subjects model) {


                        holder.product_name.setText ( model.getName() );
                        progressDialog.dismiss();
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