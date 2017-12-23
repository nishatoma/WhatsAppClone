/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity
{

  boolean loginModeActive = false;

  public void redirectIfLoggedIn()
  {
    if (ParseUser.getCurrentUser() != null)
    {
      Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
      startActivity(intent);
    }
  }

  public void toggleLoginMode(View v)
  {
    Button button = (Button) findViewById(R.id.signUpButton);
    TextView loginOrSignUp = (TextView) findViewById(R.id.toggleLogIn);
    if (loginModeActive){
      loginModeActive = false;
      button.setText("Sign up");
      loginOrSignUp.setText("Or, Login");

    } else {
      loginModeActive = true;
      button.setText("Log In");
      loginOrSignUp.setText("Or, sign up");
    }
  }

  public void signUpLogin(View v)
  {
    EditText userName = (EditText) findViewById(R.id.userName);
    EditText userPass = (EditText) findViewById(R.id.userPass);

    if (loginModeActive)
    {
      //login
      ParseUser.logInInBackground(userName.getText().toString(),
      userPass.getText().toString(), new LogInCallback()
      {
        @Override
        public void done(ParseUser user, ParseException e)
        {
          if (e == null)
          {
            Log.i("Log", "User logged in");
            redirectIfLoggedIn();
          } else
          {
            displayErrorMessage(e);
          }
        }
      });

    } else
    {
      ParseUser user = new ParseUser();
      user.setUsername(userName.getText().toString());
      user.setPassword(userPass.getText().toString());
      user.signUpInBackground(new SignUpCallback()
      {
        @Override
        public void done(ParseException e)
        {
          if (e == null)
          {
            Log.i("Sign Up", "Successful");
            redirectIfLoggedIn();
          } else
          {
            displayErrorMessage(e);
          }
        }
      });
    }
  }

  public void displayErrorMessage(Exception e)
  {
    String msg = e.getMessage();
    msg = msg.toLowerCase().contains("java") ? e.getMessage().substring(e.getMessage().indexOf(" ")) : msg;
    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Whats App");
    redirectIfLoggedIn();


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}