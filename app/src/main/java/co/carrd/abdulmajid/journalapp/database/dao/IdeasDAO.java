package co.carrd.abdulmajid.journalapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import co.carrd.abdulmajid.journalapp.model.Idea;

/**
 * Created by Abdulmajid on 6/30/18.
 */

@Dao
public interface IdeasDAO {

    @Query("SELECT * FROM Idea")
    public LiveData<List<Idea>> getIdeas();

    @Query("SELECT * FROM Idea WHERE :id")
    public List<Idea> getIdea(String id);

    @Insert
    public void insert(Idea... ideas);

    @Update
    public void update(Idea... ideas);

    @Delete
    public void delete(Idea idea);
}
