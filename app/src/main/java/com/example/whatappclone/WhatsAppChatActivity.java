package com.example.whatappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private Button btnSend;
    private EditText edtSend;
    private ArrayList<String> chatList;
    private ArrayAdapter adapter;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_chat);

        chatListView=findViewById(R.id.chatListView);
        chatList=new ArrayList<>();

        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatList);
        chatListView.setAdapter(adapter);

        btnSend=findViewById(R.id.btnSend);
        edtSend=findViewById(R.id.edtSend);

        selectedUser=getIntent().getStringExtra("SelectedUser");
        FancyToast.makeText(this,"Chat with "+selectedUser+" Now!!",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();

        btnSend.setOnClickListener(this);

        try {

            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> parseQuery = ParseQuery.or(allQueries);
            parseQuery.orderByAscending("createdAt");

            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {
                        if (objects.size() > 0) {

                            for (ParseObject chatOb : objects) {

                                String waMessage = chatOb.get("waMessage") + "";

                                if (chatOb.get("waSender").equals(selectedUser)) {
                                    waMessage = selectedUser + " : " + waMessage;
                                }
                                if (chatOb.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                    waMessage = ParseUser.getCurrentUser().getUsername() + " : " + waMessage;
                                }

                                chatList.add(waMessage);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        ParseObject chat=new ParseObject("Chat");

        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient",selectedUser);
        chat.put("waMessage",edtSend.getText().toString());

        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){
                    FancyToast.makeText(WhatsAppChatActivity.this,"Message from "+ParseUser.getCurrentUser().getUsername()+" send to "+selectedUser,FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                    chatList.add(ParseUser.getCurrentUser().getUsername()+": "+edtSend.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtSend.setText("");
                }
                else{
                    FancyToast.makeText(WhatsAppChatActivity.this,e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }

            }
        });
    }
}
