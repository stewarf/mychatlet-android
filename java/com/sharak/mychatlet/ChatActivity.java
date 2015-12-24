package com.sharak.mychatlet;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sharak.mychatlet.models.Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity {

    public static final String TAG = ChatActivity.class.getSimpleName();

    Bundle bundle;
    Server server;
    InputStream is;
    OutputStream os;

    EditText fldMensaje;
    Button btnSend;
    TableLayout tab;

    String current_msg;

    private Handler puente = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mostrarMsg(msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tab = (TableLayout)findViewById(R.id.tab);
        fldMensaje = (EditText)findViewById(R.id.fldMensaje);
        btnSend = (Button)findViewById(R.id.chatSendButton);

        bundle = getIntent().getBundleExtra("SERVER");
        server = new Server();
        server.setHostname(bundle.getString("hostname", Server.DEFAULT_HOSTNAME));
        server.setPuerto(bundle.getInt("puerto", Server.DEFAULT_PUERTO));

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                current_msg = fldMensaje.getText().toString();
                if (!"".equalsIgnoreCase(current_msg)) {
                    TableRow tr2 = new TableRow(getApplicationContext());
                    tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView textview = new TextView(getApplicationContext());
                    textview.setTextSize(17);
                    textview.setTextColor(Color.parseColor("#A901DB"));
                    textview.setText(Html.fromHtml("<b>Tu: </b>" + current_msg));
                    tr2.addView(textview);
                    tab.addView(tr2);

                    fldMensaje.setText("");
                    enviarMsg();
                }
            }

        });

        new conexionSocketTask().execute(server.getHostname(),String.valueOf(server.getPuerto()));
    }

    private class conexionSocketTask extends AsyncTask<String,Void,Socket> {

        @Override
        protected Socket doInBackground(String... params) {
            try {
                Socket cliente = new Socket(params[0],Integer.parseInt(params[1]));
                return cliente;
            } catch(Exception e) {
                Log.e(TAG,"Problemas en la conexion: "+e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Socket socket) {

            try {
                is = socket.getInputStream();
                os = socket.getOutputStream();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StringBuffer sb = new StringBuffer();
                            int c = 0;
                            while ((c = is.read()) != -1) {
                                if(c != '\n'){
                                    sb.append((char)c);
                                } else {
                                    Message msg = new Message();
                                    msg.obj = sb.toString();
                                    puente.sendMessage(msg);
                                    sb = new StringBuffer();
                                }
                            }
                        } catch(Exception ex){ }

                    }
                }).start();

            } catch(Exception e) {
                Log.e(TAG,"Problemas durante el proceso del socket: "+e.getMessage());
                e.printStackTrace();
            }

        }

    }

    public void enviarMsg(){
        try {
            os.write(current_msg.trim().getBytes());
            os.write("\r\n".getBytes());
        } catch(Exception e){
            Log.e(TAG, "Error en enviarMsg: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarMsg(String msg){
        TableRow tr1 = new TableRow(getApplicationContext());
        tr1.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView textview = new TextView(getApplicationContext());
        textview.setTextSize(17);
        textview.setTextColor(Color.parseColor("#0B0719"));
        textview.setText(Html.fromHtml(msg));
        tr1.addView(textview);
        tab.addView(tr1);
    }

}
