package com.example.examinations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.examinations.Model.Answer;
import com.example.examinations.Model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView t1_question,timerTxt;
    TextView email,name,phone;
    Button b1;
    EditText ans;
    int total=0;
    int correct=0,wrong=0;
    DatabaseReference reference;
    DatabaseReference answers;
    FirebaseAuth fAuth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            t1_question = (TextView) findViewById(R.id.questionTxt);
            timerTxt = (TextView) findViewById(R.id.timerTxt);
            b1 = (Button) findViewById(R.id.btn1);
            ans = (EditText)findViewById(R.id.Answer);
//            String bias=fAuth.getUid();
           answers=FirebaseDatabase.getInstance().getReference().child("users").child("answers");

            updateQuestion();
            reverseTimer(500, timerTxt);

        }
        private void updateQuestion ()
        {

            total++;
            ans.clearComposingText();
            if (total >8) {
                Intent i = new Intent(MainActivity.this, ResultActivity.class);
                i.putExtra("total", String.valueOf(total));
                i.putExtra("correct", String.valueOf(correct));
                i.putExtra("wrong", String.valueOf(wrong));
                startActivity(i);
            } else {
                reference = FirebaseDatabase.getInstance().getReference().child("Questions").child(String.valueOf(total));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Question question = dataSnapshot.getValue(Question.class);
                        t1_question.setText(question.getQuestion());


                        b1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String val = ans.getText().toString();
                                String id1=answers.push().getKey();
                                Answer answer=new Answer(val);
                                answers.child(id1).setValue(answer);
                                
                                if (val.equals(question.getAnswer())) {

                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            correct++;
                                            ans.setText("");
                                            updateQuestion();
                                        }
                                    }, 1500);
//                                    val="";

                                } else {
                                    wrong++;
//                                    Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
                                    ans.setText("");
                                    updateQuestion();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        public void reverseTimer ( int seconds, final TextView textView)
        {
            new CountDownTimer(seconds * 1000 + 1000, 1000) {
                @Override
                public void onTick(long l) {
                    int seconds = (int) (l / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    textView.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                }

                @Override
                public void onFinish() {

                    textView.setText("Time Up");
                    Intent myIntent = new Intent(MainActivity.this, ResultActivity.class);
                    myIntent.putExtra("total", String.valueOf(total));
                    myIntent.putExtra("correct", String.valueOf(correct));
                    myIntent.putExtra("incorrect", String.valueOf(wrong));
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),Register.class));
                    finish();

                }
            }.start();

        }
}

