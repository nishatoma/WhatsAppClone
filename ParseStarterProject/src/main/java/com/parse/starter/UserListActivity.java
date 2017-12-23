package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity
{

  ListView listView;
  ArrayList<String> users;
  ArrayAdapter<String> arrayAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_list);
    setTitle("Users");
    users = new ArrayList<>();

    listView = (ListView) findViewById(R.id.listViewUserActivity);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
      {
        //Jumpt to chat activity!
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("username", users.get(i));
        startActivity(intent);
      }
    });

    users.clear();

    arrayAdapter = new ArrayAdapter<>(UserListActivity.this, android.R.layout.simple_list_item_1, users);
    listView.setAdapter(arrayAdapter);

    ParseQuery<ParseUser> query = ParseUser.getQuery();
    query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
    query.findInBackground(new FindCallback<ParseUser>()
    {
      @Override
      public void done(List<ParseUser> objects, ParseException e)
      {
        if (objects.size() > 0 && e == null)
        {
          for (ParseUser user : objects)
          {
            users.add(user.getUsername());
          }
          arrayAdapter.notifyDataSetChanged();
        }
      }
    });


  }
}
