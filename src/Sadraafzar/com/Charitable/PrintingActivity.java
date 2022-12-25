package Sadraafzar.com.Charitable;

import Classess.BluetoothUtil;
import Classess.app;
import Classess.dbConnector;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bixolon.printer.BixolonPrinter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 7/30/2017.
 */
public class PrintingActivity extends Activity {
    //The columns of your printer. We only tried the Bixolon 300 and the Bixolon 200II, so there are the values.
    //    private final int LINE_CHARS = 42 + 22; // Bixolon 300
    private final int LINE_CHARS = 42; // Bixolon 200II


    //Some time to don't flood the printer with new commands. It's fine to wait a little after sending an image to the printer.
    private static final long PRINTING_SLEEP_TIME = 300;

    //The time the printer takes to print the ticket. It makes the print button to be enabled again after this time in millis.
    //Of course, you can get it in an empiric way... :D
    private static final long PRINTING_TIME = 2200;

    //Two constants that some Bixolon printers send, but aren't included in the Bixolon library. Probably some printers can send it? Don't know.
    static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
    static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;

    //The core of the monster: managing the Bixolon printer connection lifecycle
    private List<String> pairedPrinters = new ArrayList<String>();
    private Boolean connectedPrinter = false;
    private static BixolonPrinter bixolonPrinterApi;

