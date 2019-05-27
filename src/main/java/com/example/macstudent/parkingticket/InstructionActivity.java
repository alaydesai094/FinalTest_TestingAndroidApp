package com.example.macstudent.parkingticket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class InstructionActivity extends AppCompatActivity {

    private WebView myWebViewInstruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);


        myWebViewInstruction = (WebView) findViewById(R.id.wvInstruction);
        myWebViewInstruction.loadUrl("file:///android_asset/instruction.html");
    }
}
