package Sadraafzar.com.Charitable;

import Classess.Methods;
import Classess.app;
import Classess.dbConnector;
import Model.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 6/1/2017.
 */
public class MainActivity extends Activity {
    ImageButton  btnLogout, btnMission,btnBoxDichargeItem,btnBoxReport,btnBoxNew,btnBoxEdit,btnBoxMovement,btnReceiveOffline,btnSendOffline,btnMissionFinish;
    dbConnector db;
    Boolean FlagFinishMission;
    TextView lblLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);


        db = new dbConnector(this, app.database.DBNAME , null ,1);

        //region Mission
        btnMission = (ImageButton) findViewById(R.id.btnMission);
        btnMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MissionActivity.class);
                startActivity(intent);
            }
        });
        //endregion

        //region BoxDichargeItem
        btnBoxDichargeItem = (ImageButton) findViewById(R.id.btnBoxDichargeItem);
        btnBoxDichargeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoxDichargeItemActivity.class);
                startActivity(intent);
            }
        });
        //endregion

        //region btnBoxNew
        btnBoxNew = (ImageButton) findViewById(R.id.btnBoxNew);
        btnBoxNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BoxNewActivity.class);
                startActivity(intent);
            }
        });
        //endregion

        //region btnBoxEdit
        btnBoxEdit = (ImageButton) findViewById(R.id.btnBoxEdit);
        btnBoxEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BoxEditActivity.class);
                startActivity(intent);
            }
        });
        //endregion

        //region btnBoxEdit
        btnBoxMovement = (ImageButton) findViewById(R.id.btnBoxMovement);
        btnBoxMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BoxMovementActivity.class);
                startActivity(intent);
            }
        });
        //endregion

        //region BoxReport
        btnBoxReport = (ImageButton) findViewById(R.id.btnBoxReport);
        btnBoxReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoxReportActivity.class);
                startActivity(intent);
            }
        });
        //endregion

        //region Logout
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(app.file.name, MODE_PRIVATE)
                        .edit()
                        .putBoolean(app.file.login, false)
                        .commit();
                finish();
            }
        });
        lblLogout = (TextView) findViewById(R.id.lblLogout);
        lblLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(app.file.name, MODE_PRIVATE)
                        .edit()
                        .putBoolean(app.file.login, false)
                        .commit();
                finish();
            }
        });
        //endregion



        //region ReceiveOffline
        btnReceiveOffline = (ImageButton) findViewById(R.id.btnReceiveOffline);
        btnReceiveOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveOffline();

            }
        });
        //endregion

        //region SendOffline
        btnSendOffline = (ImageButton) findViewById(R.id.btnSendOffline);
        btnSendOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlagFinishMission=false;
                SendOffline();

            }
        });
        //endregion

        //region MissionFinish
        btnMissionFinish = (ImageButton) findViewById(R.id.btnMissionFinish);
        btnMissionFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new AlertDialog.Builder(MainActivity.this)
                .setTitle(getBaseContext().getString(R.string.FinishMissionTitle))

                .setMessage(getBaseContext().getString(R.string.FinishMissionBody))
                .setPositiveButton(getBaseContext().getString(R.string.YES), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        FlagFinishMission=true;
                        SendOffline();


                    }
                })
               .setNegativeButton(getBaseContext().getString(R.string.No), null)
               .setIcon(android.R.drawable.ic_dialog_info).show();



            }
        });
        //endregion

    }
    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }
    //region ReceiveOffline
    private void ReceiveOffline() {
        if (isNetworkConnected()) {

            Receive receive = new Receive();
            receive.execute(String.valueOf(app.Info.UserID));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getBaseContext().getString(R.string.NoInternetConnectionTitle))//"No Internet Connection")
                            //.setMessage("It looks like your internet connection is off. Please turn it " +
                            //"on and try again")
                    .setMessage(getBaseContext().getString(R.string.NoInternetConnectionBody))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }

    }


    private ProgressDialog mProgressDialog;

    protected Dialog onCreateDialog(int id) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getBaseContext().getString(R.string.wait));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        return mProgressDialog;

    }



    public class Receive extends AsyncTask<String, Integer, ReceiveMissionModel[]> {

        private String Error = "";
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(1);
        }

        @Override
        protected ReceiveMissionModel[] doInBackground(String... params) {
            try {

                Error="";
                String url = app.Communication.baseUrl + "api/ReciveData/" + params[0];

                String results = Methods.httpGet(url);
                if(results.toLowerCase().contains("error"))
                {
                    Error = "ConnectToServer";
                    return null;
                }
                else {

                    Gson gson = new Gson();
                    ReceiveMissionModel[] receiveMissions = gson.fromJson(results, ReceiveMissionModel[].class);

                    url = app.Communication.baseUrl + "api/ReciveData/";

                    results = Methods.httpGet(url);


                    ReceiveFixedDataModel receiveFixedDataModel = gson.fromJson(results, ReceiveFixedDataModel.class);


                    url = app.Communication.baseUrl + "api/Login/"+ params[0];;

                    results = Methods.httpGet(url);


                    MissionMessageViewModel missionMessageViewModel = gson.fromJson(results, MissionMessageViewModel.class);

                    dbConnector db = new dbConnector(getApplicationContext(), app.database.DBNAME, null, 1);
                    Boolean statusBox = db.insertBulkBox(receiveMissions);
                    Boolean statusFixedData = db.insertBulkFixedData(receiveFixedDataModel);
                    Boolean statusMissionMessage = db.updateMissionMessage(missionMessageViewModel);



                    return receiveMissions;
                }
            }catch (NetworkOnMainThreadException ex)
            {
                Error = "ErrorNetwork";
                return null;
            }
            catch (JsonParseException e) {
                Error = "ParseData";
                return null;
            }


        }

        protected void onProgressUpdate(Integer... progress) {
            //Not sure what to do here
        }

        protected void onPostExecute(ReceiveMissionModel[] results) {
            switch (Error)
            {
                case "ConnectToServer":
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorServer) , Toast.LENGTH_LONG).show();
                    break;
                case "ErrorNetwork":
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorNetwork), Toast.LENGTH_LONG).show();
                    break;
                case "ParseData":
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorParseJson), Toast.LENGTH_LONG).show();
                    break;
            }
            if(Error.toString().equals("")) {
                if (results.length > 0) {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.ReceiveDataSuccess), Toast.LENGTH_SHORT).show();

                } else if(results!=null) {
                    Toast.makeText(getApplicationContext(),getBaseContext().getString(R.string.NoMission), Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.ReceiveDataFailed), Toast.LENGTH_SHORT).show();
                }
            }
            dismissDialog(1);

        }
    }
    //endregion

    private void SendOffline() {
        if (isNetworkConnected()) {

            SendMissionModel sendMission = new SendMissionModel(FillListtblBoxModel(), FillListtblBoxReportModel(), FillListtblBoxDischargeItemModel());
            Final final1 = new Final(sendMission);
            final1.execute("");
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getBaseContext().getString(R.string.NoInternetConnectionTitle))//"No Internet Connection")
                            //.setMessage("It looks like your internet connection is off. Please turn it " +
                            //"on and try again")
                    .setMessage(getBaseContext().getString(R.string.NoInternetConnectionBody))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }

    }

    private List<tblBoxModel> FillListtblBoxModel()
    {
        List<tblBoxModel> list=new ArrayList<tblBoxModel>();

        String query  = "SELECT * FROM tblBox";

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {
             if(c.getShort (c.getColumnIndex("fk_RowStatus"))>1) {
                 tblBoxModel item = new tblBoxModel();
                 item.fk_HeaderID = c.getInt(c.getColumnIndex("PKIDtblBoxDichargeHeader"));
                 item.PKID = c.getInt(c.getColumnIndex("PKIDtblBox"));
                 item.fk_Type = c.getShort(c.getColumnIndex("fk_Type"));
                 item.fld_Code = c.getString(c.getColumnIndex("fld_Code"));
                 item.fld_AssignDate = c.getString(c.getColumnIndex("fld_AssignDate"));
                 item.fld_Transferee = c.getString(c.getColumnIndex("fld_Transferee"));
                 item.fk_LocationType = c.getShort(c.getColumnIndex("fk_LocationType"));
                 item.fk_CityID = c.getShort(c.getColumnIndex("fk_CityID"));
                 item.fk_AreaID = c.getShort(c.getColumnIndex("fk_AreaID"));
                 item.fld_Location = c.getShort(c.getColumnIndex("fld_Location"));
                 item.fld_Order = c.getInt(c.getColumnIndex("fld_Order"));
                 item.fld_DischargePeriod = c.getShort(c.getColumnIndex("fld_DischargePeriod"));
                 item.fld_MainStreet = c.getString(c.getColumnIndex("fld_MainStreet"));
                 item.fld_ByStreet = c.getString(c.getColumnIndex("fld_ByStreet"));
                 item.fld_Alley = c.getString(c.getColumnIndex("fld_Alley"));
                 item.fld_Impasse = c.getString(c.getColumnIndex("fld_Impasse"));
                 item.fld_No = c.getString(c.getColumnIndex("fld_No"));
                 item.fld_Floor = c.getString(c.getColumnIndex("fld_Floor"));
                 item.fld_UnitNo = c.getString(c.getColumnIndex("fld_UnitNo"));
                 item.fld_job = c.getString(c.getColumnIndex("fld_job"));
                 item.fld_POBOX = c.getString(c.getColumnIndex("fld_POBOX"));
                 item.fld_HomeTel = c.getString(c.getColumnIndex("fld_HomeTel"));
                 item.fld_WorkTel = c.getString(c.getColumnIndex("fld_WorkTel"));
                 item.fld_Mobile = c.getString(c.getColumnIndex("fld_Mobile"));
                 item.fk_LastStatus = c.getShort(c.getColumnIndex("fk_LastStatus"));
                 item.fk_LevelID = c.getShort(c.getColumnIndex("fk_LevelID"));
                 item.fld_ReleaseDate = c.getString(c.getColumnIndex("fld_ReleaseDate"));
                 item.fk_Agent1ID = c.getInt(c.getColumnIndex("fk_Agent1IDtblBox"));
                 item.fk_Agent2ID = c.getInt(c.getColumnIndex("fk_Agent2IDtblBox"));
                 item.fk_AssignType = c.getShort(c.getColumnIndex("fk_AssignType"));
                 item.fld_Descr = c.getString(c.getColumnIndex("fld_Descr"));
                 item.fld_EmptyPerod = c.getShort(c.getColumnIndex("fld_EmptyPerod"));
                 item.fk_RowStatus = c.getShort(c.getColumnIndex("fk_RowStatus"));
                 item.fld_Adder = c.getString(c.getColumnIndex("fld_Adder"));

                 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                 if (c.getString(c.getColumnIndex("fld_AddDate"))!=null&&!c.getString(c.getColumnIndex("fld_AddDate")).toString().equals(""))
                     try {
                         item.fld_AddDate =simpleDateFormat.parse(c.getString(c.getColumnIndex("fld_AddDate")));
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }


                 item.fld_Editor = c.getString(c.getColumnIndex("fld_Editor"));

                 if (c.getString(c.getColumnIndex("fld_EditDate"))!=null&&!c.getString(c.getColumnIndex("fld_EditDate")).toString().equals(""))
                     try {
                         item.fld_EditDate =simpleDateFormat.parse(c.getString(c.getColumnIndex("fld_EditDate")));
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }

                 item.fld_AddressNote = c.getString(c.getColumnIndex("fld_AddressNote"));
                 item.fld_street3 = c.getString(c.getColumnIndex("fld_street3"));
                 item.fld_alley3 = c.getString(c.getColumnIndex("fld_alley3"));
                 item.fld_latitude = c.getDouble(c.getColumnIndex("fld_latitude"));
                 item.fld_longitude = c.getDouble(c.getColumnIndex("fld_longitude"));

                ;
                 list.add(item);
             }

        }

        return list;
    }
    private List<tblBoxReportModel> FillListtblBoxReportModel()
    {
        List<tblBoxReportModel> list=new ArrayList<tblBoxReportModel>();

        String query  = "SELECT * FROM tblBoxReport";

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {
            tblBoxReportModel item=new tblBoxReportModel();
            item.fk_BoxID    =c.getInt   (c.getColumnIndex("fk_BoxID"     ));
            item.fld_Date    =c.getString(c.getColumnIndex("fld_Date"     ));
            item.fk_Agent1   =c.getInt   (c.getColumnIndex("fk_Agent1"));
            item.fk_Agent2   =c.getInt   (c.getColumnIndex("fk_Agent2"    ));
            item.fk_StatusID =c.getShort (c.getColumnIndex("fk_StatusID"  ));
            item.fk_LevelID  =c.getShort (c.getColumnIndex("fk_LevelID"  ));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            if (c.getString(c.getColumnIndex("fld_AddDate"))!=null&&!c.getString(c.getColumnIndex("fld_AddDate")).toString().equals(""))
                try {
                    item.fld_AddDate =simpleDateFormat.parse(c.getString(c.getColumnIndex("fld_AddDate")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            item.fld_Adder   =c.getString(c.getColumnIndex("fld_Adder"    ));
            item.fk_MissionID=c.getInt(c.getColumnIndex("fk_MissionID"));
            item.fld_Descr   =c.getString(c.getColumnIndex("fld_Descr"         ));
            list.add(item);
        }

        return list;
    }
    private List<tblBoxDischargeItemModel> FillListtblBoxDischargeItemModel()
    {
        List<tblBoxDischargeItemModel> list=new ArrayList<tblBoxDischargeItemModel>();

        String query  = "SELECT * FROM tblBoxDischargeItem";

        Cursor c = null;
        c = db.select(query);

        while(c.moveToNext()) {
            tblBoxDischargeItemModel item=new tblBoxDischargeItemModel();
            item.fk_HeaderID      =c.getInt   (c.getColumnIndex("fk_HeaderID"        ));
            item.fk_AreaID        =c.getShort (c.getColumnIndex("fk_AreaID"          ));
            item.fld_Location     =c.getShort (c.getColumnIndex("fld_Location"));
            item.fld_FishNo       =c.getString(c.getColumnIndex("fld_FishNo"         ));
            item.fk_BoxID         =c.getInt   (c.getColumnIndex("fk_BoxID"));
            item.fld_DischargeTime=c.getString(c.getColumnIndex("fld_DischargeTime"));
            item.fld_PiecePrice = c.getLong(c.getColumnIndex("fld_PiecePrice"));
            item.fld_PaperPrice = c.getLong(c.getColumnIndex("fld_PaperPrice"     ));
            item.fk_SarFasl = c.getShort(c.getColumnIndex("fk_SarFasl"         ));
            item.fld_Address = c.getString(c.getColumnIndex("fld_Address"));
            item.fld_Transferee = c.getString(c.getColumnIndex("fld_Transferee"));
            item.fld_Adder = c.getString(c.getColumnIndex("fld_Adder"          ));
            item.fld_description=c.getString(c.getColumnIndex("fld_description"          ));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            if (c.getString(c.getColumnIndex("fld_AddDate"))!=null&&!c.getString(c.getColumnIndex("fld_AddDate")).toString().equals(""))
                try {
                    item.fld_AddDate =simpleDateFormat.parse(c.getString(c.getColumnIndex("fld_AddDate")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            item.fld_latitude     =c.getDouble(c.getColumnIndex("fld_latitude"));
            item.fld_longitude    =c.getDouble(c.getColumnIndex("fld_longitude"));


            list.add(item);
        }

        return list;
    }
    public class Final extends AsyncTask<String, Integer, String> {

        private String Error = "";
        private String Error2="";

        public Final(SendMissionModel sendMission)
        {
            sendMissions = sendMission;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(1);
        }

        SendMissionModel sendMissions;

        @Override
        protected String doInBackground(String... params) {
            try {
                Gson gson = new Gson();
                String data = gson.toJson(sendMissions);
                String url = app.Communication.baseUrl + "api/SendData/";
               // Methods.disableConnectionReuseIfNecessary();
                Methods.ServerResponse serverResponse = Methods.httpPUTServerError(url, data);
                String r=serverResponse.ResponseCode;
                if(r.toLowerCase().contains("error"))
                {
                    Error = "ConnectToServer";
                    Error2=r;
                    return null;
                }
                else
                if(r.contains("502"))
                {
                    Error="BadGateway";
                    Error2=serverResponse.Content;
                    return null;
                }
                else
                {
                    return "Done";
                }


            }
            catch (NetworkOnMainThreadException ex)
            {
                Error = "ErrorNetwork";
                return null;
            }
            catch (JsonParseException e) {
                Error = "ParseData";
                return null;
            }
        }


        protected void onProgressUpdate(Integer... progress) {
            //Not sure what to do here
        }

        protected void onPostExecute(String results) {

            if(results!=null && results.contains("Done"))
            {
                if(FlagFinishMission==false)
                Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.SendDataSuccess),Toast.LENGTH_SHORT).show();
                else
                {
                    db.DropDatabase();
                    SharedPreferences prefs = getSharedPreferences(app.file.name, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.SuccessFinishMission), Toast.LENGTH_LONG).show();
                    finish();
                    System.exit(0);
                }
            }
            else
            {
                switch (Error)
                {
                    case "ConnectToServer":
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorServer)+"\n"+Error2 , Toast.LENGTH_LONG).show();
                        break;
                    case "ErrorNetwork":
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorNetwork), Toast.LENGTH_LONG).show();
                        break;
                    case "ParseData":
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorParseJson), Toast.LENGTH_LONG).show();
                        break;
                    case "BadGateway":
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.ErrorBadGateway)+"\n"+Error2, Toast.LENGTH_LONG).show();
                        break;
                }
            }

            dismissDialog(1);
        }
    }

}