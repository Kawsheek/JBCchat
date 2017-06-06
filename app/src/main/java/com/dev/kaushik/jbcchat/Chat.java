package com.dev.kaushik.jbcchat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kaushik Das on 5/5/2017.
 */


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageButton sendButton;
    EditText messageArea;
    Firebase reference1, reference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageButton)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);




        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://jbcchat-ed847.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://jbcchat-ed847.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        ProgressDialog pd = new ProgressDialog(Chat.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        try {

            reference1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map map = dataSnapshot.getValue(Map.class);
                    String message = map.get("message").toString();
                    String userName = map.get("user").toString();

                    if(userName.equals(UserDetails.username)){
                        addMessageBox("You:-\n" + message, 1);
                    }
                    else{
                        addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            });
            pd.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    sendButton.setEnabled(true);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");

                }
                else {
                    sendButton.setEnabled(false);
                }
            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;
        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.post(new Runnable()
        {
            public void run()
            {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

    }
}