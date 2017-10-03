package com.wink.anu.look4book;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by WELLCOME on 31-07-2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    String url;
    public BookLoader(Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        return QueryUtils.fetchBookList(url);
    }
}
