package co.carrd.abdulmajid.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import co.carrd.abdulmajid.journalapp.database.AppDatabase;
import co.carrd.abdulmajid.journalapp.model.Idea;

/**
 * Created by Abdulmajid on 7/1/18.
 */

public class IdeaViewModel extends AndroidViewModel {
    private LiveData<List<Idea>> ideas;

    public IdeaViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(this.getApplication());
        ideas = database.ideaModel().getIdeas();

    }

    LiveData<List<Idea>> getIdeas(){
        return ideas;
    }


}
