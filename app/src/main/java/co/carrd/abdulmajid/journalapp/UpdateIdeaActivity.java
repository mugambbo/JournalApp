package co.carrd.abdulmajid.journalapp;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import co.carrd.abdulmajid.journalapp.database.AppDatabase;
import co.carrd.abdulmajid.journalapp.model.Idea;
import co.carrd.abdulmajid.journalapp.util.AppExecutors;
import co.carrd.abdulmajid.journalapp.util.DateTimeUtils;

public class UpdateIdeaActivity extends AppCompatActivity {

    private TextInputEditText mTitle;
    private EditText mDescription;
    Idea idea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_idea);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("idea")){
            idea = (Idea) bundle.getSerializable("idea");
        } else {
            Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
            finish();
        }

        mTitle = findViewById(R.id.et_idea_title);
        mDescription = findViewById(R.id.et_idea_description);

        mTitle.setText(idea.getTitle());
        mDescription.setText(idea.getDescription());

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
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
            List<String> actionWordList = Arrays.asList(actionWords);

            String[] ideaWords = description.split(" ");
            for (String ideaWord : ideaWords){
                if (actionWordList.contains(String.valueOf(ideaWord.charAt(0)).toUpperCase() + ideaWord.substring(1, ideaWord.length())) && count < 3){
                    wordTagsBuilder.append(seprator);
                    wordTagsBuilder.append(String.valueOf(ideaWord.charAt(0)).toUpperCase()).append(ideaWord.substring(1, ideaWord.length()));
                    seprator = ",";
                    count++;
                }
            }
            idea.setUpdatedAt(DateTimeUtils.todaysDate());
            idea.setTitle(title);
            idea.setDescription(description);
            idea.setWordTags(wordTagsBuilder.toString());

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getDatabase(UpdateIdeaActivity.this).ideaModel().update(idea);
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateIdeaActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            });
        }
    }
}
