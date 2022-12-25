package Sadraafzar.com.Charitable;

import Classess.Utils;
import Classess.app;
import Classess.dbConnector;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 10/12/2017.
 */
public class PrintRongtaActivity extends Activity{






    private View layoutThereArentPairedPrinters;
    private View layoutPrinterReady;
    private Button printButton = null;
    ImageButton imgBtnBack;
    dbConnector db;
    boolean printAuto=false;


    String firstMessage="",lastMessage="";


    // Debugging
    private static final String TAG = "BloothPrinterActivity";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static int revBytes=0;
    public  static boolean isHex=false;

    public static final int REFRESH = 8;

    // Layout Views
    private TextView mTitle;

    // Name of the connected device
    public static String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    public static BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    public static BluetoothPrintDriver mChatService = null;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        setContentView(R.layout.printrongtalayout);


        db = new dbConnector(this, app.database.DBNAME , null ,1);



        imgBtnBack=(ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        layoutThereArentPairedPrinters = findViewById(R.id.layoutNoExisteImpresora);
        layoutPrinterReady = findViewById(R.id.layoutImpresoraPreparada);


        // Set up the custom title
      /*  mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);*/
        mTitle = (TextView) findViewById(R.id.title_right_text);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }



        findViewById(R.id.pairPrinter).setOnClickListener(new View.OnClickListener() {
            Intent serverIntent = null;
            @Override
            public void onClick(View v) {
                // Launch the DeviceListActivity to see devices and do scan
                serverIntent = new Intent(PrintRongtaActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

            }
        });


        printButton = (Button) findViewById(R.id.print);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Print();
            }
        });


    }

    private String SpacePicture(String stFirst,String stLast)
    {
        int lengthString1=stFirst.length();
        int lengthString2=stLast.length();
        String space="";
        for (int i=1;i<=(35-lengthString1-lengthString2);i++)
            space+=' ';
        return space;
    }

    private String[] SplitMessage(String message)
    {
        return message.split(";");

    }
    private void Fill(int fk_HeaderID,int fk_BoxID,int fk_SarFasl)
    {


        String query  = "SELECT tblBoxDischargeItem.fld_Transferee,tblBox.fld_Code,tblSarFasl.fld_SarFasl,tblBoxDischargeItem.fld_FishNo,tblBoxDischargeItem.fld_PiecePrice\n" +
                ",tblBoxDischargeItem.fld_PaperPrice,tblBoxDischargeItem.fld_description FROM tblBoxDischargeItem inner join tblBox on tblBoxDischargeItem.fk_BoxID=tblBox.PKIDtblBox inner join tblSarFasl on tblBoxDischargeItem.fk_SarFasl=tblSarFasl.PKID  Where  fk_HeaderID="+fk_HeaderID+" and fk_BoxID="+fk_BoxID+" and fk_SarFasl="+fk_SarFasl;


        Cursor c = null;
        c = db.select(query);
        if(c.moveToNext()) {

            app.print.Transferee=c.getString(c.getColumnIndex("fld_Transferee"));
            app.print.BoxCode=c.getString(c.getColumnIndex("fld_Code"));
            app.print.SarFasl=c.getString(c.getColumnIndex("fld_SarFasl"));
            app.print.FishNo=c.getString(c.getColumnIndex("fld_FishNo"));
            DecimalFormat df = new DecimalFormat("###,###,###");
            app.print.PiecePrice=df.format(c.getLong(c.getColumnIndex("fld_PiecePrice")));
            app.print.PaperPrice=df.format(c.getLong(c.getColumnIndex("fld_PaperPrice")));
            app.print.SumPrice=df.format((c.getLong(c.getColumnIndex("fld_PiecePrice")) + c.getLong(c.getColumnIndex("fld_PaperPrice"))));

            app.print.Description=c.getString(c.getColumnIndex("fld_description"));
        }
        c.close();

        query="Select * From Login";
        c = null;
        c = db.select(query);
        if(c.moveToNext()) {
            app.print.Agent1Name=c.getString(c.getColumnIndex("fld_Name1"))+" "+c.getString(c.getColumnIndex("fld_Family1"));
            app.print.Agent1Code=c.getString(c.getColumnIndex("fld_Code1"));
            app.print.Agent2Name=c.getString(c.getColumnIndex("fld_Name2"))+" "+c.getString(c.getColumnIndex("fld_Family2"));
            app.print.Agent2Code=c.getString(c.getColumnIndex("fld_Code2"));
            app.print.Message=c.getString(c.getColumnIndex("fld_Message"));

        }


    }


    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] bufTemp2 = Utils.decodeBitmap(bmp);
                BluetoothPrintDriver.printByteData(bufTemp2);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    public void printPhoto(Bitmap bmp) {
        try {
            //Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/inpaint/"+"seconds"+".jpg");
            if(bmp!=null){
                byte[] bufTemp2 = Utils.decodeBitmap(bmp);
                BluetoothPrintDriver.printByteData(bufTemp2);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

   public void printNewLine()
   {
       BluetoothPrintDriver.Begin();

       BluetoothPrintDriver.BT_Write("\n");
       BluetoothPrintDriver.LF();
   }
   public void Print()
   {
       Intent serverIntent = null;
       if(BluetoothPrintDriver.IsNoConnection()){
           // Launch the DeviceListActivity to see devices and do scan
           serverIntent = new Intent(PrintRongtaActivity.this, DeviceListActivity.class);
           startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
       }
       else{






               printNewLine();


               printPhoto( R.drawable.logo_print2);

               printPhoto(app.drawText(getBaseContext().getString(R.string.app_name) + " " + getBaseContext().getString(R.string.reyCity), 340, 28, Layout.Alignment.ALIGN_CENTER, true));

               printNewLine();

           //BoxCode
           firstMessage=(getBaseContext().getString(R.string.PrintBoxCode)+"  "+app.print.BoxCode);
           lastMessage="";
           printPhoto(app.drawText(firstMessage,340,22,Layout.Alignment.ALIGN_CENTER));

           //Transferee
           firstMessage=getBaseContext().getString(R.string.PrintTransfree)+"  "+app.print.Transferee;
           lastMessage="";
           printPhoto(app.drawText(firstMessage, 340, 22, Layout.Alignment.ALIGN_CENTER));

           //DateTime
           firstMessage=getBaseContext().getString(R.string.PrintDate)+app.nowDate();
           lastMessage=getBaseContext().getString(R.string.PrintTime)+" "+app.now("HH:mm");
           printPhoto(app.drawText(firstMessage+" "+lastMessage,340,22,Layout.Alignment.ALIGN_CENTER));

               //FishNo
               firstMessage="";
               lastMessage=getBaseContext().getString(R.string.PrintFishNo)+" "+app.print.FishNo;
               //printPhoto(app.drawText(firstMessage + SpacePicture(firstMessage, lastMessage) + lastMessage, 340, 22, Layout.Alignment.ALIGN_CENTER));
               printPhoto(app.drawText(firstMessage+" "+lastMessage,340,22,Layout.Alignment.ALIGN_CENTER));






               printPhoto(app.drawText("------------------------------", 340, 25, Layout.Alignment.ALIGN_CENTER,true));

               //SarFasle
               firstMessage=getBaseContext().getString(R.string.PrintSarFasl);
               lastMessage=app.print.SarFasl;
               printPhoto(app.drawText(firstMessage + "                       " + lastMessage, 340, 24, Layout.Alignment.ALIGN_NORMAL));

               //PiecePrice
               firstMessage=getBaseContext().getString(R.string.PrintPiecePrice);
               lastMessage=app.print.PiecePrice+" "+getBaseContext().getString(R.string.PrintUnitMoney);
               printPhoto(app.drawText(firstMessage + "                " + lastMessage, 340, 22, Layout.Alignment.ALIGN_NORMAL));

               //PaperPrice
               firstMessage=getBaseContext().getString(R.string.PrintPaperPrice);
               lastMessage=app.print.PaperPrice+" "+getBaseContext().getString(R.string.PrintUnitMoney);
               printPhoto(app.drawText(firstMessage + "          " + lastMessage, 340, 22, Layout.Alignment.ALIGN_NORMAL));

               printPhoto(app.drawText("------------------------------", 340, 25, Layout.Alignment.ALIGN_CENTER,true));


               //SumPrice
               firstMessage=getBaseContext().getString(R.string.PrintSumPrice);
               lastMessage=app.print.SumPrice+" "+getBaseContext().getString(R.string.PrintUnitMoney);
               printPhoto(app.drawText(firstMessage + "              " + lastMessage, 340, 24, Layout.Alignment.ALIGN_NORMAL));

               //Description
               if(!app.print.Description.equals("")) {
                   firstMessage = getBaseContext().getString(R.string.PrintDescription);
                   lastMessage = app.print.Description;
                   printPhoto(app.drawText(firstMessage + lastMessage, 340, 22, Layout.Alignment.ALIGN_NORMAL));
               }
               printNewLine();

               //Mission1
               firstMessage=getBaseContext().getString(R.string.PrintMission1);
               lastMessage=app.print.Agent1Name+"("+app.print.Agent1Code+")";
               //printPhoto(app.drawText(firstMessage+SpacePicture(firstMessage, lastMessage) + lastMessage, 340, 22, Layout.Alignment.ALIGN_CENTER));
               printPhoto(app.drawText(firstMessage + "    " + lastMessage, 340, 22, Layout.Alignment.ALIGN_CENTER));

               //Mission2
               firstMessage=getBaseContext().getString(R.string.PrintMission2);
               lastMessage=app.print.Agent2Name+"("+app.print.Agent2Code+")";
               //printPhoto(app.drawText(firstMessage+SpacePicture(firstMessage, lastMessage) + lastMessage, 340, 22, Layout.Alignment.ALIGN_CENTER));
               printPhoto(app.drawText(firstMessage+"    "+lastMessage,340,22,Layout.Alignment.ALIGN_CENTER));

               printNewLine();

               //Message
               String message[]=SplitMessage(app.print.Message);
               for(int i=0;i<message.length;i++) {
                   firstMessage =message[i];
                   lastMessage = "";
                   printPhoto(app.drawText(firstMessage, 340, 22, Layout.Alignment.ALIGN_CENTER));
               }

           printNewLine();
           printNewLine();

       }
   }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothPrintDriver.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
        Intent data=getIntent();
        if(data!=null)
        {
            String BoxIDString=data.getStringExtra("fk_BoxID");
            String HeaderIDString=data.getStringExtra("fk_HeaderID");
            String SarFaslString=data.getStringExtra("fk_SarFasl");
            Fill(Integer.parseInt(HeaderIDString),Integer.parseInt(BoxIDString),Integer.parseInt(SarFaslString));
        }
        if(!BluetoothPrintDriver.IsNoConnection())
        {
            Print();
            finish();
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothPrintDriver(this, mHandler);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    @SuppressLint("NewApi")
    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintDriver.STATE_CONNECTED:
                            mTitle.setText(R.string.title_connected_to);
                            mTitle.append(mConnectedDeviceName);
                            layoutThereArentPairedPrinters.setVisibility(View.GONE);
                            layoutPrinterReady.setVisibility(View.VISIBLE);
                            //setTitle(R.string.title_connected_to);
                            //setTitle(mConnectedDeviceName);
                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:
                            mTitle.setText(R.string.title_connecting);
                            //setTitle(R.string.title_connecting);
                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
                        case BluetoothPrintDriver.STATE_NONE:
                            mTitle.setText(R.string.title_not_connected);
                            layoutThereArentPairedPrinters.setVisibility(View.VISIBLE);
                            layoutPrinterReady.setVisibility(View.GONE);
                            //setTitle(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String ErrorMsg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    float Voltage = 0;
                    if(D) Log.i(TAG, "readBuf[0]:"+readBuf[0]+"  readBuf[1]:"+readBuf[1]+"  readBuf[2]:"+readBuf[2]);
                    if(readBuf[2]==0)
                        ErrorMsg = "NO ERROR!         ";
                    else
                    {
                        if((readBuf[2] & 0x02) != 0)
                            ErrorMsg = "ERROR: No printer connected!";
                        if((readBuf[2] & 0x04) != 0)
                            ErrorMsg = "ERROR: No paper!  ";
                        if((readBuf[2] & 0x08) != 0)
                            ErrorMsg = "ERROR: Voltage is too low!  ";
                        if((readBuf[2] & 0x40) != 0)
                            ErrorMsg = "ERROR: Printer Over Heat!  ";
                    }
                    Voltage = (float) ((readBuf[0]*256 + readBuf[1])/10.0);
                    //if(D) Log.i(TAG, "Voltage: "+Voltage);
                    DisplayToast(ErrorMsg+"                                        "+"Battery voltage��"+Voltage+" V");
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    //��ʾ��Ϣ
    public void showMessage(String str)
    {
        Toast.makeText(this,str, Toast.LENGTH_LONG).show();
    }//showMessage

    // ��ʾToast
    public void DisplayToast(String str)
    {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        //����toast��ʾ��λ��
        toast.setGravity(Gravity.TOP, 0, 100);
        //��ʾ��Toast
        toast.show();
    }//DisplayToast

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    //Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }



}