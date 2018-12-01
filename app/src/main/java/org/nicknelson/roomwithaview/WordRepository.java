package org.nicknelson.roomwithaview;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<WordEntity>> mAllWords;

    WordRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    LiveData<List<WordEntity>> getAllWords() {
        return mAllWords;
    }

    public void insert (WordEntity word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<WordEntity, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WordEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
