package Sadraafzar.com.Charitable;

import Classess.app;
import Classess.dbConnector;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 6/19/2017.
 */
public class BoxInfoActivity extends Activity {
    dbConnector db;
    TextView lblBoxCode,lblLocationType,lblTransferee,lblMainStreet,lblByStreet,lblAlley,lblImpasse,lblPlaqueNo,lblFloor,lblUnitNo,lbljob,lblPOBOX,lblHomeTel,lblWorkTel;
    TextView lblMobile,lblDescr,lblAddressNote,lblstreet3,lblalley3,lblLocation,lblArea;

    ImageButton imgBtnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boxinfolayout);

        db = new dbConnector(this, app.database.DBNAME , null ,1);

        lblBoxCode=(TextView)findViewById(R.id.lblBoxCodeValue);
        lblTransferee=(TextView)findViewById(R.id.lblTransfereeValue);
        lblLocationType=(TextView)findViewById(R.id.lblLocationTypeValue);
        lblMainStreet=(TextView)findViewById(R.id.lblMainStreetValue);
        lblByStreet=(TextView)findViewById(R.id.lblByStreetValue);
        lblAlley=(TextView)findViewById(R.id.lblAlleyValue);
        lblImpasse=(TextView)findViewById(R.id.lblImpasseValue);
        lblPlaqueNo=(TextView)findViewById(R.id.lblPlaqueNoValue);
        lblFloor=(TextView)findViewById(R.id.lblFloorValue);
        lblUnitNo=(TextView)findViewById(R.id.lblUnitNoValue);
        lbljob=(TextView)findViewById(R.id.lbljobValue);
        lblPOBOX=(TextView)findViewById(R.id.lblPOBOXValue);
        lblHomeTel=(TextView)findViewById(R.id.lblHomeTelValue);
        lblWorkTel=(TextView)findViewById(R.id.lblWorkTelValue);
        lblMobile=(TextView)findViewById(R.id.lblMobileValue);
        lblDescr=(TextView)findViewById(R.id.lblDescrValue);
        lblAddressNote=(TextView)findViewById(R.id.lblAddressNoteValue);
        lblstreet3=(TextView)findViewById(R.id.lblstreet3Value);
        lblalley3=(TextView)findViewById(R.id.lblalley3Value);
        lblLocation=(TextView)findViewById(R.id.lblLocationValue);
        lblArea=(TextView)findViewById(R.id.lblAreaValue);

        Intent data=getIntent();

        BoxSearch(data.getStringExtra("BoxCode"));

        imgBtnBack=(ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


    }



    private void BoxSearch(String boxCode)
    {
        String query  = "SELECT * FROM tblBox where fld_Code="+boxCode;

        Cursor c = null;
        c = db.select(query);


        if(c.moveToNext()) {

            lblBoxCode.setText(boxCode);
            lblTransferee   .setText(c.getString(c.getColumnIndex("fld_Transferee"  )));



            lblLocationType .setText(LocationTypeSearch(String.valueOf(c.getInt(c.getColumnIndex("fk_LocationType")))));




            lblMainStreet   .setText(c.getString(c.getColumnIndex("fld_MainStreet"  )));
            lblByStreet     .setText(c.getString(c.getColumnIndex("fld_ByStreet"    )));
            lblAlley        .setText(c.getString(c.getColumnIndex("fld_Alley"       )));
            lblImpasse      .setText(c.getString(c.getColumnIndex("fld_Impasse"     )));
            lblPlaqueNo     .setText(c.getString(c.getColumnIndex("fld_No"          )));
            lblFloor        .setText(c.getString(c.getColumnIndex("fld_Floor"       )));
            lblUnitNo       .setText(c.getString(c.getColumnIndex("fld_UnitNo"      )));
            lbljob          .setText(c.getString(c.getColumnIndex("fld_job"         )));
            lblPOBOX        .setText(c.getString(c.getColumnIndex("fld_POBOX"       )));
            lblHomeTel      .setText(c.getString(c.getColumnIndex("fld_HomeTel"     )));
            lblWorkTel      .setText(c.getString(c.getColumnIndex("fld_WorkTel"     )));
            lblMobile       .setText(c.getString(c.getColumnIndex("fld_Mobile"      )));
            lblDescr        .setText(c.getString(c.getColumnIndex("fld_Descr"       )));
            lblAddressNote  .setText(c.getString(c.getColumnIndex("fld_AddressNote" )));
            lblstreet3      .setText(c.getString(c.getColumnIndex("fld_street3"     )));
            lblalley3       .setText(c.getString(c.getColumnIndex("fld_alley3"      )));

            lblArea         .setText(AreaSearch(String.valueOf(c.getShort(c.getColumnIndex("fk_AreaID")))));
            lblLocation     .setText(String.valueOf(c.getShort (c.getColumnIndex("fld_Location"    ))));

        }
        else {

            lblBoxCode.setText("");
            lblTransferee   .setText("");
            lblLocationType .setText("");
            lblMainStreet   .setText("");
            lblByStreet     .setText("");
            lblAlley        .setText("");
            lblImpasse      .setText("");
            lblPlaqueNo     .setText("");
            lblFloor        .setText("");
            lblUnitNo       .setText("");
            lbljob          .setText("");
            lblPOBOX        .setText("");
            lblHomeTel      .setText("");
            lblWorkTel      .setText("");
            lblMobile       .setText("");
            lblDescr        .setText("");
            lblAddressNote  .setText("");
            lblstreet3      .setText("");
            lblalley3       .setText("");
            lblArea         .setText("");
            lblLocation.setText("");

        }

    }
    private String LocationTypeSearch(String fk_LocationType)
    {
        String query  = "SELECT  PKID, fld_LocationType  FROM tblLocationType Where PKID="+fk_LocationType ;

        Cursor c = null;
        c = db.select(query);

        if (c.moveToNext()) {


            String value = c.getString(c.getColumnIndex("fld_LocationType"));
            return value;
        }
        return "";
    }
    private String AreaSearch(String fk_AreaID)
    {
        String query  = "SELECT  PKID, fld_AreaTitle  FROM tblArea Where PKID="+fk_AreaID ;

        Cursor c = null;
        c = db.select(query);

        if (c.moveToNext()) {


            String value = c.getString(c.getColumnIndex("fld_AreaTitle"));
            return value;
        }
        return "";
    }

}