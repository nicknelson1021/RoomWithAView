package org.nicknelson.roomwithaview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize widgets
        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.edit_text);
        button = findViewById(R.id.add_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button was clicked
            }
        });

        final WordListAdapter adapter = new WordListAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
