package com.tranxit.enterprise.common.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.data.network.APIClient;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends BaseActivity {


    ChatMessageAdapter mAdapter;
    String chatPath = null, id = "";
    @BindView(R.id.chat_lv)
    ListView chatLv;
    @BindView(R.id.message)
    EditText message;
    @BindView(R.id.send)
    ImageView send;
    @BindView(R.id.chat_controls_layout)
    LinearLayout chatControlsLayout;

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static String sender = "provider";

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chatPath = extras.getString("request_id", null);
            id = extras.getString("user_id", "");
            initChatView(chatPath);
        }

    }

    private void initChatView(String chatPath) {
        if (chatPath == null) {
            return;
        }

        message.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String myText = message.getText().toString().trim();
                if (myText.length() > 0) {
                    sendMessage(myText);
                }
                handled = true;
            }
            return handled;
        });

        mAdapter = new ChatMessageAdapter(activity(), new ArrayList<Chat>());
        chatLv.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(chatPath);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                mAdapter.add(chat);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @OnClick(R.id.send)
    public void onViewClicked() {
        String myText = message.getText().toString();
        if (myText.length() > 0) {
            sendMessage(myText);
        }
    }

    private void sendMessage(String messageStr) {
        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setTimestamp(new Date().getTime());
        chat.setType("text");
        chat.setText(messageStr);
        myRef.push().setValue(chat);
        chatSend(messageStr, id);
        message.setText("");
    }

    void chatSend(String messageStr, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("message", messageStr);

        Observable modelObservable = APIClient.getAPIClient().chatPush(map);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> onSuccess(trendsResponse),
                        throwable -> onError((Throwable) throwable));
    }


    public void onSuccess(Object trendsResponse) {

        Log.e("Chat push", "SUCCESS");

    }

    public void onError(Throwable e) {

        Log.e("Chat push", "ERROR");

    }
}