    //View layer things
    private Animation rotation = null; //Caching an animation makes the world a better place to be
    private View layoutLoading;
    private View layoutThereArentPairedPrinters;
    private View layoutPrinterReady;
    private TextView debugTextView = null; //A hidden TextView where you can test things
    private Button printButton = null; //Guess it :P
    ImageButton imgBtnBack;
    dbConnector db;
    String firstMessage="",lastMessage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing);

        db = new dbConnector(this, app.database.DBNAME , null ,1);


        imgBtnBack=(ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        if (rotation == null) {
            rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        }

        debugTextView = (TextView) findViewById(R.id.debug);
        printButton = (Button) findViewById(R.id.print);

        layoutLoading = findViewById(R.id.layoutLoading);
        layoutThereArentPairedPrinters = findViewById(R.id.layoutNoExisteImpresora);
        layoutPrinterReady = findViewById(R.id.layoutImpresoraPreparada);

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                printButton.setEnabled(false);
                new Handler().postDelayed(new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        printButton.setEnabled(true);
                    }
                }, PRINTING_TIME);

                Thread t = new Thread() {
                    /** Where the actual print happens. BTW, the easyest code. */
                    public void run() {
                        try {
                            //FIXME this example hardcodes the text values to increase a little the readability of the code. Don't do it in production! :)

                            bixolonPrinterApi.setSingleByteFont(BixolonPrinter.CODE_PAGE_FARSI); //It fixes an issue printing special values like €, αινσϊ...

                            bixolonPrinterApi.lineFeed(1, false); //It's like printing \n\n
                            Bitmap fewlapsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_print);

                            //BEWARE THE DOG: The 260 and 50 values are really MAGIC. They aren't as siple as width and height. It can break the Bitmap print.
                            bixolonPrinterApi.printBitmap(fewlapsBitmap, BixolonPrinter.ALIGNMENT_CENTER, 100, 50, false);

                            bixolonPrinterApi.printBitmap(app.drawText(getBaseContext().getString(R.string.app_name)+" "+getBaseContext().getString(R.string.reyCity), 400, 25, Layout.Alignment.ALIGN_CENTER,true), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);

                            bixolonPrinterApi.lineFeed(1, false); //It's like printing \n\n

                            //FishNo
                            firstMessage="";
                            lastMessage=getBaseContext().getString(R.string.PrintFishNo)+app.print.FishNo;
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage+SpacePicture(firstMessage,lastMessage)+lastMessage,400,20,Layout.Alignment.ALIGN_CENTER), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);

                            //DateTime
                            firstMessage=getBaseContext().getString(R.string.PrintDate)+app.nowDate();
                            lastMessage=getBaseContext().getString(R.string.PrintTime)+" "+app.now("HH:mm");
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage+"                     "+lastMessage,400,20,Layout.Alignment.ALIGN_CENTER), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);

                            //BoxCode
                            firstMessage=(getBaseContext().getString(R.string.PrintBoxCode)+"  "+app.print.BoxCode);
                            lastMessage="";
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage,400,20,Layout.Alignment.ALIGN_CENTER), BixolonPrinter.ALIGNMENT_RIGHT, 400, 50, false);

                            //Transferee
                            firstMessage=getBaseContext().getString(R.string.PrintTransfree)+"  "+app.print.Transferee;
                            lastMessage="";
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage,400,20,Layout.Alignment.ALIGN_CENTER), BixolonPrinter.ALIGNMENT_RIGHT, 400, 50, false);


                            printText("---------------------------------", BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.TEXT_ATTRIBUTE_FONT_A);

                            //SarFasle
                            firstMessage=getBaseContext().getString(R.string.PrintSarFasl);
                            lastMessage=app.print.SarFasl;
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage+"                                      "+lastMessage,400,20,Layout.Alignment.ALIGN_NORMAL), BixolonPrinter.ALIGNMENT_RIGHT, 400, 50, false);

                            //PiecePrice
                            firstMessage=getBaseContext().getString(R.string.PrintPiecePrice);
                            lastMessage=app.print.PiecePrice+" "+getBaseContext().getString(R.string.PrintUnitMoney);
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage+"                                    "+lastMessage,400,20,Layout.Alignment.ALIGN_NORMAL), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);

                            //PaperPrice
                            firstMessage=getBaseContext().getString(R.string.PrintPaperPrice);
                            lastMessage=app.print.PaperPrice+" "+getBaseContext().getString(R.string.PrintUnitMoney);
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage+"                              "+lastMessage,400,20,Layout.Alignment.ALIGN_NORMAL), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);

                            printText("---------------------------------", BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.TEXT_ATTRIBUTE_FONT_A);


                            //SumPrice
                            firstMessage=getBaseContext().getString(R.string.PrintSumPrice);
                            lastMessage=app.print.SumPrice+" "+getBaseContext().getString(R.string.PrintUnitMoney);
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage+"                                       "+lastMessage,400,20,Layout.Alignment.ALIGN_NORMAL), BixolonPrinter.ALIGNMENT_RIGHT, 400, 50, false);



                            //Description
                            if(!app.print.Description.equals("")) {
                                firstMessage = getBaseContext().getString(R.string.PrintDescription);
                                lastMessage = app.print.Description;
                                bixolonPrinterApi.printBitmap(app.drawText(firstMessage + lastMessage, 400, 20, Layout.Alignment.ALIGN_NORMAL), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);
                            }



                            bixolonPrinterApi.lineFeed(1, false); //It's like printing \n\n

                            //Mission1
                            firstMessage=getBaseContext().getString(R.string.PrintMission1);
                            lastMessage=app.print.Agent1Name+"("+app.print.Agent1Code+")";
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage+SpacePicture(firstMessage,lastMessage)+lastMessage,400,20,Layout.Alignment.ALIGN_CENTER), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);

                            //Mission2
                            firstMessage=getBaseContext().getString(R.string.PrintMission2);
                            lastMessage=app.print.Agent2Name+"("+app.print.Agent2Code+")";
                            bixolonPrinterApi.printBitmap(app.drawText(firstMessage+SpacePicture(firstMessage,lastMessage)+lastMessage,400,20,Layout.Alignment.ALIGN_CENTER), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);


                            bixolonPrinterApi.lineFeed(1, false); //It's like printing \n\n

                            //Message
                            String message[]=SplitMessage(app.print.Message);
                            for(int i=0;i<message.length;i++) {
                                firstMessage =message[i];
                                lastMessage = "";
                                bixolonPrinterApi.printBitmap(app.drawText(firstMessage, 400, 20, Layout.Alignment.ALIGN_CENTER), BixolonPrinter.ALIGNMENT_CENTER, 400, 50, false);
                            }
                           // Thread.sleep(PRINTING_SLEEP_TIME); // Don't strees the printer while printing the Bitmap... it don't like it.



                            bixolonPrinterApi.lineFeed(2, false);
                        } catch (Exception e) {
                            Log.e("ERROR", "Printing", e);
                        }
                    }
                };
                t.start();
            }
        });
        findViewById(R.id.pairPrinter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String mac : pairedPrinters) {
                    BluetoothUtil.unpairMac(mac);
                }
                pairedPrinters.clear();

                Intent settingsIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(settingsIntent);
            }
        });

        updateScreenStatus(layoutLoading);
    }

    private String SpacePicture(String stFirst,String stLast)
    {
        int lengthString1=stFirst.length();
        int lengthString2=stLast.length();
        String space="";
        for (int i=1;i<=(55-lengthString1-lengthString2);i++)
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
    private void updateScreenStatus(View viewToShow) {
        if (viewToShow == layoutLoading) {
            layoutLoading.setVisibility(View.VISIBLE);
            layoutThereArentPairedPrinters.setVisibility(View.GONE);
            layoutPrinterReady.setVisibility(View.GONE);
            iconLoadingStart();
        } else if (viewToShow == layoutThereArentPairedPrinters) {
            layoutLoading.setVisibility(View.GONE);
            layoutThereArentPairedPrinters.setVisibility(View.VISIBLE);
            layoutPrinterReady.setVisibility(View.GONE);
            iconLoadingStop();
        } else if (viewToShow == layoutPrinterReady) {
            layoutLoading.setVisibility(View.GONE);
            layoutThereArentPairedPrinters.setVisibility(View.GONE);
            layoutPrinterReady.setVisibility(View.VISIBLE);
            iconLoadingStop();
        }

        updatePrintButtonState();
    }

    PairWithPrinterTask task = null;

    @Override
    protected void onResume() {
        super.onResume();

        bixolonPrinterApi = new BixolonPrinter(this, handler, null);
        task = new PairWithPrinterTask();
        task.execute();

        updatePrintButtonState();

        BluetoothUtil.startBluetooth();

        Intent data=getIntent();
        if(data!=null)
        {
            String BoxIDString=data.getStringExtra("fk_BoxID");
            String HeaderIDString=data.getStringExtra("fk_HeaderID");
            String SarFaslString=data.getStringExtra("fk_SarFasl");
            Fill(Integer.parseInt(HeaderIDString),Integer.parseInt(BoxIDString),Integer.parseInt(SarFaslString));
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (task != null) {
            task.stop();
            task = null;
        }

        if (bixolonPrinterApi != null) {
            bixolonPrinterApi.disconnect();
        }

        super.onPause();
    }

    private void updatePrintButtonState() {
        printButton.setEnabled(connectedPrinter != null && connectedPrinter == true);
    }

    private final Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            // Log.i("Handler", msg.what + " " + msg.arg1 + " " + msg.arg2);

            switch (msg.what) {

                case BixolonPrinter.MESSAGE_STATE_CHANGE:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_STATE_CHANGE");
                    switch (msg.arg1) {
                        case BixolonPrinter.STATE_CONNECTED:
                            updateScreenStatus(layoutPrinterReady);
                            Log.i("Handler", "BixolonPrinter.STATE_CONNECTED");
                            connectedPrinter = true;
                            updateScreenStatus(layoutPrinterReady);
                            break;

                        case BixolonPrinter.STATE_CONNECTING:
                            updateScreenStatus(layoutLoading);
                            Log.i("Handler", "BixolonPrinter.STATE_CONNECTING");
                            connectedPrinter = false;
                            break;

                        case BixolonPrinter.STATE_NONE:
                            updateScreenStatus(layoutLoading);
                            Log.i("Handler", "BixolonPrinter.STATE_NONE");
                            connectedPrinter = false;
                            break;
                    }
                    break;

                case BixolonPrinter.MESSAGE_WRITE:
                    switch (msg.arg1) {
                        case BixolonPrinter.PROCESS_SET_SINGLE_BYTE_FONT:
                            Log.i("Handler", "BixolonPrinter.PROCESS_SET_SINGLE_BYTE_FONT");
                            break;

                        case BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT:
                            Log.i("Handler", "BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT");
                            break;

                        case BixolonPrinter.PROCESS_DEFINE_NV_IMAGE:
                            Log.i("Handler", "BixolonPrinter.PROCESS_DEFINE_NV_IMAGE");
                            break;

                        case BixolonPrinter.PROCESS_REMOVE_NV_IMAGE:
                            Log.i("Handler", "BixolonPrinter.PROCESS_REMOVE_NV_IMAGE");
                            break;

                        case BixolonPrinter.PROCESS_UPDATE_FIRMWARE:
                            Log.i("Handler", "BixolonPrinter.PROCESS_UPDATE_FIRMWARE");
                            break;
                    }
                    break;

                case BixolonPrinter.MESSAGE_READ:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_READ");
                    break;

                case BixolonPrinter.MESSAGE_DEVICE_NAME:
                    debugTextView.setText(msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME));
                    Log.i("Handler", "BixolonPrinter.MESSAGE_DEVICE_NAME - " + msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME));
                    break;

                case BixolonPrinter.MESSAGE_TOAST:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_TOAST - " + msg.getData().getString("toast"));
                    // Toast.makeText(getApplicationContext(), msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
                    break;

                // The list of paired printers
                case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET");
                    if (msg.obj == null) {
                        updateScreenStatus(layoutThereArentPairedPrinters);
                    } else {
                        Set<BluetoothDevice> pairedDevices = (Set<BluetoothDevice>) msg.obj;
                        for (BluetoothDevice device : pairedDevices) {
                            if (!pairedPrinters.contains(device.getAddress())) {
                                pairedPrinters.add(device.getAddress());
                            }
                            if (pairedPrinters.size() == 1) {
                                PrintingActivity.bixolonPrinterApi.connect(pairedPrinters.get(0));
                            }
                        }
                    }
                    break;

                case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_PRINT_COMPLETE");
                    break;

                case BixolonPrinter.MESSAGE_COMPLETE_PROCESS_BITMAP:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_COMPLETE_PROCESS_BITMAP");
                    break;

                case MESSAGE_START_WORK:
                    Log.i("Handler", "MESSAGE_START_WORK");
                    break;

                case MESSAGE_END_WORK:
                    Log.i("Handler", "MESSAGE_END_WORK");
                    break;

                case BixolonPrinter.MESSAGE_USB_DEVICE_SET:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_USB_DEVICE_SET");
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
                    } else {
                        // DialogManager.showUsbDialog(MainActivity.this,
                        // (Set<UsbDevice>) msg.obj, mUsbReceiver);
                    }
                    break;

                case BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET");
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No connectable device", Toast.LENGTH_SHORT).show();
                    }
                    // DialogManager.showNetworkDialog(PrintingActivity.this, (Set<String>) msg.obj);
                    break;
            }
        }
    };

    class PairWithPrinterTask extends AsyncTask<Void, Void, Void> {

        boolean running = true;

        public PairWithPrinterTask() {

        }

        public void stop() {
            running = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (running) {
                if (connectedPrinter == null || connectedPrinter == false) {
                    publishProgress();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        int action = 0;

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (action < 20) {
                bixolonPrinterApi.findBluetoothPrinters();
                action++;
            } else {
                bixolonPrinterApi.disconnect();
                action = 0;
            }
        }
    }

    private void printText(String textToPrint) {
        printText(textToPrint, BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.TEXT_ATTRIBUTE_FONT_C);
    }

    private void printText(String textToPrint, int alignment) {
        printText(textToPrint, alignment, BixolonPrinter.TEXT_ATTRIBUTE_FONT_C);
    }

    private void printText(String textToPrint, int alignment, int attribute) {

        if (textToPrint.length() <= LINE_CHARS) {
            bixolonPrinterApi.printText(textToPrint, alignment, attribute, BixolonPrinter.TEXT_SIZE_HORIZONTAL1, false);
        } else {
            String textToPrintInNextLine = null;
            while (textToPrint.length() > LINE_CHARS) {
                textToPrintInNextLine = textToPrint.substring(0, LINE_CHARS);
                textToPrintInNextLine = textToPrintInNextLine.substring(0, textToPrintInNextLine.lastIndexOf(" ")).trim() + "\n";
                bixolonPrinterApi.printText(textToPrintInNextLine, alignment, attribute, BixolonPrinter.TEXT_SIZE_HORIZONTAL1, false);
                textToPrint = textToPrint.substring(textToPrintInNextLine.length(), textToPrint.length());
            }
            bixolonPrinterApi.printText(textToPrint, alignment, attribute, BixolonPrinter.TEXT_SIZE_HORIZONTAL1, false);
        }
    }

    /**
     * Print the common two columns ticket style text. Label+Value.
     *
     * @param leftText
     * @param rightText
     */
    private void printTextTwoColumns(String leftText, String rightText) {
        if (leftText.length() + rightText.length() + 1 > LINE_CHARS) { // If two Strings cannot fit in same line
            int alignment = BixolonPrinter.ALIGNMENT_LEFT;
            int attribute = 0;
            attribute |= BixolonPrinter.TEXT_ATTRIBUTE_FONT_C;
            bixolonPrinterApi.printText(leftText, alignment, attribute, BixolonPrinter.TEXT_SIZE_HORIZONTAL1, false);

            alignment = BixolonPrinter.ALIGNMENT_RIGHT;
            attribute = 0;
            attribute |= BixolonPrinter.TEXT_ATTRIBUTE_FONT_C;
            bixolonPrinterApi.printText(rightText, alignment, attribute, BixolonPrinter.TEXT_SIZE_HORIZONTAL1, false);
        } else {
            int padding = LINE_CHARS - leftText.length() - rightText.length();
            String paddingChar = " ";
            for (int i = 0; i < padding; i++) {
                paddingChar = paddingChar.concat(" ");
            }

            int alignment = BixolonPrinter.ALIGNMENT_LEFT;
            int attribute = 0;
            attribute |= BixolonPrinter.TEXT_ATTRIBUTE_FONT_C;
            bixolonPrinterApi.printText(leftText + paddingChar + rightText, alignment, attribute, BixolonPrinter.TEXT_SIZE_HORIZONTAL1, false);
        }
    }

    boolean animated = false;

    public void iconLoadingStart() {
        View loading = findViewById(R.id.loading);
        if (loading != null && !animated) {
            loading.startAnimation(rotation);
            loading.setVisibility(View.VISIBLE);
        }

        if (loading == null) {
            setProgressBarIndeterminateVisibility(Boolean.TRUE);
        }
        animated = true;
    }

    public void iconLoadingStop() {
        setProgressBarIndeterminateVisibility(Boolean.FALSE);

        View loading = findViewById(R.id.loading);
        if (loading != null) {
            loading.clearAnimation();
            loading.setVisibility(View.INVISIBLE);
        }
        animated = false;
    }
}