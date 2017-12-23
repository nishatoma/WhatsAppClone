package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity
{

  String activeUser = "";
  ArrayList<String> messages;
  ArrayAdapter<String> arrayAdapter;

  public void sendChat(View view)
  {
    final EditText chatText = (EditText) findViewById(R.id.chatEditText);

    if (!chatText.getText().toString().isEmpty())
    {
      final ParseObject message = new ParseObject("Message");

      message.put("sender", ParseUser.getCurrentUser().getUsername());
      message.put("recipient", activeUser);
      message.put("message", chatText.getText().toString());

      message.saveInBackground(new SaveCallback()
      {
        @Override
        public void done(ParseException e)
        {
          if (e == null)
          {
            messages.add(chatText.getText().toString());
            arrayAdapter.notifyDataSetChanged();
            chatText.setText("");
          }
        }
      });
    }



  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);
    Intent intent = getIntent();
    activeUser = intent.getStringExtra("username");
    setTitle(activeUser);
    ListView listView = (ListView) findViewById(R.id.chatListView);
    messages = new ArrayList<>();
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
    listView.setAdapter(arrayAdapter);

    ParseQuery<ParseObject> query1 = new ParseQuery<>("Message");
    query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
    query1.whereEqualTo("recipient", activeUser);

    ParseQuery<ParseObject> query2 = new ParseQuery<>("Message");
    query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
    query2.whereEqualTo("sender", activeUser);
    List<ParseQuery<ParseObject>> queries = new ArrayList<>();

    queries.add(query1);
    queries.add(query2);

    ParseQuery<ParseObject> query = ParseQuery.or(queries);
    query.orderByAscending("createdAt");
    query.findInBackground(new FindCallback<ParseObject>()
    {
      @Override
      public void done(List<ParseObject> objects, ParseException e)
      {
        if (objects.size() > 0 && e == null)
        {
          messages.clear();
          for (ParseObject msg : objects)
          {
            String msgContent = msg.getString("message");
            if (!msg.getString("sender").equals(ParseUser.getCurrentUser().getUsername()))
            {
              msgContent = "> " + msgContent;
            }

            messages.add(msgContent);
          }
          arrayAdapter.notifyDataSetChanged();
        }
      }
    });



  }
}
