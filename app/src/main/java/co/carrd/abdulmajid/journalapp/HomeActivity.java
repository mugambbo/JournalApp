package co.carrd.abdulmajid.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import co.carrd.abdulmajid.journalapp.database.AppDatabase;
import co.carrd.abdulmajid.journalapp.model.Idea;
import co.carrd.abdulmajid.journalapp.util.AppExecutors;

public class HomeActivity extends AppCompatActivity implements IdeasAdapter.OnIdeaClickListener {

    private FloatingActionButton mAddIdeasFab;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private IdeasAdapter mAdapter;
    private ViewModel viewModel;
    private TextView mEmptyList;
    private Handler handler;
    private Runnable runnable;
    private long refreshDelay = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddIdeasFab = (FloatingActionButton) findViewById(R.id.fab);
        mAddIdeasFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddIdeaActivity.class));
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ideas);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new IdeasAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyList = findViewById(R.id.tv_empty_list);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getDatabase(HomeActivity.this).ideaModel().delete(mAdapter.retrieveTask(viewHolder.getAdapterPosition()));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);
        setupViewModel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupHandler();
    }

    private void setupViewModel(){
        IdeaViewModel viewModel = ViewModelProviders.of(this).get(IdeaViewModel.class);
        viewModel.getIdeas().observe(this, new Observer<List<Idea>>() {
            @Override
            public void onChanged(@Nullable List<Idea> ideas) {
                mAdapter.setIdeas(ideas);
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (mAdapter.getItemCount() < 1){
            mEmptyList.setVisibility(View.VISIBLE);
        } else {
            mEmptyList.setVisibility(View.GONE);
        }
        mAdapter.updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, Idea idea) {
        Intent intent = new Intent(this, UpdateIdeaActivity.class);
        intent.putExtra("idea", idea);
        startActivity(intent);
    }

    private void setupHandler(){
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                updateUI();
                handler.postDelayed(this, refreshDelay);
            }
        };
        handler.postDelayed(runnable, refreshDelay);
    }

    private void stopHandler(){
        if (handler != null && runnable != null){
            handler.removeCallbacks(runnable);
        }
    }

}
