package com.example.android.cst205_project_3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.Set;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Nick89 on 4/29/2016.
 */
public class Client_Fragment extends Fragment {
    String TAG = "client";
    TextView output;
    Button btn_start, btn_device;
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothDevice device;
    BluetoothDevice remoteDevice;
    Button end, toPreview;

    String p = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();

    File someFile = new File(p + "/newFile.mp3");

    FileOutputStream fos = null;

    long size = 0;

    public Client_Fragment() {

    }

    private final int MESSAGE_READ = 1;
    private ConnectedThread mConnectedThread;
    private final int SUCCESS_CONNECT = 0;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                //case SUCCESS_CONNECT:
                //do something
                //  if (mConnectedThread != null)
                //    mConnectedThread = new ConnectedThread((BluetoothSocket) msg.obj);
                //String send = "Successfully Connected";
                //mConnectedThread.write(send.getBytes());
                //break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    //mkmsg(Integer.toString(readBuf.length));
                    //CharSet charSet = new Charset();
                    //String s = new String(readBuf, Charset.defaultCharset());
                            //readBuf.toString();
                    //mkmsg(s);


                    try {
                        if(fos != null) {

                            fos.write(readBuf);
                           // fos.flush();
                            //fos.close();
                        }
                        else
                            Log.d("File: ", "File is still null");
                    } catch(IOException e) {Log.d("File: ", "File not writing/saving"); }

                    break;
            }
        }
    };

    public void close() {
        try {
            fos.flush();
            fos.close();
        } catch(IOException e) { Log.d("File: ", "Closed");}
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            output.append(msg.getData().getString("msg"));
            return true;
        }

    });

    public void mkmsg(String str) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_client, container, false);

        output = (TextView) myView.findViewById(R.id.ct_output);
        //output.append(someFile.getAbsolutePath());
        btn_device = (Button) myView.findViewById(R.id.which_device);
        btn_device.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                querypaired();
            }
        });
        btn_start = (Button) myView.findViewById(R.id.start_client);
        btn_start.setEnabled(false);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.append("Starting client\n");
                startClient();
            }
        });
        toPreview = (Button) myView.findViewById(R.id.toPreview);
        toPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(getContext(), Preview.class);
                String pathToSend = someFile.getAbsolutePath();
                nextScreen.putExtra("File", pathToSend);
                //output.append(someFile.getAbsolutePath());
                startActivity(nextScreen);
            }
        });
        end = (Button) myView.findViewById(R.id.end);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        //setup the bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            //Device does not support Bluetooth
            output.append("No bluetooth device.]n");
            btn_start.setEnabled(false);
            btn_device.setEnabled(false);
        }
        Log.v(TAG, "bluetooth");



        return myView;
    }

    public void querypaired() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //if there are paired devices
        if(pairedDevices.size() > 0) {
            //loop thru paired devices
            output.append("at least 1 paired device\n");
            final BluetoothDevice blueDev[] = new BluetoothDevice[pairedDevices.size()];
            String[] items = new String[blueDev.length];
            int i = 0;
            for(BluetoothDevice device1 :pairedDevices) {
                blueDev[i] = device1;
                items[i] = blueDev[i].getName() + ": " + blueDev[i].getAddress();
                output.append("Device: " + items[i] + "\n");
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                i++;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose Bluetooth:");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    dialog.dismiss();
                    if (item >= 0 && item < blueDev.length) {
                        device = blueDev[item];
                        btn_device.setText("device: " + blueDev[item].getName());
                        btn_start.setEnabled(true);
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void startClient() {
        if(device != null) {
            mkmsg("Connecting................\n");
            new Thread(new ConnectThread(device)).start();
        }
        else
            mkmsg("Skipped\n");
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            //Get a bluetoothSocket fro a connection with given device

            try {
                tmp = device.createRfcommSocketToServiceRecord(ConnectActivity.MY_UUID);//(MainActivity.MY_UUID);
            } catch(IOException e) {
                mkmsg("Client connection failed: " + e.getMessage() + "\n");
            }
            socket = tmp;
        }

        public void run() {
            mkmsg("Client running\n");
            //Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            //Make a connection to the BluetoothSocket
            try {
                //This is a blocking call and will only return on
                //successful connection or an exception
                socket.connect();
            } catch (IOException e) {
                mkmsg("Connect failed\n");
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e2) {
                    mkmsg("unable to close() socket during connection failure: " + e2.getMessage() + "\n");
                    socket = null;
                }
                //Start the service over to restart listening mode
            }
            // if a connection was accepted
            if(socket != null) {
                mkmsg("Connection made\n");
                mkmsg("Remote device address: " + socket.getRemoteDevice().getAddress() + "\n");
                //Note this is copied from the TCPdemo code
                connected(socket, socket.getRemoteDevice());
                try {
                    //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                    //mkmsg("Attempting to send message ...\n");
                   // out.println("hello from Bluetooth Demo Client");
                    //out.flush();
                    //mkmsg("Message sent ...\n");
                    //mkmsg("Attempting to receive a message ...\n");
                    //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //String str = in.readLine();
                   // mkmsg("received a message:\n" + str + "\n");


                   // mHandler.obtainMessage(SUCCESS_CONNECT, socket).sendToTarget();

                   // mkmsg("We are done, close connection\n");
                } catch(Exception e) {
                    mkmsg("Error happened sending/receiving\n");
                }
               /* finally {
                    try {
                        socket.close();
                    } catch(IOException e) {
                        mkmsg("unable to close socket" + e.getMessage() + "\n");
                    }
                }*/
            } else {
                mkmsg("Made connection, but socket is null\n");
            }
            //mkmsg("Client ending\n");
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                mkmsg("close() of connect socket failed: " + e.getMessage() + "\n");
            }
        }
    }

    private void connected(BluetoothSocket socket, BluetoothDevice remoteDevice) {
        // Cancel the thread that completed the connection
        //if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        // Cancel the accept thread because we only want to connect to one device
        //if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        mkmsg("Connected to connected thread\n");

        try {
            fos = new FileOutputStream(someFile);
        } catch(FileNotFoundException e) {Log.d("File: ", "File not found"); }
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch(IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[3000];// = new byte[337292]; // buffer store for the stream
            int bytes; // bytes returned from read()
            byte[] toSend;

            //listen to input stream until exception
            while(true) {
                try {
                    //buffer = new byte[1024];
                    //read from inputstream
                    bytes = mmInStream.read(buffer);
                    toSend = new byte[bytes];

                    for(int ix = 0; ix < bytes; ix++)
                        toSend[ix] = buffer[ix];

                    //mkmsg(Integer.toString(bytes) + "\n");
                    //bytes is 0 if no bytes to read or -1 if end of file
                    // send obtained bytes to the UI activity
                    //if(bytes == 337292)
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, toSend).sendToTarget();
                   // mkmsg("Message read");
                } catch (IOException e) {
                    break;
                }
            }
        }

        // call this from main activity to send data to the remote device

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        //Call this from the main activity to shutdown connection
        public void cancel() {
            try {
                mmSocket.close();
            } catch(IOException e) { }
        }
    }


}
