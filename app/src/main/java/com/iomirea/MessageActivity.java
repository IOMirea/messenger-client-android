package com.iomirea;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
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
import android.content.ClipboardManager;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout copyLinearLayout, deleteLinearLayout, editLinearLayout;
    BottomSheetDialog bottomSheetDialog;
    int copyPosition;
    final TempMessageAdapter tempMessageAdapter = new TempMessageAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ListView messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(tempMessageAdapter);
        TempMessage message = new TempMessage("Привет, слышал про новый мессенджер?",
                true);
        tempMessageAdapter.add(message);
        message = new TempMessage ("Привет)", false);
        tempMessageAdapter.add(message);
        message = new TempMessage ("Нет, но я уже очень хочу его скачать",
                false);
        tempMessageAdapter.add(message);
        message = new TempMessage ("Так мы уже в нём переписываемся", true);
        tempMessageAdapter.add(message);
        message = new TempMessage ("А",
                false);
        tempMessageAdapter.add(message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createBottomSheetDialog();
        //if(bottomSheetDialog == null){}

        final EditText mtext = findViewById((R.id.message_textfield));
        ImageButton send_message = (ImageButton) findViewById(R.id.send_message);
        //отправка сообщения в функции ниже
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sending = message_trimmer(mtext.getText().toString());
                if (sending.length()!=0) {
                    tempMessageAdapter.add(new TempMessage(sending,true));
                }
                mtext.setText("");
            }
        });
        send_message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String sending = message_trimmer(mtext.getText().toString());
                if (sending.length()!=0) {
                    tempMessageAdapter.add(new TempMessage(sending,false));
                }
                mtext.setText("");
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
                bottomSheetDialog.show();
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected String message_trimmer(String source) {
        source = source.trim();
        source = source.replaceAll("\\s+", " ");
        return source;
    }


    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null);
            copyLinearLayout = view.findViewById(R.id.copyLinearLayout);
            deleteLinearLayout = view.findViewById(R.id.deleteLinearLayout);
            editLinearLayout = view.findViewById(R.id.editLinearLayout);

            copyLinearLayout.setOnClickListener((View.OnClickListener) this);
            deleteLinearLayout.setOnClickListener((View.OnClickListener) this);
            editLinearLayout.setOnClickListener((View.OnClickListener) this);

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
                tempMessageAdapter.editTextInMessage(copyPosition);
                bottomSheetDialog.dismiss();
            }
                break;

        }
    }

    private void copyToClipboard(String clipboard_text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText( clipboard_text, clipboard_text);
        clipboard.setPrimaryClip(clip);
    }
}
