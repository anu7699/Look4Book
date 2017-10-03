package com.wink.anu.look4book;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import static com.wink.anu.look4book.QueryUtils.LOG_TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private  static final String query1="https://www.googleapis.com/books/v1/volumes?q=";
    private  static final String query2="&maxResults=20";
    private  static String query="";


    private ListView bookList;
    private CustomAdapter adapter;
    private static String Title="Look4Book";
    private ProgressBar loading_spinner;
    private TextView empty_text;
    private LoaderManager loaderManager;
    private boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.this.setTitle(Title);
        bookList=(ListView)findViewById(R.id.list);
        adapter=new CustomAdapter(this,new ArrayList<Book>());
        loading_spinner=(ProgressBar)findViewById(R.id.loading_spinner);
        loading_spinner.setVisibility(View.INVISIBLE);
        empty_text=(TextView)findViewById(R.id.empty_text_view);
        empty_text.setText("Search something");
        bookList.setEmptyView(empty_text);
        bookList.setAdapter(adapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri=Uri.parse(adapter.getItem(position).getUrl());
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(uri);
                startActivity(i);

            }
        });
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE) ;
        NetworkInfo info=cm.getActiveNetworkInfo();
        isConnected=info!=null&&info.isConnectedOrConnecting();
        loaderManager=getLoaderManager();
        Fresco.initialize(MainActivity.this);

       // loaderManager.initLoader(0,null,MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list,menu);
        final MenuItem search=menu.findItem(R.id.action_search);
        final SearchView searchView=(SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                if(input.equals(""))
                    Toast.makeText(getApplicationContext(),"Please enter a keyword to search",Toast.LENGTH_LONG).show();
                else
                {
                    query=query1+input+query2;
                    empty_text.setText("");
                    loading_spinner.setVisibility(View.VISIBLE);
                    if(isConnected)
                    {
                        loaderManager.restartLoader(0,null,MainActivity.this);


                    }
                    else {
                        loading_spinner.setVisibility(View.GONE);
                        empty_text.setText("No internet connection");
                    }

                }
                searchView.clearFocus();
                searchView.setQuery("",false);
                searchView.setIconified(true);
                search.collapseActionView();
                Title=input;
                MainActivity.this.setTitle(input);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               /*if(!TextUtils.isEmpty(newText))
                   query=query1+newText+query2;
                else
                    query="";
                getLoaderManager().restartLoader(0,null,MainActivity.this);*/
                return false;}
        });
        return true;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this,query);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        Log.d(LOG_TAG,"finished loading");
        loading_spinner.setVisibility(View.GONE);

        adapter.clear();
        if(!data.isEmpty()&&data!=null)
        {
            adapter.addAll(data);
        }
        empty_text.setText("Nothing to display");
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
