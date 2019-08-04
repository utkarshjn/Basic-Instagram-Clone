package com.utkarsh.myinstagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.security.Key;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

    EditText usernameEdit;
    EditText passwordEdit;
    Button signupButton;
    TextView switchButton;
    RelativeLayout backgroundLayout;
    ImageView logoImageView;

    Boolean signUpmodeActive=true;

    public void showUserList(){
        Intent intent=new Intent(getApplicationContext(),UserActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        //After entering password if we epress the enter key it automatically leads to sign up
        if(i== KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            signUp(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.switchButton){
            if(signUpmodeActive){
                signUpmodeActive=false;
                signupButton.setText("LOGIN");
                switchButton.setText("Or, Sign Up");

            }else{
                signUpmodeActive=true;
                signupButton.setText("SIGN UP");
                switchButton.setText("Or, Login");
            }
        }else if(view.getId()==R.id.backgroundLayout || view.getId()==R.id.logoImageView){
            //To hide the keyboard by pressing either in the backgroud or on the image
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void signUp(View view){
        if(usernameEdit.getText().toString().matches("") || passwordEdit.getText().toString().matches("")) {
            Toast.makeText(this,"A username and password are required",Toast.LENGTH_LONG).show();

        }else {

            if(signUpmodeActive) {

                ParseUser user = new ParseUser();
                user.setUsername(usernameEdit.getText().toString());
                user.setPassword(passwordEdit.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Sign Up: ", "Successful");
                            Toast.makeText(MainActivity.this,"Sign Up Successful",Toast.LENGTH_LONG).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                ParseUser.logInInBackground(usernameEdit.getText().toString(), passwordEdit.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null){
                            Log.i("Sign Up: ","Login successful");
                            Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                            showUserList();
                        }
                        else {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEdit=(EditText) findViewById(R.id.usernameEdit);
        passwordEdit=(EditText) findViewById(R.id.passwordEdit);
        signupButton=(Button) findViewById(R.id.signupButton);
        switchButton=(TextView) findViewById(R.id.switchButton);
        backgroundLayout=(RelativeLayout) findViewById(R.id.backgroundLayout);
        logoImageView=(ImageView) findViewById(R.id.logoImageView);

        passwordEdit.setOnKeyListener(this);

        switchButton.setOnClickListener(this);

        backgroundLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}
