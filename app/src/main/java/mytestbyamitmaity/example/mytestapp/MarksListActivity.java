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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class MarksListActivity extends AppCompatActivity {


    String sub_id,exma_id;
    private DatabaseReference RootRef;
    private ProgressDialog progressDialog;
    private TextView avr_marks,next_good;
    double total = 0,i=0,average=0;
    private EditText in1,in2,in3;
    private String currentUserID;
    FirebaseAuth mAuth;
    private RelativeLayout in4;
    String SaveCurrentData;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_list);

        sub_id = getIntent().getExtras().get("SUBID").toString();
        exma_id = getIntent().getExtras().get("EXAMID").toString();
        next_good  = findViewById(R.id.next_good);
        mAuth = FirebaseAuth.getInstance ();
        currentUserID = mAuth.getCurrentUser ().getUid ();

        in1 = findViewById(R.id.input_test_name);
        in2 = findViewById(R.id.input_om);
        in3 = findViewById(R.id.input_fm);
        in4 = findViewById(R.id.input_button);



        RootRef = FirebaseDatabase.getInstance ().getReference ().child("Users").child(currentUserID).child("Exam").child(exma_id).child("Subjects").child(sub_id).child("Marks");

        avr_marks= findViewById(R.id.average_marks);
        progressDialog = new ProgressDialog( MarksListActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Check your Internet or Wi-fi Connection" );
        recyclerView = findViewById(R.id.marks_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        in4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance ();


                SimpleDateFormat currentdate = new SimpleDateFormat ( "dd/MM/yyyy" );
                SaveCurrentData = currentdate.format ( calendar.getTime () );
                progressDialog.show();
                String key = RootRef.push().getKey();
                String string_title = in1.getText().toString();
                String string_om = in2.getText().toString();
                String string_fm = in3.getText().toString();

                if (TextUtils.isEmpty(key))
                {
                    Toast.makeText(MarksListActivity.this, "Error +", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(string_title))
                {
                    Toast.makeText(MarksListActivity.this, "Error +", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(string_om))
                {
                    Toast.makeText(MarksListActivity.this, "Error +", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(string_fm))
                {
                    Toast.makeText(MarksListActivity.this, "Error +", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String,Object> updatee = new HashMap<>();
                    updatee.put("Title",string_title);
                    updatee.put("OM",string_om);
                    updatee.put("FM",string_fm);
                    updatee.put("Date",SaveCurrentData);

                    RootRef.child(key).updateChildren(updatee).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                in1.setText(null);
                                in2.setText(null);
                                in3.setText(null);

                                Toast.makeText(MarksListActivity.this ,"Done..", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(MarksListActivity.this, "Error ! Send Again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }



            }
        });


    }



    @Override
    public void onStart() {
        super.onStart ();

        progressDialog.show ();


        FirebaseRecyclerOptions<Marks> options =
                new FirebaseRecyclerOptions.Builder<Marks> ()
                        .setQuery ( RootRef,Marks.class )
                        .build ();


        FirebaseRecyclerAdapter<Marks, StudentViewHolder2> adapter =
                new FirebaseRecyclerAdapter<Marks, StudentViewHolder2> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolder2 holder, final int position, @NonNull final Marks model) {

                        recyclerView.smoothScrollToPosition(getItemCount());


                        DecimalFormat df = new DecimalFormat("0.00");
                        holder.title.setText(model.getTitle());
                        holder.om.setText("OM: "+model.getOM());
                        holder.fm.setText("FM: "+model.getFM());
                        holder.date.setText(model.getDate());


                        float total_marks  = Float.parseFloat(model.getFM());
                        float get_marks  = Float.parseFloat(model.getOM());

                        float percen = ((float)get_marks/total_marks)*100;

                        total = total+percen;
                        i=i+1;
                        average = (float)total/i;
                        avr_marks.setText(String.valueOf(df.format(average))+"% ge");


                        holder.percentage.setText(String.valueOf(df.format(percen))+"%");

                        float out_of_300 = 3*percen;

                        next_good.setText(String.valueOf(df.format(out_of_300)));


                        progressDialog.dismiss();




                    }

                    @NonNull
                    @Override
                    public StudentViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.exam_layout,viewGroup,false );
                       StudentViewHolder2 viewHolder  = new StudentViewHolder2(  view);
                        return viewHolder;

                    }
                };
        recyclerView.setAdapter ( adapter );
        adapter.startListening ();




    }


    public static class StudentViewHolder2 extends  RecyclerView.ViewHolder
    {

        TextView title,om,fm,date,percentage;
        public StudentViewHolder2(@NonNull View itemView) {
            super ( itemView );
            title = itemView.findViewById ( R.id.title_marks);
            om = itemView.findViewById ( R.id.om_marks);
            fm = itemView.findViewById ( R.id.fm_marks);
            date = itemView.findViewById ( R.id.date_marks);
            percentage = itemView.findViewById ( R.id.per_marks);


        }
    }
}