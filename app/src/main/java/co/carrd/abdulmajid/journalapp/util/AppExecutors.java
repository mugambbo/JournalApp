package co.carrd.abdulmajid.journalapp.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Abdulmajid on 6/23/18.
 */

public class AppExecutors {
    private static AppExecutors sInstance;
    private Executor diskIO;
    private Executor networkIO;
    private Executor mainThread;
    private static final Object LOCK = new Object();

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread){
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance(){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO(){
        return diskIO;
    }

    public Executor networkIO(){
        return networkIO;
    }

    public Executor mainThread(){
        return mainThread;
    }

    private static class MainThreadExecutor  implements Executor {
        private Handler mainTheadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainTheadHandler.post(command);
        }
    }
}
