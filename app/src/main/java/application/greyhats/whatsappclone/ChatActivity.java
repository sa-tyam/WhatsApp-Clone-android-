package application.greyhats.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ListView chatListView;
    EditText messageEditText;

    String other_user;
    ArrayList<String> chatsList;
    ArrayAdapter<String> adapter;

    List<ParseQuery<ParseObject>> queries;

    public void sendMessage (View view) {
        final String message = messageEditText.getText().toString();

        if ( !message.equals("")) {
            ParseObject chat = new ParseObject("Chat");
            chat.put("message", message);
            chat.put("reciever", other_user);
            chat.put("sender", ParseUser.getCurrentUser().getUsername());

            chat.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.d("message", "delievered");
                    chatsList.add("> " +message);
                    adapter.notifyDataSetChanged();
                    messageEditText.setText("");
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        chatListView = findViewById(R.id.chatListView);
        messageEditText = findViewById(R.id.messageEditText);

        chatsList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatsList);
        chatListView.setAdapter(adapter);

        other_user = getIntent().getStringExtra("username");
        setTitle(other_user);

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Chat");
        query1.whereEqualTo("sender", other_user);
        query1.whereEqualTo("reciever", ParseUser.getCurrentUser().getUsername());

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Chat");
        query2.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("reciever", other_user);

        queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);

        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if ( e == null && objects.size()>0 ) {
                    for ( ParseObject object : objects ) {
                        String message = "";
                        if ( object.getString("sender").equals(ParseUser.getCurrentUser().getUsername())) {
                            message = "> " + object.getString("message");
                        } else {
                            message = object.getString("message");
                        }
                        chatsList.add(message);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}