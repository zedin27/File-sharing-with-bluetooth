package com.example.android.cst205_project_3;
/*
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.app.FragmentManager;
import android.widget.Button;

import java.util.UUID;
*/
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, ConnectActivity.class);
                startActivity(intent);

            }

        });

    }

}



/*
public class MainActivity extends AppCompatActivity implements Help_Fragment.OnFragmentInteractionListener {

    public static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final String NAME = "BluetoothDemo";

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frag_container, new Help_Fragment()).commit();

    }

    @Override
    public void onButtonSelected(int id) {
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        if(id == 2)  { // client
            transaction.replace(R.id.frag_container, new Client_Fragment());
        }
        else {// server
                transaction.replace(R.id.frag_container, new Server_Fragment());
        }
    // and add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);
        //commit the  transaction
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
*/
