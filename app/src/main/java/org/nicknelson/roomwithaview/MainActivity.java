package org.nicknelson.roomwithaview;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // TODO: Wire up the "add 500 words" option
    // TODO: Add paging to the app
    // TODO: Keyboard enter button should add word as well as pressing "add" button

    RecyclerView recyclerView;
    EditText editText;
    Button button;
    private WordViewModel mWordViewModel;
    int wordCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize variables
        wordCount = 0;

        // initialize widgets
        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.edit_text);
        button = findViewById(R.id.add_button);

        final WordListAdapter adapter = new WordListAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this, new Observer<List<WordEntity>>() {
            @Override
            public void onChanged(@Nullable final List<WordEntity> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
                wordCount = words.size();
            }
        });

    }

    public void buttonClick(View view) {

        if (TextUtils.isEmpty(editText.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.toast,
                    Toast.LENGTH_LONG).show();
        } else {

            WordEntity word = new WordEntity();
            String wordStr = editText.getText().toString();
            editText.setText("");

            word.setWord(wordStr);
            word.setCreateDate(new Date());

            mWordViewModel.insert(word);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // override options menu and inflate layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_overflow, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (wordCount > 0) {
            menu.findItem(R.id.home_clear).setVisible(true);
        } else {
            menu.findItem(R.id.home_clear).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_clear:
                mWordViewModel.deleteAll();
                break;
            case R.id.home_add:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

        class WordViewHolder extends RecyclerView.ViewHolder {
            private final TextView wordItemView;

            private WordViewHolder(View itemView) {
                super(itemView);
                wordItemView = itemView.findViewById(R.id.textView);
            }
        }

        private final LayoutInflater mInflater;
        private List<WordEntity> mWords; // Cached copy of words

        WordListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

        @Override
        public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
            return new WordViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(WordViewHolder holder, int position) {
            if (mWords != null) {
                WordEntity current = mWords.get(position);
                holder.wordItemView.setText(current.getWord());
            } else {
                // Covers the case of data not being ready yet.
                holder.wordItemView.setText("No Word");
            }
        }

        void setWords(List<WordEntity> words){
            mWords = words;
            notifyDataSetChanged();
        }

        // getItemCount() is called many times, and when it is first called,
        // mWords has not been updated (means initially, it's null, and we can't return null).
        @Override
        public int getItemCount() {
            if (mWords != null)
                return mWords.size();
            else return 0;
        }
    }
}
