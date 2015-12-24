package com.sharak.mychatlet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sharak.mychatlet.models.Server;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Context context = this;

    Server server;
    EditText fldHostname;
    EditText fldPuerto;
    Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fldHostname = (EditText) findViewById(R.id.fld_hostname_1);
        fldPuerto = (EditText) findViewById(R.id.fld_puerto_1);

        btnAceptar = (Button) findViewById(R.id.button_aceptar_1);

        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean otroServidor = true;

                String strHostname = fldHostname.getText().toString();
                String strPuerto = fldPuerto.getText().toString();

                if("".equalsIgnoreCase(strHostname) && "".equalsIgnoreCase(strPuerto)){
                    otroServidor = false;
                }

                if(otroServidor){
                    server = new Server(strHostname,Integer.parseInt(strPuerto));
                } else {
                    server = new Server(Server.DEFAULT_HOSTNAME,Server.DEFAULT_PUERTO);
                }

                Bundle args = new Bundle();
                args.putString("hostname", server.getHostname());
                args.putInt("puerto", server.getPuerto());

                Intent chatIntent = new Intent(context, ChatActivity.class);
                chatIntent.putExtra("SERVER", args);
                startActivity(chatIntent);

                finish();
            }

        });

    }

}
