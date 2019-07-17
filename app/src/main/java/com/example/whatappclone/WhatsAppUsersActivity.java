package com.example.whatappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.whatappclone.R;
import com.example.whatappclone.SignUp;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppUsersActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_users);

        final SwipeRefreshLayout swipeRefreshLayout=findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try{

                    ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username",arrayList);

                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {

                            if(e==null) {

                                if(objects.size()>0) {

                                    for (ParseUser user : objects) {

                                        arrayList.add(user.getUsername());

                                    }

                                    listView.setAdapter(arrayAdapter);
                                    arrayAdapter.notifyDataSetChanged();

                                    if (swipeRefreshLayout.isRefreshing()) {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                                else {
                                    Toast.makeText(WhatsAppUsersActivity.this, "No users to show", Toast.LENGTH_LONG).show();
                                    if (swipeRefreshLayout.isRefreshing()) {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }
                            else if(e != null){
                                Toast.makeText(WhatsAppUsersActivity.this,"Unknown Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }
                catch (Exception e){
                    e.getStackTrace();
                }

            }
        });

        listView=findViewById(R.id.my_ListView);
        arrayList=new ArrayList();
        arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);

        FancyToast.makeText(getApplicationContext(),"Welcome: "+ ParseUser.getCurrentUser().getUsername(),FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();

        final ProgressDialog progressDialog=new ProgressDialog(WhatsAppUsersActivity.this);
        progressDialog.setMessage("Loading User...");
        progressDialog.show();

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().get("username")).toString();

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {

                    if (e == null) {

                        if (objects.size() > 0) {


                            for (ParseUser user : objects) {

                                arrayList.add(user.getUsername());

                            }
                            listView.setAdapter(arrayAdapter);
                            progressDialog.dismiss();
                        } else {
                            FancyToast.makeText(getApplicationContext(), "There are no users than current user", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                            progressDialog.dismiss();
                        }
                    } else {
                        FancyToast.makeText(getApplicationContext(), "Unknown Error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        progressDialog.dismiss();
                    }
                }
            });

        }
        catch (Exception e){
            e.getStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logOut){

            ParseUser.getCurrentUser().logOut();
            Intent A=new Intent(WhatsAppUsersActivity.this, SignUp.class);
            startActivity(A);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    }

