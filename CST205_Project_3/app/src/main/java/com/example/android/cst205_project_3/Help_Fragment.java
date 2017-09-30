package com.example.android.cst205_project_3;

/**
 * Created by Nick89 on 4/30/2016.
 */

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Help_Fragment extends Fragment {
    /**
     * This is the callback variable, for the button to launch the server or client fragment from the mainActivity.
     */
    private OnFragmentInteractionListener mListener;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface OnFragmentInteractionListener {
        /**
         * Callback for when an item has been selected.
         */
        public void onButtonSelected(int id);
    }

    //Bluetooth device and code to turn the device on if needed
    BluetoothAdapter mBluetoothAdapter = null;
    private static final int REQUEST_ENABLE_BT = 2;
    Button btn_client, btn_server;
    TextView logger;

    public Help_Fragment() {
        //Required empty public constructor
    }
    //A simple method to append data to the logger textview.
    //so I don't have to think about it in the code.
    public void mkmsg(String msg) {
        logger.append(msg + "\n");
    }

//This code will check to see if there is a bluetooth device and
    //turn it on if is it turned off.
    public void startbt() {
        mBluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            //Device does not support Bluetooth
            mkmsg("This device does not support bluetooth");
            return;
        }
        //make sure bluetooth is enabled
        if(!mBluetoothAdapter.isEnabled()) {
            mkmsg("There is bluetooth, but turned off");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            mkmsg("The bluetooth is ready to use.");
            //bluetooth is on, so list paired devices from here
            querypaired();
        }
    }

    /*
     * This method will query the bluetooth device and ask for a list of all
     * paired devices.  It will then display to the screen the name of the device and the address
     *   In client fragment we need this address to so we can connect to the bluetooth device that is acting as the server.
     */

    public void querypaired() {
        mkmsg("Paired Device: ");
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //if there are paired devices
        if(pairedDevices.size() > 0) {
            final BluetoothDevice blueDev[] = new BluetoothDevice[pairedDevices.size()];
            String item;
            int i = 0;
            for(BluetoothDevice device1 : pairedDevices) {
                blueDev[i] = device1;
                item = blueDev[i].getName() + ": " + blueDev[i].getAddress();
                mkmsg("Device: " + item);
                i++;
            }
        }
        else {
            mkmsg("There are no paired device");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_help, container, false);
        logger = (TextView) myView.findViewById(R.id.logger1);


        btn_client = (Button) myView.findViewById(R.id.button2);
        btn_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) //don't call if null, duh...
                    mListener.onButtonSelected(2);
            }
        });
        btn_server = (Button) myView.findViewById(R.id.button1);
        btn_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) //don't call if null, duh...
                    mListener.onButtonSelected(1);
            }
        });

        startbt();

        return myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT) {
            //bluetooth result code
            if(resultCode == Activity.RESULT_OK) {
                mkmsg("Bluetooth is on");
                querypaired();
            } else {
                mkmsg("Please turn the bluetooth on");
            }
        }
    }

    /*
     * This is all for the callbacks.
     *
     */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
