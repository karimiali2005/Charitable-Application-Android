package Sadraafzar.com.Charitable;

import Classess.app;
import Classess.dbConnector;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 6/14/2017.
 */
public class BoxReportActivity extends Activity {
    Spinner spinnerStatus,spinnerLevel;
    dbConnector db;
    List<StringWithTag> itemListStatus,itemListLevel = new ArrayList<StringWithTag>();
    ArrayAdapter<StringWithTag> spinnerAdapterStatus,spinnerAdapterLevel;
    Button btnSave;
    ImageButton imgSetBoxCode,imgBtnBack;
    TextView txtBoxCode,txtDescr;
    int EtiableBoxDichargeItem=0,PKIDtblBox=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boxreportlayout);

        db = new dbConnector(this, app.database.DBNAME , null ,1);

        itemListStatus = new ArrayList<StringWithTag>();

        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);

        spinnerAdapterStatus = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemListStatus){

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
                text.setTextSize(10);
                text.setGravity(Gravity.RIGHT);

                return view;

            }
        };

        spinnerAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapterStatus);


        itemListLevel = new ArrayList<StringWithTag>();

        spinnerLevel = (Spinner) findViewById(R.id.spinnerLevel);

        spinnerAdapterLevel = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemListLevel){

            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);

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

        spinnerAdapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(spinnerAdapterLevel);

        txtDescr=(TextView)findViewById(R.id.txtDescr);
        txtBoxCode=(TextView)findViewById(R.id.txtBoxCode);
        txtBoxCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("") )
                {
                    BoxSearch(s.toString());
                    if (PKIDtblBox!=0) {
                        Fill(PKIDtblBox);
                    }
                    else
                    {
                        txtDescr.setText("");
                    }

                }
                else
                {
                    txtDescr.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtBoxCode.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.PleaseBoxCode), Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(!BoxSearch(txtBoxCode.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.NotFoundBox), Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onResume() {

        LoadSpinnerStatus();
        LoadSpinnerLevel();
        Intent data=getIntent();
        if(data!=null)
        {
            String BoxCode=data.getStringExtra("BoxCode");
            if(BoxCode!="")
                txtBoxCode.setText(BoxCode);
        }
        super.onResume();
    }
    private void LoadSpinnerStatus()
    {
        itemListStatus.clear();

        String query  = "SELECT PKID,fld_BoxStatus  FROM tblBoxStatus  ";

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {

            int key = c.getInt(c.getColumnIndex("PKID"));
            String value = c.getString(c.getColumnIndex("fld_BoxStatus"));
            StringWithTag item=new StringWithTag(value,key);
            itemListStatus.add(item);
        }

        spinnerAdapterStatus.notifyDataSetChanged();
    }
    private void LoadSpinnerLevel()
    {
        itemListLevel.clear();

        String query  = "SELECT PKID, fld_BoxLevel  FROM tblBoxLevel  ";

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {

            int key = c.getInt(c.getColumnIndex("PKID"));
            String value = c.getString(c.getColumnIndex("fld_BoxLevel"));
            StringWithTag item=new StringWithTag(value,key);
            itemListLevel.add(item);
        }

        spinnerAdapterLevel.notifyDataSetChanged();
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

    private Boolean BoxSearch(String boxCode) {
        String query = "SELECT * FROM tblBox where fld_Code=" + boxCode;

        Cursor c = null;
        c = db.select(query);


        if (c.moveToNext()) {

            PKIDtblBox=c.getInt(c.getColumnIndex("PKIDtblBox"));
            return true;
        }
        return false;
    }



    private boolean Save()
    {
        try {


            String query  = "SELECT  fk_Agent1ID,fk_Agent2ID,fk_MissionID  FROM tblBox " ;

            Cursor c = null;
            c = db.select(query);
            int Agent1ID=0;
            int Agent2ID=0;
            int fk_MissionID=0;

            if(c.moveToNext()) {
                Agent1ID=c.getInt(c.getColumnIndex("fk_Agent1ID"));
                Agent2ID=c.getInt(c.getColumnIndex("fk_Agent2ID"));
                fk_MissionID=c.getInt(c.getColumnIndex("fk_MissionID"));

            }
            c.close();



            ContentValues values = new ContentValues();

            values.put("fk_BoxID", String.valueOf(PKIDtblBox));
            values.put("fld_Date",app.nowDate());
            values.put("fk_Agent1",Agent1ID );
            values.put("fk_Agent2",Agent2ID);
            int positionStatus = spinnerStatus.getSelectedItemPosition();
            StringWithTag swt = (StringWithTag) spinnerStatus.getItemAtPosition(positionStatus);
            Integer fk_StatusID = (Integer) swt.tag;
            values.put("fk_StatusID", fk_StatusID);

            int positionLevel = spinnerLevel.getSelectedItemPosition();
            StringWithTag swt2 = (StringWithTag) spinnerLevel.getItemAtPosition(positionLevel);
            Integer fk_LevelID = (Integer) swt2.tag;
            values.put("fk_LevelID", fk_LevelID);
            values.put("fld_AddDate", app.now("yyyy-MM-dd HH:mm:ss.SSS"));
            values.put("fld_Adder", "app");

            values.put("fld_Descr", txtDescr.getText().toString());
            values.put("fk_MissionID",fk_MissionID);


            Boolean status =false;
            if(EtiableBoxDichargeItem==1)
            {
                String strFilter="fk_BoxID="+PKIDtblBox;
                status = db.update("tblBoxReport", values,strFilter);
            }
            else
            {
                status =  db.insert("tblBoxReport", values);
                EtiableBoxDichargeItem=1;
            }


            return status;
        }
        catch (Exception ex)
        {
            return false;
        }

    }
    private void Fill(int fk_BoxID)
    {
        String query  = "SELECT * FROM tblBoxReport Where fk_BoxID="+fk_BoxID;
        Cursor c = null;
        c = db.select(query);
        if(c.moveToNext()) {

            txtDescr.setText( c.getString(c.getColumnIndex("fld_Descr")));
            spinnerLevel.setSelection(PositionList(itemListLevel,c.getShort (c.getColumnIndex("fk_LevelID"))));
            spinnerStatus.setSelection(PositionList(itemListStatus,c.getShort (c.getColumnIndex("fk_StatusID"))));
            EtiableBoxDichargeItem=1;
        }
        else {
            txtDescr.setText("");
            EtiableBoxDichargeItem=0;
        }
    }
    private int PositionList(List<StringWithTag> list,Object tag)
    {
        int position=-1;
        for(StringWithTag sw : list) //assume categories isn't null.
        {
            position++;

            if(tag.toString().equals(sw.tag.toString())) //assumes name isn't null.
            {
                return position;
            }
        }
        return -1;
    }


}