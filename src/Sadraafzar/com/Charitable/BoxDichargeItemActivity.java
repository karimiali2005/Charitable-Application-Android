package Sadraafzar.com.Charitable;

import Classess.GPSTracker;
import Classess.app;
import Classess.dbConnector;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 6/9/2017.
 */
public class BoxDichargeItemActivity extends Activity {
    Button btnSave,btnSavePrint;
    ImageButton btnNext,btnPrevious,btnInfo,btnBoxEditShow,btnBoxMovementShow,btnBoxReportShow,imgSetBoxCode,imgBtnBack;
    Spinner spinner;
    dbConnector db;
    List<StringWithTag> itemList = new ArrayList<StringWithTag>();
    ArrayAdapter<StringWithTag> spinnerAdapter;
    String fld_BoxStatus="",fld_BoxLevel="",fld_Descr="",fld_Code="",fld_Address="",fld_Transferee="";
    EditText txtBoxCode,txtFishNo,txtPaperPrice,txtPiecePrice,txtDescription;
    TextView lblStatusValue,lblDescrValue,lblTransfereeValue;
    int PKIDtblBox=0,fk_HeaderID=0,EtiableBoxDichargeItem=0,fk_AreaID=0,fld_Location=0;
    boolean flagFirst=false;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boxdichargeitemlayout);

        flagFirst=true;


        db = new dbConnector(this, app.database.DBNAME , null ,1);


        //region spinner sarfasl
        itemList = new ArrayList<StringWithTag>();

        spinner = (Spinner) findViewById(R.id.spinner);

        spinnerAdapter = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemList){

            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setGravity(Gravity.RIGHT);

                return view;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.SpinnerTextColor));
                text.setTextSize(15);
                text.setGravity(Gravity.RIGHT);

                return view;

            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                if(!txtBoxCode.getText().toString().equals("") )
                {
                    BoxSearch(txtBoxCode.getText().toString());
                    if (!fld_BoxLevel.equals("")) {
                        lblTransfereeValue.setText(fld_Transferee);
                       // lblStatusValue.setText(fld_BoxStatus + "-" + fld_BoxLevel);
                        lblDescrValue.setText(fld_Descr);

                        Fill(fk_HeaderID,PKIDtblBox);
                        FillFishNumber();
                    }
                    else {
                       // lblStatusValue.setText(getBaseContext().getString(R.string.NotFound));
                        lblTransfereeValue.setText(getBaseContext().getString(R.string.NotFound));
                        lblDescrValue.setText(getBaseContext().getString(R.string.NotFound));
                    }
                }
                else
                {
                    lblTransfereeValue.setText("");
                    //lblStatusValue.setText("");
                    lblDescrValue.setText("");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //endregion

        lblTransfereeValue=(TextView)findViewById(R.id.lblTransfereeValue);
       // lblStatusValue=(TextView)findViewById(R.id.lblStatusValue);
        lblDescrValue=(TextView)findViewById(R.id.lblDescrValue);

        //region TextBoxCode
        txtBoxCode=(EditText)findViewById(R.id.txtBoxCode);
        txtBoxCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("") )
                {
                    BoxSearch(s.toString());
                    if (!fld_BoxLevel.equals("")) {
                        lblTransfereeValue.setText(fld_Transferee);
                        //lblStatusValue.setText(fld_BoxStatus + "-" + fld_BoxLevel);
                        lblDescrValue.setText(fld_Descr);

                        Fill(fk_HeaderID,PKIDtblBox);
                        FillFishNumber();
                    }
                    else {
                      //  lblStatusValue.setText(getBaseContext().getString(R.string.NotFound));
                        lblTransfereeValue.setText(getBaseContext().getString(R.string.NotFound));
                        lblDescrValue.setText(getBaseContext().getString(R.string.NotFound));
                    }
                }
                else
                {
                    lblTransfereeValue.setText("");
                   // lblStatusValue.setText("");
                    lblDescrValue.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion



        //region Info
        btnInfo=(ImageButton)findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoxInfoActivity.class);
                intent.putExtra("BoxCode", txtBoxCode.getText().toString());
                startActivity(intent);

            }
        });
        //endregion



        //region Next
        btnNext=(ImageButton)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtBoxCode.getText().toString().equals("") )
                {
                    BoxNextSearch(txtBoxCode.getText().toString());
                    if(PKIDtblBox!=0)
                    {
                        txtBoxCode.setText(fld_Code);
                    }
                    else
                        Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.NotFound), Toast.LENGTH_SHORT).show();

                }

            }
        });
        //endregion

        //region Previous
        btnPrevious=(ImageButton)findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtBoxCode.getText().toString().equals("") )
                {
                    BoxPreviousSearch(txtBoxCode.getText().toString());
                    if(PKIDtblBox!=0)
                    {
                        txtBoxCode.setText(fld_Code);
                    }
                    else
                        Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.NotFound), Toast.LENGTH_SHORT).show();

                }

            }
        });
        //endregion

        //region Save
        txtFishNo=(EditText)findViewById(R.id.txtFishNo);
        txtPaperPrice=(EditText)findViewById(R.id.txtPaperPrice);
        txtPiecePrice=(EditText)findViewById(R.id.txtPiecePrice);
        txtDescription=(EditText)findViewById(R.id.txtDescription);


        btnSave = (Button) findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Save()) {
                        Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.SuccessInserted), Toast.LENGTH_SHORT).show();
                        btnNext.performClick();
                    }

                }
            });
        //endregion

        btnBoxEditShow = (ImageButton) findViewById(R.id.btnBoxEditShow);
        btnBoxEditShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoxEditActivity.class);
                intent.putExtra("BoxCode", txtBoxCode.getText().toString());
                startActivity(intent);
            }
        });

        btnBoxMovementShow = (ImageButton) findViewById(R.id.btnBoxMovementShow);
        btnBoxMovementShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoxMovementActivity.class);
                intent.putExtra("BoxCode", txtBoxCode.getText().toString());
                startActivity(intent);
            }
        });

        btnBoxReportShow = (ImageButton) findViewById(R.id.btnBoxReportShow);
        btnBoxReportShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoxReportActivity.class);
                intent.putExtra("BoxCode", txtBoxCode.getText().toString());
                startActivity(intent);
            }
        });

        imgSetBoxCode = (ImageButton) findViewById(R.id.imgSetBoxCode);
        imgSetBoxCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtBoxCode.getText().toString().equals("")) {
                    int code = Integer.parseInt(txtBoxCode.getText().toString());
                    if(code<1000000)
                    {
                        code+=1000000;
                        txtBoxCode.setText(String.valueOf(code));
                    }
                }

            }
        });

        txtBoxCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(!txtBoxCode.getText().toString().equals("")) {
                        int code = Integer.parseInt(txtBoxCode.getText().toString());
                        if(code<1000000)
                        {
                            code+=1000000;
                            txtBoxCode.setText(String.valueOf(code));
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        imgBtnBack=(ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        btnSavePrint = (Button) findViewById(R.id.btnSavePrint);
        btnSavePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Save()||EtiableBoxDichargeItem==1) {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.SuccessInserted), Toast.LENGTH_SHORT).show();
                    if(app.Printer.IsBIXOLON) {
                        Intent intent = new Intent(getApplicationContext(), PrintingActivity.class);
                        intent.putExtra("fk_BoxID", String.valueOf(PKIDtblBox));
                        intent.putExtra("fk_HeaderID", String.valueOf(fk_HeaderID));
                        int position = spinner.getSelectedItemPosition();
                        StringWithTag swt = (StringWithTag) spinner.getItemAtPosition(position);
                        Integer key = (Integer) swt.tag;
                        intent.putExtra("fk_SarFasl", String.valueOf(key));
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), PrintRongtaActivity.class);
                        intent.putExtra("fk_BoxID", String.valueOf(PKIDtblBox));
                        intent.putExtra("fk_HeaderID", String.valueOf(fk_HeaderID));
                        int position = spinner.getSelectedItemPosition();
                        StringWithTag swt = (StringWithTag) spinner.getItemAtPosition(position);
                        Integer key = (Integer) swt.tag;
                        intent.putExtra("fk_SarFasl", String.valueOf(key));
                        startActivity(intent);
                    }
                }
            }
        });



        }
    private static class StringWithTag {
        public String string;
        public Object tag;

        public StringWithTag(String string, Object tag) {
            this.string = string;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return string;
        }
    }


    @Override
    protected void onResume() {
        LoadSpinnerSarfasl();
        if (flagFirst) {
            BoxFirstSearch();
            flagFirst=false;
        }
        if(PKIDtblBox!=0)
        {
            txtBoxCode.setText(fld_Code);
        }


        Intent data=getIntent();
        if(data!=null)
        {
            String BoxCode=BoxCodeReturn(data.getStringExtra("BoxID"));
            if(BoxCode!="")
                txtBoxCode.setText(BoxCode);
        }

        super.onResume();
    }
    private void LoadSpinnerSarfasl()
    {
        itemList.clear();

        String query  = "SELECT PKID,fld_SarFasl FROM tblSarFasl ";

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {

            int key = c.getInt(c.getColumnIndex("PKID"));
            String value = c.getString(c.getColumnIndex("fld_SarFasl"));
            StringWithTag item=new StringWithTag(value,key);
            itemList.add(item);
        }

        spinnerAdapter.notifyDataSetChanged();
    }
    private String BoxCodeReturn(String BoxID)
    {
        String query  = "SELECT fld_Code,PKIDtblBox FROM tblBox where PKIDtblBox="+BoxID;

        Cursor c = null;
        c = db.select(query);
        if(c.moveToNext()) {
           String result= c.getString(c.getColumnIndex("fld_Code"));
            c.close();
            return result;
        }
        return "";
    }
    private void BoxSearch(String boxCode)
    {


        fld_Descr="";

        String query  = "SELECT PKIDtblBox,fk_LastStatus,fk_LevelID,fld_Descr,PKIDtblBoxDichargeHeader,fk_AreaID,fld_Location,fld_MainStreet,fld_Alley,fld_Impasse,fld_Transferee,fld_ByStreet,fld_street3,fld_alley3,fld_AddressNote,fld_No,fld_Floor,fld_UnitNo FROM tblBox where fld_Code="+boxCode;

        Cursor c = null;
        c = db.select(query);

        int levelID=0;
        int lastStatus=0;
        while(c.moveToNext()) {
            String str=c.getString(c.getColumnIndex("fld_MainStreet"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr="خ "+str;
            }

            str=c.getString(c.getColumnIndex("fld_ByStreet"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-خ "+str;
            }
            str=c.getString(c.getColumnIndex("fld_Alley"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-ک "+str;
            }
            str=c.getString(c.getColumnIndex("fld_Impasse"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-ک "+str;
            }
            str=c.getString(c.getColumnIndex("fld_street3"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-خ "+str;
            }
            str=c.getString(c.getColumnIndex("fld_alley3"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-ک "+str;
            }
            str=c.getString(c.getColumnIndex("fld_Descr"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-ت "+str;
            }
            str=c.getString(c.getColumnIndex("fld_AddressNote"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-ت "+str;
            }
            str=c.getString(c.getColumnIndex("fld_No"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-پ "+str;
            }
            str=c.getString(c.getColumnIndex("fld_Floor"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-ط "+str;
            }
            str=c.getString(c.getColumnIndex("fld_UnitNo"));
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                fld_Descr+="-و "+str;
            }


            levelID=c.getInt(c.getColumnIndex("fk_LevelID"));
            lastStatus=c.getInt(c.getColumnIndex("fk_LastStatus"));
            PKIDtblBox=c.getInt(c.getColumnIndex("PKIDtblBox"));
            fk_HeaderID=c.getInt(c.getColumnIndex("PKIDtblBoxDichargeHeader"));
            fk_AreaID=c.getInt(c.getColumnIndex("fk_AreaID"));
            fld_Location=c.getInt(c.getColumnIndex("fld_Location"));
            fld_Address=c.getString(c.getColumnIndex("fld_MainStreet"))+" "+c.getString(c.getColumnIndex("fld_Alley"))+" "+c.getString(c.getColumnIndex("fld_Impasse"));
            fld_Transferee=c.getString(c.getColumnIndex("fld_Transferee"));

        }
        if(levelID!=0) {
            query = "SELECT fld_BoxLevel FROM tblBoxLevel where PKID=" + levelID;
            c = db.select(query);
            while(c.moveToNext()) {
                fld_BoxLevel=c.getString(c.getColumnIndex("fld_BoxLevel"));
            }

        }
        else
        fld_BoxLevel="";

        if(lastStatus!=0) {
            query = "SELECT fld_BoxStatus FROM tblBoxStatus where PKID=" + lastStatus;
            c = db.select(query);
            while (c.moveToNext()) {
                fld_BoxStatus = c.getString(c.getColumnIndex("fld_BoxStatus"));
            }

        }
        else
        {
            fld_BoxStatus="";
        }

    }
    private void BoxFirstSearch()
    {
        fld_Descr="";
        String query  = "SELECT PKIDtblBox,fld_Code,PKIDtblBoxDichargeHeader,fk_AreaID,fld_Location FROM tblBox  ORDER BY fk_AreaID,fld_Location,fld_Order LIMIT 1";
        Cursor c = null;
        c = db.select(query);
        if(c.moveToNext()) {

            PKIDtblBox=c.getInt(c.getColumnIndex("PKIDtblBox"));
            fk_HeaderID=c.getInt(c.getColumnIndex("PKIDtblBoxDichargeHeader"));
            fld_Code=c.getString(c.getColumnIndex("fld_Code"));
        }
        else {
            PKIDtblBox = 0;
            fk_HeaderID=0;
            fld_Code="";
        }
    }
    private void BoxNextSearch(String boxCode)
    {
        int id=0;
        boolean flagNext=false;
        String query  = "SELECT id FROM tblBox where fld_Code="+boxCode;
        Cursor c = null;
        c = db.select(query);
        if(c.moveToNext()) {
            id=c.getInt(c.getColumnIndex("id"));
            if (id !=0)
                flagNext=true;
        }



        if(flagNext) {
            fld_Descr = "";
           query = "SELECT PKIDtblBox,fld_Code,PKIDtblBoxDichargeHeader,fk_AreaID,fld_Location FROM tblBox where id=" +(id+1);
            c = null;
            c = db.select(query);
            if (c.moveToNext()) {

                PKIDtblBox = c.getInt(c.getColumnIndex("PKIDtblBox"));
                fk_HeaderID = c.getInt(c.getColumnIndex("PKIDtblBoxDichargeHeader"));
                fld_Code = c.getString(c.getColumnIndex("fld_Code"));
            } else {
                PKIDtblBox = 0;
                fk_HeaderID = 0;
                fld_Code = "";
            }
        }
    }

    private void BoxPreviousSearch(String boxCode)
    {
        int id=0;
        boolean flagPrevious=false;
        String query  = "SELECT id FROM tblBox where fld_Code="+boxCode;
        Cursor c = null;
        c = db.select(query);
        if(c.moveToNext()) {
            id=c.getInt(c.getColumnIndex("id"));
            if (id !=0)
                flagPrevious=true;
        }
        if(flagPrevious) {
            fld_Descr = "";
            query = "SELECT PKIDtblBox,fld_Code,PKIDtblBoxDichargeHeader,fk_AreaID,fld_Location FROM tblBox where id=" +(id-1);
            c = null;
            c = db.select(query);
            if (c.moveToNext()) {

                PKIDtblBox = c.getInt(c.getColumnIndex("PKIDtblBox"));
                fk_HeaderID = c.getInt(c.getColumnIndex("PKIDtblBoxDichargeHeader"));
                fld_Code = c.getString(c.getColumnIndex("fld_Code"));
            } else {
                PKIDtblBox = 0;
                fk_HeaderID = 0;
                fld_Code = "";
            }
        }
    }
    private boolean Save() {

        GPSTracker gps = new GPSTracker(BoxDichargeItemActivity.this);
        double latitude=0;
        double longitude=0;
        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


        }else{
            new AlertDialog.Builder(this)
                    .setTitle(getBaseContext().getString(R.string.NoGPSON))//"No Internet Connection")
                            //.setMessage("It looks like your internet connection is off. Please turn it " +
                            //"on and try again")
                    .setMessage(getBaseContext().getString(R.string.GPSONPlease))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
            //return false;
        }



        Boolean status=false;
        if(EtiableBoxDichargeItem==1)
        {
            /*String strFilter="fk_HeaderID="+fk_HeaderID+" and fk_BoxID="+PKIDtblBox;
            status = db.update("tblBoxDischargeItem", values,strFilter);*/

            Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.NoEdit), Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            ContentValues values = new ContentValues();

            values.put("fk_HeaderID", fk_HeaderID);
            values.put("fk_AreaID", fk_AreaID);
            values.put("fld_Location", fld_Location);
            values.put("fld_FishNo", txtFishNo.getText().toString());
            values.put("fk_BoxID", PKIDtblBox);
            values.put("fld_DischargeTime",app.now("HH:mm"));
            values.put("fld_PiecePrice",Integer.parseInt(txtPiecePrice.getText().toString()));
            values.put("fld_description",txtDescription.getText().toString());
            values.put("fld_PaperPrice", Integer.parseInt(txtPaperPrice.getText().toString()));
            int position = spinner.getSelectedItemPosition();
            StringWithTag swt = (StringWithTag) spinner.getItemAtPosition(position);
            Integer key = (Integer) swt.tag;
            values.put("fk_SarFasl", key.toString());
            values.put("fld_Address", fld_Address);
            values.put("fld_Transferee", fld_Transferee);
            values.put("fld_Adder", "app");
            values.put("fld_AddDate", app.now("yyyy-MM-dd HH:mm:ss.SSS"));
            values.put("fld_latitude", latitude);
            values.put("fld_longitude", longitude);

            status = db.insert("tblBoxDischargeItem", values);
            EtiableBoxDichargeItem=1;
        }

        //Update Location Box
        ContentValues values2 = new ContentValues();
        values2.put("fld_latitude",latitude);
        values2.put("fld_longitude",longitude);
        values2.put("fk_RowStatus",3);
        String strFilter2="PKIDtblBox="+PKIDtblBox;
        status = db.update("tblBox", values2,strFilter2);


        return status;
    }

    private void FillFishNumber()
    {
        if(txtFishNo.getText().toString().equals("")) {
            String query = "SELECT Max(fld_FishNo) As fld_FishNo FROM tblBoxDischargeItem";
            Cursor c = null;
            c = db.select(query);
            if (c.moveToNext()) {
                String str =c.getString(c.getColumnIndex("fld_FishNo"));
                if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
                {
                    int serial=Integer.parseInt(str)+1;
                    txtFishNo.setText(String.valueOf(serial));
                }

            }
        }
    }

    private void Fill(int fk_HeaderID,int fk_BoxID)
    {
        String query="";
        int position = spinner.getSelectedItemPosition();
        if(position>-1) {
            StringWithTag swt = (StringWithTag) spinner.getItemAtPosition(position);
            Integer key = (Integer) swt.tag;
            query  = "SELECT * FROM tblBoxDischargeItem Where  fk_HeaderID="+fk_HeaderID+" and fk_BoxID="+fk_BoxID+" and fk_SarFasl="+key;
        }
        else
        {
            query  = "SELECT * FROM tblBoxDischargeItem Where  fk_HeaderID="+fk_HeaderID+" and fk_BoxID="+fk_BoxID+" and fk_SarFasl=1";
        }


        Cursor c = null;
        c = db.select(query);
        if(c.moveToNext()) {

            txtFishNo.setText( c.getString(c.getColumnIndex("fld_FishNo")));
            txtPaperPrice.setText(c.getString(c.getColumnIndex("fld_PaperPrice")));
            txtPiecePrice.setText(c.getString(c.getColumnIndex("fld_PiecePrice")));
            txtDescription.setText(c.getString(c.getColumnIndex("fld_description")));

            spinner.setSelection(c.getInt(c.getColumnIndex("fk_SarFasl"))-1);
            EtiableBoxDichargeItem=1;
        }
        else {
            txtFishNo.setText("");
            txtPaperPrice.setText("");
            txtPiecePrice.setText("");
            txtDescription.setText("");
            EtiableBoxDichargeItem=0;
        }
    }
}
