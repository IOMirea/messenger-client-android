package com.iomirea;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.iomirea.http.Callback;
import com.iomirea.http.CallbackListener;
import com.iomirea.http.Message;
import com.iomirea.http.User;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private final TempMessageAdapter tempMessageAdapter = new TempMessageAdapter(this);
    private LinearLayout editLinearLayout;
    private BottomSheetDialog bottomSheetDialog;
    private int copyPosition, copyPositionForEditing = -1;
    private boolean editing = false;
    private String typedText;  // saves user text while editing message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences themePref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (themePref.getBoolean("DarkTheme", false)) {
            setTheme(R.style.AppThemeNight);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        ListView messagesView = findViewById(R.id.messages_view);
        messagesView.setAdapter(tempMessageAdapter);

        CallbackListener<Message[]> cl = new CallbackListener<Message[]>() {
            @Override
            public void callback(Callback<Message[]> cb) {
                if (!cb.isSuccess()) {
                    Toast.makeText(getApplicationContext(), cb.getError().toString(), Toast.LENGTH_LONG).show();
                    return;
                }
                User me = MainActivity.state.getMe();
                if (me == null) {
                    // TODO: Not identified, logout
                    Toast.makeText(getApplicationContext(), "Not identified", Toast.LENGTH_LONG).show();
                    return;
                }

                Long myID = MainActivity.state.getMe().getID();

                for (Message message : cb.getObj()) {
                    tempMessageAdapter.add(new TempMessage(message.getContent(), message.getAuthor().getID().equals(myID)));
                }
            }
        };

        MainActivity.client.get_messages(0L, 0L, cl);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createBottomSheetDialog();

        final EditText inputField = findViewById((R.id.message_textfield));
        final ImageButton sendButton = findViewById(R.id.send_message);

        //отправка сообщения в функции ниже
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    tempMessageAdapter.editTextInMessage(copyPositionForEditing, inputField.getText().toString());
                    editing = false;
                    sendButton.setBackgroundResource(R.drawable.ic_send_black_24dp);
                    inputField.setText(typedText);
                    copyPositionForEditing = -1;
                } else {
                    String toSend = message_trimmer(inputField.getText().toString());

                    if (toSend.length() == 0) {
                        return;
                    }

                    tempMessageAdapter.add(new TempMessage(toSend, true));

                    CallbackListener<Message> cl = new CallbackListener<Message>() {
                        @Override
                        public void callback(Callback<Message> cb) {
                            if (!cb.isSuccess()) {
                                // TODO: Delivery failed, mark message as failed

                                return;
                            }

                            // TODO: Delivery successful, mark message as confirmed
                        }
                    };

                    MainActivity.client.send_message(0L, toSend, cl);

                    inputField.setText("");
                }
            }
        });

        sendButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String sending = message_trimmer(inputField.getText().toString());
                if (sending.length() != 0) {
                    tempMessageAdapter.add(new TempMessage(sending,false));
                }
                inputField.setText("");

                return false;
            }
        });

        messagesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                copyPosition = position;

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(27);

                //открытие меню
                if(copyPositionForEditing == -1) {
                    bottomSheetDialog.show();

                    editLinearLayout.setVisibility(View.VISIBLE);
                    if (!tempMessageAdapter.belongToCurrentUser(position)) {
                        editLinearLayout.setVisibility(View.GONE);
                    }

                } else{
                    Toast.makeText(getApplicationContext(), (R.string.message_deleting_error), Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected String message_trimmer(String source) {
        // Right trim
        // TODO: make configurable from settings. Default: full trim
        boolean trimLeft = true;

        if (trimLeft) {
            source = source.replaceAll("\\s+$", "");
        } else {
            source = source.trim();
        }

        // TODO: make configurable from settings. Default: off
        // source = source.replaceAll("\\s+", " ");

        return source;
    }

    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null);
            LinearLayout copyLinearLayout = view.findViewById(R.id.copyLinearLayout);
            LinearLayout deleteLinearLayout = view.findViewById(R.id.deleteLinearLayout);
            editLinearLayout = view.findViewById(R.id.editLinearLayout);

            copyLinearLayout.setOnClickListener(this);
            deleteLinearLayout.setOnClickListener(this);
            editLinearLayout.setOnClickListener(this);

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.copyLinearLayout: {
                copyToClipboard(tempMessageAdapter.getTextFromMessage(copyPosition));

                bottomSheetDialog.dismiss();
            }
                break;
            case R.id.deleteLinearLayout: {
                    tempMessageAdapter.deleteTextInMessage(copyPosition);
                    bottomSheetDialog.dismiss();
            }
                break;
            case R.id.editLinearLayout: {
                editing = true;
                bottomSheetDialog.dismiss();

                ImageButton sendButton = findViewById(R.id.send_message);
                sendButton.setBackgroundResource(R.drawable.ic_edit_black_24dp);

                final EditText inputField = findViewById((R.id.message_textfield));
                typedText = inputField.getText().toString();
                inputField.setText(tempMessageAdapter.getTextFromMessage(copyPosition));

                copyPositionForEditing = copyPosition;
            }
            break;
        }
    }

    private void copyToClipboard(String clipboard_text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText( clipboard_text, clipboard_text);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, R.string.toast_text_copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }
}
