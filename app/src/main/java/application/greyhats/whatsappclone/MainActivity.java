package application.greyhats.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    int login_signup_flag = 0;

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginSignupButton;
    TextView toggleTextView;

    public void loginSignup (View view) {

        if ( login_signup_flag == 0 ) {
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if ( e == null ) {
                        Log.d("login", "successfull");
                        moveToUserList();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if ( e == null ) {
                        Log.d("signup", "successful");
                        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if ( e == null ) {
                                    Log.d("login", "successfull");
                                    moveToUserList();
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public void toggleLoginSignup (View view) {

        if ( login_signup_flag == 0 ) {
            login_signup_flag = 1;
            loginSignupButton.setText("Sign Up");
            toggleTextView.setText("Log In");
        } else {
            login_signup_flag = 0;
            loginSignupButton.setText("Log In");
            toggleTextView.setText("Sign Up");
        }

    }

    public void moveToUserList () {
        Intent intent = new Intent(MainActivity.this, UserListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginSignupButton = findViewById(R.id.loginSignupButton);
        toggleTextView = findViewById(R.id.toggleTextView);

        if ( ParseUser.getCurrentUser() != null ) {
            moveToUserList();
        }
    }
}