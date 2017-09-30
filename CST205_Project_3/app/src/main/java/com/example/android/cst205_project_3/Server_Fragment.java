package com.example.android.cst205_project_3;

/**
 * Created by Nick89 on 4/30/2016.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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



import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;


public class Server_Fragment extends Fragment {

    Button buttonOpenDialog;
    Button buttonUp;
    TextView textFolder;

    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;

    File root;
    File curFolder;

    private List<String> fileList = new ArrayList<String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    BluetoothAdapter mBluetoothAdapter = null;

    TextView output;
    Button btn_start;
    Button btn_send;

    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    String fileName = "/Elvis_Herod_-_The_Hunter_and_The_Fox.mp3";//"/Darth_Vader.mp3";//"/Elvis_Herod_-_The_Hunter_and_The_Fox.mp3";

    private final int MESSAGE_READ = 1;
    private ConnectedThread mConnectedThread;
    private final int SUCCESS_CONNECT = 0;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SUCCESS_CONNECT:
                    //do something
                    if (mConnectedThread != null)
                        mConnectedThread = new ConnectedThread((BluetoothSocket) msg.obj);
                    String send = "Successfully Connected";
                    mConnectedThread.write(send.getBytes());
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String s = readBuf.toString();
                    break;
            }
        }
    };

    public Server_Fragment() {
        // Required empty public constructor
    }
    //////////////////////////////
/*
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Custom Dialog");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                }); //4:20

                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist); //4.49
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File selected = new File(fileList.get(position)); //4.49
                        if (selected.isDirectory()) {
                            ListDir(selected);
                        } else {
                            Toast.makeText(getContext(), selected.toString() + " selected", Toast.LENGTH_LONG).show();
                            dismissDialog(CUSTOM_DIALOG_ID);
                        }
                    }
                });
                break;
        }
        return dialog;
    }
    //@Override ///5.21
    protected void onPrepare(int id, Dialog dialog){
        super.onPrepareDialog(id, dialog);
        switch(id) {
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                break;
        }
    }
    void ListDir(File f){
        if(f.equals(root)){
            buttonUp.setEnabled(false);
        } else {
            buttonUp.setEnabled(true);
        }
        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();
        for(File file: files){
            fileList.add(file.getPath());
        }
        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);
    }
*/
    //////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_server, container, false);
        //////////////////////////
/*
        buttonOpenDialog = (Button)myView.findViewById(R.id.opendialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;
        //text field for output info.
        */
        //////////////////////////////////////////////////////////////////////////////////////////////
        output = (TextView) myView.findViewById(R.id.sv_output);

        btn_start = (Button) myView.findViewById(R.id.start_server);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.append("Starting server\n");
                startServer();
            }
        });

        btn_send = (Button) myView.findViewById(R.id.send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String s = "Hello client!!!!!\n";
                //write(s.getBytes());
               // File file = new File(path + "/Elvis_Herod_-_The_Hunter_and_The_Fox.mp3");
                //mkmsg(path + "/Elvis_Herod_-_The_Hunter_and_The_Fox.mp3" );
                //mkmsg(file.getAbsolutePath());

/////////////////////////////////////////////////////////
                try {
                    File file = new File(path + fileName);
                    FileInputStream fis = new FileInputStream(file);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];

                    try {
                        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                            bos.write(buf, 0, readNum);
                        } // end for
                    } // end try
                    catch (IOException ex) {
                        mkmsg("Not reading\n");
                    } // end catch
                    byte[] bytes = bos.toByteArray();
                    /*
                    int length = bytes.length;
                    mkmsg("length: " + Integer.toString(length));
                    byte[] send = new byte[1024];
                    int jx = 0;
                    for(int ix = 0; ix < length; ix++) {
                        if(ix % 1024 == 0) {
                            write(send);
                            send = new byte[1024];
                            jx = 0;
                        }

                        send[jx] = bytes[ix];
                        jx++;
                    }
                    */
                    write(bytes);
                    mkmsg("Finished Process");

                    //write("stop".getBytes());


                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("File: ", "File not found");
                }

//////////////////////////////////////////////////////////////

            }
        });


        //setup the bluetooth adapter.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            output.append("No bluetooth device.\n");
            btn_start.setEnabled(false);
        }

        return myView;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            output.append(msg.getData().getString("msg"));
            return true;
        }

    });

    public void mkmsg(String str) {
        //handler junk, because thread can't update screen!
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }
/*
    public byte[] getFileBytes() throws FileNotFoundException, IOException {


        return bytes;
    }
*/
    public void write(byte[] out) {
        // Create temporary object
        /*
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
        */
        mConnectedThread.write(out);
    }

    public void startServer() {
        new Thread(new AcceptThread()).start();
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

        String s = "Hello Client!!!\n";
        write(s.getBytes());

    }


    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */

    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(ConnectActivity.NAME,ConnectActivity.MY_UUID);//MainActivity.NAME, MainActivity.MY_UUID);
            } catch (IOException e) {
                mkmsg("Failed to start server\n");
            }
            mmServerSocket = tmp;
        }

        public void run() {
            mkmsg("waiting on accept");
            BluetoothSocket socket = null;
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                mkmsg("Failed to accept\n");
            }

            // If a connection was accepted
            if (socket != null) {
                mkmsg("Connection made\n");
                mkmsg("Remote device address: " + socket.getRemoteDevice().getAddress() + "\n");
                //Note this is copied from the TCPdemo code.
                connected(socket, socket.getRemoteDevice());
                try {
                    mkmsg("Attempting to receive a message ...\n");
                    //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //String str = in.readLine();
                    //mkmsg("received a message:\n" + str + "\n");

                    //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    //mkmsg("Attempting to send message ...\n");
                    //out.println("Hi from Bluetooth Demo Server");
                    //out.flush();
                   // mkmsg("Message sent...\n");

                    //mHandler.obtainMessage(SUCCESS_CONNECT, socket).sendToTarget();

                    //mkmsg("We are done, closing connection\n");
                    //connected(socket, socket.getRemoteDevice());

                } catch (Exception e) {
                    mkmsg("Error happened sending/receiving\n");

                } /*finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        mkmsg("Unable to close socket" + e.getMessage() + "\n");
                    }
                }*/
            } else {
                mkmsg("Made connection, but socket is null\n");
            }
            //mkmsg("Server ending \n");
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                mkmsg("close() of connect socket failed: " + e.getMessage() + "\n");
            }
        }
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
            byte[] buffer; // buffer store for the stream
            int bytes; // bytes returned from read()

            //listen to input stream until exception
            while(true) {
                try {
                    buffer = new byte[1024];
                    //read from inputstream
                    bytes = mmInStream.read(buffer);
                    // send obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
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

