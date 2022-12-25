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
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 6/5/2017.
 */
public class BoxNewActivity extends Activity {
    Button btnSave;
    ImageButton imgSetBoxCode,imgBtnBack;
    dbConnector db;
    Spinner spinnerLocationType,spinnerArea;
    List<StringWithTag> itemListLocationType,itemListArea= new ArrayList<StringWithTag>();
    ArrayAdapter<StringWithTag> spinnerAdapterLocationType,spinnerAdapterArea;
    TextView txtBoxCode,txtTransferee,txtMainStreet,txtByStreet,txtAlley,txtImpasse,txtPlaqueNo,txtFloor,txtUnitNo,txtjob,txtPOBOX,txtHomeTel,txtWorkTel;
    TextView txtMobile,txtDescr,txtAddressNote,txtstreet3,txtalley3,txtLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boxnewlayout);

        db = new dbConnector(this, app.database.DBNAME , null ,1);

        itemListLocationType = new ArrayList<StringWithTag>();

        spinnerLocationType = (Spinner) findViewById(R.id.spinnerLocationType);

        spinnerAdapterLocationType = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemListLocationType){

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

        spinnerAdapterLocationType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocationType.setAdapter(spinnerAdapterLocationType);

        itemListArea = new ArrayList<StringWithTag>();

        spinnerArea = (Spinner) findViewById(R.id.spinnerArea);

        spinnerAdapterArea = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemListArea){

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

        spinnerAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(spinnerAdapterArea);

        txtBoxCode=(TextView)findViewById(R.id.txtBoxCode);
        txtTransferee=(TextView)findViewById(R.id.txtTransferee);
        txtMainStreet=(TextView)findViewById(R.id.txtMainStreet);
        txtByStreet=(TextView)findViewById(R.id.txtByStreet);
        txtAlley=(TextView)findViewById(R.id.txtAlley);
        txtImpasse=(TextView)findViewById(R.id.txtImpasse);
        txtPlaqueNo=(TextView)findViewById(R.id.txtPlaqueNo);
        txtFloor=(TextView)findViewById(R.id.txtFloor);
        txtUnitNo=(TextView)findViewById(R.id.txtUnitNo);
        txtjob=(TextView)findViewById(R.id.txtjob);
        txtPOBOX=(TextView)findViewById(R.id.txtPOBOX);
        txtHomeTel=(TextView)findViewById(R.id.txtHomeTel);
        txtWorkTel=(TextView)findViewById(R.id.txtWorkTel);
        txtMobile=(TextView)findViewById(R.id.txtMobile);
        txtDescr=(TextView)findViewById(R.id.txtDescr);
        txtAddressNote=(TextView)findViewById(R.id.txtAddressNote);
        txtstreet3=(TextView)findViewById(R.id.txtstreet3);
        txtalley3=(TextView)findViewById(R.id.txtalley3);
        txtLocation=(TextView)findViewById(R.id.txtLocation);

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


        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtBoxCode.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.PleaseBoxCode), Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (Save()) {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.SuccessInserted), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.FailedInserted), Toast.LENGTH_LONG).show();
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

        LoadSpinnerLocationType();
        LoadSpinnerArea();
        super.onResume();
    }
    private void LoadSpinnerLocationType()
    {
        itemListLocationType.clear();

        String query  = "SELECT PKID,fld_LocationType  FROM tblLocationType  ";

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {

            int key = c.getInt(c.getColumnIndex("PKID"));
            String value = c.getString(c.getColumnIndex("fld_LocationType"));
            StringWithTag item=new StringWithTag(value,key);
            itemListLocationType.add(item);
        }

        spinnerAdapterLocationType.notifyDataSetChanged();
    }

    private void LoadSpinnerArea()
    {
        itemListArea.clear();


        String query  = "SELECT  PKID, fld_AreaTitle  FROM tblArea " ;

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {

            int key = c.getInt(c.getColumnIndex("PKID"));
            String value = c.getString(c.getColumnIndex("fld_AreaTitle"));
            StringWithTag item=new StringWithTag(value,key);
            itemListArea.add(item);
        }

        spinnerAdapterArea.notifyDataSetChanged();
    }
    private boolean Save()
    {
        try {

            GPSTracker gps = new GPSTracker(BoxNewActivity.this);
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
               // return false;
            }

            String query  = "SELECT  fk_Agent1ID,fk_Agent2ID  FROM tblBox " ;

            Cursor c = null;
            c = db.select(query);
            int Agent1ID=0;
            int Agent2ID=0;

            if(c.moveToNext()) {
                Agent1ID=c.getInt(c.getColumnIndex("fk_Agent1ID"));
                Agent2ID=c.getInt(c.getColumnIndex("fk_Agent2ID"));

            }
            c.close();



            ContentValues values = new ContentValues();

            values.put("fld_Code", txtBoxCode.getText().toString());
            values.put("fld_AssignDate",app.nowDate());
            values.put("fld_Transferee", txtTransferee.getText().toString());
            int positionLocationType = spinnerLocationType.getSelectedItemPosition();
            StringWithTag swt = (StringWithTag) spinnerLocationType.getItemAtPosition(positionLocationType);
            Integer fk_LocationType = (Integer) swt.tag;
            values.put("fk_LocationType",fk_LocationType);
            values.put("fld_MainStreet",txtMainStreet.getText().toString());
            values.put("fld_ByStreet", txtByStreet.getText().toString());
            values.put("fld_Alley", txtAlley.getText().toString());
            values.put("fld_Impasse", txtImpasse.getText().toString());
            values.put("fld_No", txtPlaqueNo.getText().toString());
            values.put("fld_Floor", txtFloor.getText().toString());
            values.put("fld_UnitNo", txtUnitNo.getText().toString());
            values.put("fld_job", txtjob.getText().toString());
            values.put("fld_POBOX",txtPOBOX.getText().toString());
            values.put("fld_HomeTel", txtHomeTel.getText().toString());
            values.put("fld_WorkTel", txtWorkTel.getText().toString());
            values.put("fld_Mobile", txtMobile.getText().toString());
            values.put("fk_LastStatus",2);
            values.put("fk_LevelID", 6);
            values.put("fk_Agent1IDtblBox",Agent1ID );
            values.put("fk_Agent2IDtblBox",Agent2ID);
            values.put("fk_AssignType", 1);//???
            values.put("fld_Descr", txtDescr.getText().toString());
            values.put("fk_RowStatus",2);//1 Show-2 Add-3 Edit-4 Movement
            values.put("fld_Adder", "app");
            values.put("fld_AddDate", app.now("yyyy-MM-dd HH:mm:ss.SSS"));
            values.put("fld_AddressNote", txtAddressNote.getText().toString());
            values.put("fld_street3", txtAddressNote.getText().toString());
            values.put("fld_alley3", txtalley3.getText().toString());
            int positionArea = spinnerArea.getSelectedItemPosition();
            StringWithTag swt2 = (StringWithTag) spinnerArea.getItemAtPosition(positionArea);
            Integer fk_AreaID = (Integer) swt2.tag;
            values.put("fk_AreaID", fk_AreaID);
            if(!txtLocation.getText().toString().equals(""))
            values.put("fld_Location", Integer.parseInt(txtLocation.getText().toString()));
            values.put("fld_latitude", latitude);
            values.put("fld_longitude", longitude);


            Boolean status = db.insert("tblBox", values);

            return status;
        }
        catch (Exception ex)
        {
           return false;
        }

    }
}