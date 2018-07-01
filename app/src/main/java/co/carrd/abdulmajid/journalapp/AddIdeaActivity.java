package co.carrd.abdulmajid.journalapp;

import android.arch.persistence.room.util.StringUtil;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.carrd.abdulmajid.journalapp.database.AppDatabase;
import co.carrd.abdulmajid.journalapp.model.Idea;
import co.carrd.abdulmajid.journalapp.util.AppExecutors;
import co.carrd.abdulmajid.journalapp.util.DateTimeUtils;

public class AddIdeaActivity extends AppCompatActivity {

    private TextInputEditText mTitle;
    private EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idea);

        mTitle = findViewById(R.id.et_idea_title);
        mDescription = findViewById(R.id.et_idea_description);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveIdea();
            }
        });
    }

    private void validateAndSaveIdea(){
        StringBuilder wordTagsBuilder = new StringBuilder();
        int count = 0;
        String seprator = "";
        String description = mDescription.getText().toString();
        String title = mTitle.getText().toString();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)){
            String[] actionWords = getResources().getStringArray(R.array.action_words);
            String[] ideaWords = description.split(" ");
            for (String ideaWord : ideaWords){
                Log.e("JournalApp", "Idea Word: "+ideaWord);
                for (String actionWord :actionWords){
                    Log.e("Journal App", "Action Word: "+actionWord);
                    if (ideaWord.contains(actionWord) && count < 3){
                        wordTagsBuilder.append(seprator);
                        wordTagsBuilder.append(actionWord);
                        seprator = ",";
                        count++;
                    }
                }
            }

            final Idea idea = new Idea(title, description, wordTagsBuilder.toString(), DateTimeUtils.todaysDate(), DateTimeUtils.todaysDate());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getDatabase(AddIdeaActivity.this).ideaModel().insert(idea);
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddIdeaActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            });
        }
    }

}
