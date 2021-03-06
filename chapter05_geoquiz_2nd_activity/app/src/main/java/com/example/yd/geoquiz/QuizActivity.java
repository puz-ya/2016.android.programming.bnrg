package com.example.yd.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created on 16.11.2016
 @author Puzino Yury.
 */

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    public static Toast toast;

    private TextView mTextView;
    private Button mNextButton;
    private Button mPrevButton;

    /* Logs levels:
    * ERROR     Log.e
    * WARNING   Log.w
    * INFO      Log.i
    * DEBUG     Log.d
    * VERBOSE   Log.v   only for developers
    */
    private static final String TAG = "QuizAct";
    //index for saving in Bundle
    private static final String KEY_INDEX = "index";
    //tag for intent cheat
    private int REQUEST_CHEATCODE = 0;
    private boolean mIsCheater;
    public static final String ANSWER_INDEX = "cheating_was_made";

    //неправильно, но пока так
    private final Question[] mQuestions = new Question[]{
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set log
        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //check if we changed configuration of the phone
        if (savedInstanceState != null) {
            //save current question ID
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            //save flag if user cheated
            mIsCheater = savedInstanceState.getBoolean(ANSWER_INDEX, false);
            //and we can remember id of cheated question
            //but PY is lazy today :\
        }

        //getting text from array and insert into textview
        //comment to java.lang.RuntimeException
        mTextView = (TextView) findViewById(R.id.question_textview);
        updateQuestion();

        //buttons
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Скоро тут будет код!
                //toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //for false button
                //toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(false);
            }
        });

        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                //update value
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton = (Button)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1);
                if(mCurrentIndex == -1){
                    mCurrentIndex = mQuestions.length - 1;
                }
                updateQuestion();
            }
        });

        //adding listener to textview
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
            }
        });

        //new cheat button (show answer)
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting CheatActivity
                //explicit Intent
                //old one: Intent intent = new Intent(QuizActivity.this, CheatActivity.class);

                boolean bAnswer = mQuestions[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.cheatingIntent(QuizActivity.this, bAnswer);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CHEATCODE);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    //set new question
    private void updateQuestion(){

        //logging + creating new exception
        //Log.d(TAG, "Update question text #" + mCurrentIndex, new Exception());

        int question = mQuestions[mCurrentIndex].getTextResId();
        mTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if(mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{

            if(userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CHEATCODE){
            if(intent == null){
                return;
            }
            mIsCheater = intent.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //saving our index from death
    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        Log.i(TAG, "OnSaveInstanceState");
        bundle.putInt(KEY_INDEX, mCurrentIndex);
        bundle.putBoolean(ANSWER_INDEX, mIsCheater);
    }
}
