package org.nicknelson.roomwithaview;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    // TODO: Add paging to the app
    // TODO: Add swipe-to-delete
    // TODO: Remove checkbox transparent background and remove row selector so only cb is animated

    RecyclerView recyclerView;
    EditText editText;
    Button button;
    private WordViewModel mWordViewModel;
    int wordCount;
    DividerItemDecoration itemDecor;

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

        // instantiate RecyclerView divider
        itemDecor = new DividerItemDecoration(this, HORIZONTAL);

        final WordListAdapter adapter = new WordListAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        // add list divider
        //recyclerView.addItemDecoration(itemDecor);

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this, new Observer<List<WordEntity>>() {
            @Override
            public void onChanged(@Nullable final List<WordEntity> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
                wordCount = words.size();
            }
        });

        // add word when enter is pressed on keyboard
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()!=KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        buttonClick(null);
                        return true;
                    } }
                return false;
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
            word.setIsSelected(false);
            word.setCreateDate(new Date());

            mWordViewModel.insert(word);

        }
    }

    public void addWords(int amount) {
        WordEntity word;

        int i = 1;
        String wordStr;

        do {
            wordStr = String.format("%05d", i);
            word = new WordEntity();
            word.setWord(wordStr);
            word.setCreateDate(new Date());
            mWordViewModel.insert(word);
            i++;
        } while (i <= amount);
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
                addWords(500);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

        class WordViewHolder extends RecyclerView.ViewHolder {

            private final TextView wordItemView;
            private final CheckBox checkBox;
            private final LinearLayout viewForeground;

            private WordViewHolder(View itemView) {
                super(itemView);
                viewForeground = itemView.findViewById(R.id.viewForeground);
                wordItemView = itemView.findViewById(R.id.textView);
                checkBox = itemView.findViewById(R.id.checkBox);

                checkBox.setClickable(false);

                viewForeground.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        final WordEntity thisWord = mWords.get(getAdapterPosition());
                        Toast.makeText(MainActivity.this,
                                "You long-clicked: " + thisWord.getWord(),
                                Toast.LENGTH_LONG).show();

                        return true;
                    }
                });

                viewForeground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final WordEntity thisWord = mWords.get(getAdapterPosition());
                        Toast.makeText(MainActivity.this,
                                "You clicked: " + thisWord.getWord(),
                                Toast.LENGTH_LONG).show();
                    }
                });
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
                final WordEntity current = mWords.get(position);
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
