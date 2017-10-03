package com.wink.anu.look4book;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WELLCOME on 31-07-2017.
 */

public class CustomAdapter extends ArrayAdapter<Book> {

    public CustomAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context,0,books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView =convertView;
        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Book currentBook=getItem(position);
        Uri uri = Uri.parse(currentBook.getImgUrl());
        SimpleDraweeView draweeView = (SimpleDraweeView)listItemView.findViewById(R.id.thumbnail);
        draweeView.setImageURI(uri);
        TextView tv_author=(TextView)listItemView.findViewById(R.id.tv_author);
        tv_author.setText(currentBook.getAuthor());
        TextView tv_title=(TextView)listItemView.findViewById(R.id.tv_title);
        tv_title.setText(currentBook.getTitle());

        return listItemView;
    }
}
