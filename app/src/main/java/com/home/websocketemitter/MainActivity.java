package com.home.websocketemitter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    WebSocketClient webSocketClient;
    EditText editText;
    Button button_1;
    Button button_2;
    String baseURL = "ws://192.168.100.246:5000/";
    String eventName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);

        button_1 = findViewById(R.id.button_1);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emitEvent1();
            }
        });

        button_2 = findViewById(R.id.button_2);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emitEvent2();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            webSocketClient.close();
        } catch (Exception exception) {

        }
    }

    private void emitEvent1() {
        eventName = "Function_1";
        connect();
    }

    private void emitEvent2() {
        eventName = "Function_2_3";
        connect();
    }

    private void connect() {
        try {
            webSocketClient = new WebSocketClient(
                    new URI(baseURL + eventName)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("Socket", "onOpen");
                }

                @Override
                public void onMessage(String message) {
                    Log.d("Socket", "onMessage::" + message);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("Socket", "onClose");
                }

                @Override
                public void onError(Exception ex) {
                    Log.d("Socket", "onError");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "Error: " + ex.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            webSocketClient.connect();
            Log.d("Socket", "Connected");

        } catch (Exception exception) {
            Log.d("Socket", exception.getMessage());
            Toast.makeText(MainActivity.this,
                    "Error: " + exception.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}