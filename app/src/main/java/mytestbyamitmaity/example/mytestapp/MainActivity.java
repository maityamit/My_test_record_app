package mytestbyamitmaity.example.mytestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CardView b,j,ja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=findViewById(R.id.boards);
        j=findViewById(R.id.jeemain);
        ja=findViewById(R.id.jeeadvanced);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,SubjectsListActivity.class);
                intent1.putExtra("EXAMID","BOARDS");
                startActivity(intent1);
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,SubjectsListActivity.class);
                intent1.putExtra("EXAMID","JEEM");
                startActivity(intent1);
            }
        });
        ja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,SubjectsListActivity.class);
                intent1.putExtra("EXAMID","JEEA");
                startActivity(intent1);
            }
        });
    }
}