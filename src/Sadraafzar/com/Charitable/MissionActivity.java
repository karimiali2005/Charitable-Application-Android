package Sadraafzar.com.Charitable;

import Classess.app;
import Classess.dbConnector;
import Model.BoxModel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 6/12/2017.
 */
public class MissionActivity extends Activity {

    Spinner spinnerMission,spinnerLocation;
    dbConnector db;
    List<StringWithTag> itemListMission;
    ArrayAdapter<StringWithTag> spinnerAdapterMission;
    GridView gridView1;
    ImageButton imgBtnBack;
    CheckBox chkNotDisharge;
    TextView lblCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missionlayout);


        lblCount=(TextView)findViewById(R.id.lblCount);

        imgBtnBack=(ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        db = new dbConnector(this, app.database.DBNAME , null ,1);

        itemListMission = new ArrayList<StringWithTag>();

        spinnerMission = (Spinner) findViewById(R.id.spinnerMission);

      spinnerAdapterMission = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemListMission){

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

        spinnerAdapterMission.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinnerMission.setAdapter(spinnerAdapterMission);


        spinnerMission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                StringWithTag swt = (StringWithTag) spinnerMission.getItemAtPosition(position);
                String key = (String) swt.tag;
                String[] separated = key.split(":");
                BindData(Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2]));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        gridView1 = (GridView)findViewById(R.id.gridView);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                ContextThemeWrapper ctw = new ContextThemeWrapper(getApplicationContext(), R.style.CustomPopupTheme);
                PopupMenu popupMenu = new PopupMenu(ctw,arg1);
                popupMenu.getMenuInflater().inflate(R.menu.item_gridview_menu,popupMenu.getMenu());



                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        TextView tv =(TextView) arg1.findViewById(R.id.lblBoxCode);//your textview id
                        switch (item.getItemId()) {
                            case R.id.item1:
                                int BoxID = Integer.valueOf(String.valueOf(tv.getTag()));
                                Intent intent1 = new Intent(getApplicationContext(), BoxDichargeItemActivity.class);
                                intent1.putExtra("BoxID", String.valueOf(BoxID));
                                startActivity(intent1);

                                return true;

                            case R.id.item2:
                                Intent intent2 = new Intent(getApplicationContext(), BoxInfoActivity.class);
                                intent2.putExtra("BoxCode",tv.getText().toString());
                                startActivity(intent2);
                                return true;

                            case R.id.item3:
                                Intent intent3 = new Intent(getApplicationContext(), BoxEditActivity.class);
                                intent3.putExtra("BoxCode", tv.getText().toString());
                                startActivity(intent3);

                                return true;

                            case R.id.item4:
                                Intent intent4 = new Intent(getApplicationContext(), BoxMovementActivity.class);
                                intent4.putExtra("BoxCode", tv.getText().toString());
                                startActivity(intent4);
                                return true;
                            case R.id.item5:
                                Intent intent5 = new Intent(getApplicationContext(), BoxReportActivity.class);
                                intent5.putExtra("BoxCode", tv.getText().toString());
                                startActivity(intent5);
                                return true;
                        }
                        return false;


                    }
                });
                popupMenu.show();

            }

        });

        chkNotDisharge=(CheckBox)findViewById(R.id.chkNotDisharge);
        chkNotDisharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int positionMission = spinnerMission.getSelectedItemPosition();
                if(positionMission>-1) {
                    StringWithTag swt = (StringWithTag) spinnerMission.getItemAtPosition(positionMission);

                    String key = (String) swt.tag;
                    String[] separated = key.split(":");
                    BindData(Integer.parseInt(separated[0]), Integer.parseInt(separated[1]), Integer.parseInt(separated[2]));
                }
            }
        });

    }



    @Override
    protected void onResume() {

        LoadSpinnerMission();
        int positionMission = spinnerMission.getSelectedItemPosition();
        if(positionMission>-1) {
            StringWithTag swt = (StringWithTag) spinnerMission.getItemAtPosition(positionMission);

            String key = (String) swt.tag;
            String[] separated = key.split(":");
            BindData(Integer.parseInt(separated[0]), Integer.parseInt(separated[1]), Integer.parseInt(separated[2]));
        }
        super.onResume();


    }


    private void LoadSpinnerMission()
    {
        itemListMission.clear();

        String query  = "SELECT  DISTINCT PKIDtblBoxDichargeHeader, fld_DischargeNo, fk_AreaID, fld_Location,fld_AreaTitle  FROM tblBox INNER JOIN\n" +
                "                      tblArea ON tblBox.fk_AreaID = tblArea.PKID where PKIDtblBoxDichargeHeader IS Not Null ";

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {

            int key = c.getInt(c.getColumnIndex("PKIDtblBoxDichargeHeader"));
            int keyAreaID = c.getInt(c.getColumnIndex("fk_AreaID"));
            int keyLocation = c.getInt(c.getColumnIndex("fld_Location"));
            String value = c.getString(c.getColumnIndex("fld_DischargeNo"))+"-"+c.getString(c.getColumnIndex("fld_AreaTitle"))+"-"+keyLocation;
            StringWithTag item=new StringWithTag(value,key+":"+keyAreaID+":"+keyLocation);
            itemListMission.add(item);
        }

        spinnerAdapterMission.notifyDataSetChanged();
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
    private void BindData(int PKIDtblBoxDichargeHeader,int AreaID,int Location)
    {

        List<BoxModel> list = new ArrayList<BoxModel>();
        list.clear();
        String query="";
        if(chkNotDisharge.isChecked())
        {
            query  = "SELECT PKIDtblBox, fld_Code, fld_Order,fld_MainStreet,fld_ByStreet,fld_Alley,fld_Impasse ,fld_Transferee,fld_street3,fld_alley3,fld_Descr,fld_AddressNote,fld_No,fld_Floor,fld_UnitNo FROM tblBox  where PKIDtblBoxDichargeHeader="+PKIDtblBoxDichargeHeader+" and fk_AreaID="+AreaID+" and fld_Location="+Location+" and PKIDtblBox not in (select fk_BoxID from tblBoxDischargeItem)"
                    + " and PKIDtblBox not in (select fk_BoxID from tblBoxReport)"

                    +" order by  fld_Order";
        }
        else
        {
            query  = "SELECT PKIDtblBox, fld_Code, fld_Order,fld_MainStreet,fld_ByStreet,fld_Alley,fld_Impasse ,fld_Transferee,fld_street3,fld_alley3,fld_Descr,fld_AddressNote,fld_No,fld_Floor,fld_UnitNo FROM tblBox  where PKIDtblBoxDichargeHeader="+PKIDtblBoxDichargeHeader+" and fk_AreaID="+AreaID+" and fld_Location="+Location+" order by  fld_Order";
        }


        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {
            BoxModel item=new BoxModel();
            item.PKIDtblBox=c.getInt(c.getColumnIndex("PKIDtblBox"));
            item.fld_Code=c.getString(c.getColumnIndex("fld_Code"));
            item.fld_Order=c.getInt(c.getColumnIndex("fld_Order"));
            item.fld_MainStreet=c.getString(c.getColumnIndex("fld_MainStreet"));
            item.fld_ByStreet=c.getString(c.getColumnIndex("fld_ByStreet"));
            item.fld_Alley=c.getString(c.getColumnIndex("fld_Alley"));
            item.fld_Impasse=c.getString(c.getColumnIndex("fld_Impasse"));
            item.fld_Transferee=c.getString(c.getColumnIndex("fld_Transferee"));
            item.fld_street3=c.getString(c.getColumnIndex("fld_street3"));
            item.fld_alley3=c.getString(c.getColumnIndex("fld_alley3"));
            item.fld_Descr=c.getString(c.getColumnIndex("fld_Descr"));
            item.fld_AddressNote=c.getString(c.getColumnIndex("fld_AddressNote"));
            item.fld_No=c.getString(c.getColumnIndex("fld_No"));
            item.fld_Floor=c.getString(c.getColumnIndex("fld_Floor"));
            item.fld_UnitNo=c.getString(c.getColumnIndex("fld_UnitNo"));
            list.add(item);
        }

        gridView1.setAdapter(  new CustomGridAdapter( this, list ) );
    }
    private boolean ExistsDishargeItem(int BoxID)
    {
        String query  = "SELECT * FROM tblBoxDischargeItem where fk_BoxID="+BoxID;

        Cursor c = null;
        c = db.select(query);

        if(c.moveToNext()) {
            c.close();
         return true;
        }
        else
        {
            c.close();
            return false;
        }
    }
    class CustomGridAdapter extends BaseAdapter {

        private Context context;
        private final List<BoxModel> boxModels;

        public CustomGridAdapter(Context context, List<BoxModel> boxModels) {
            this.context = context;
            this.boxModels  = boxModels;
        }

        @Override
        public int getCount() {
            lblCount.setText(String.valueOf(boxModels.size()));
            return boxModels.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        // Number of times getView method call depends upon gridValues.length
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView;
            if (convertView == null) {
                gridView = inflater.inflate(R.layout.missiongridviewlayout, null);

            } else {
                gridView = (View) convertView;
            }
            if (position % 2 == 0)
                gridView.setBackgroundDrawable(getResources().getDrawable(R.drawable.glass_back_1));
            else
                gridView.setBackgroundDrawable(getResources().getDrawable(R.drawable.glass_back_2));

            BoxModel box = boxModels.get(position);

            TextView lblBoxCode, lblOrder, lblMainStreet,lblTransfereeValue;

            lblBoxCode = (TextView)gridView.findViewById(R.id.lblBoxCode);
            lblMainStreet = (TextView)gridView.findViewById(R.id.lblMainStreet);
            lblTransfereeValue=(TextView)gridView.findViewById(R.id.lblTransfereeValue);




            lblBoxCode.setText(String.valueOf(box.fld_Code));
            lblBoxCode.setTag(box.PKIDtblBox);

            String address="";
            String str=box.fld_MainStreet;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address="خ "+str;
            }

            str=box.fld_ByStreet;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-خ "+str;
            }
            str=box.fld_Alley;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-ک "+str;
            }
            str=box.fld_Impasse;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-ک "+str;
            }
            str=box.fld_street3;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-خ "+str;
            }
            str=box.fld_alley3;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-ک "+str;
            }
            str=box.fld_Descr;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-ت "+str;
            }
            str=box.fld_AddressNote;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-ت "+str;
            }
            str=box.fld_No;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-پ "+str;
            }
            str=box.fld_Floor;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-ط "+str;
            }
            str=box.fld_UnitNo;
            if (!(str == null || str.isEmpty() || str.equalsIgnoreCase("null")))
            {
                address+="-و "+str;
            }



            //lblMainStreet.setText(String.valueOf(box.fld_MainStreet+" "+box.fld_ByStreet+" "+box.fld_Alley+" "+box.fld_Impasse));
            lblMainStreet.setText(address);
           lblTransfereeValue.setText(String.valueOf(box.fld_Transferee));

            ImageView imgDishargeItem;
            imgDishargeItem=(ImageView)gridView.findViewById(R.id.imgDisharge);
            if(ExistsDishargeItem(box.PKIDtblBox))
            {
                imgDishargeItem.setImageResource(R.drawable.mainpage_delsafe_iocn_disharge);
            }
            else
            {
                imgDishargeItem.setImageResource(R.drawable.mainpage_delsafe_iocn);
            }
            imgDishargeItem.setTag(box.PKIDtblBox);
            imgDishargeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int BoxID = Integer.valueOf(String.valueOf(v.getTag()));
                       Intent intent = new Intent(getApplicationContext(), BoxDichargeItemActivity.class);
                       intent.putExtra("BoxID", String.valueOf(BoxID));
                       startActivity(intent);

                }
            });

            return gridView;
        }
    }
}