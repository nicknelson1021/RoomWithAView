package org.nicknelson.roomwithaview;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "WordEntity")
@TypeConverters(DateConverter.class)
public class WordEntity {

    @PrimaryKey(autoGenerate = true)
    private int mWordId;

    @NonNull
    private String mWord;

    @NonNull
    public Date mCreateDate;

    public int getWordId() {
        return mWordId;
    }

    public void setWordId(int wordId) {
        this.mWordId = wordId;
    }

    @NonNull
    public String getWord() {
        return mWord;
    }

    public void setWord(@NonNull String word) {
        this.mWord = word;
    }

    @NonNull
    public Date getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(@NonNull Date createDate) {
        this.mCreateDate = createDate;
    }

}
