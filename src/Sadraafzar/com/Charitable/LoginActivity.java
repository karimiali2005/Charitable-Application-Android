package Sadraafzar.com.Charitable;

import Classess.Methods;
import Classess.app;
import Classess.dbConnector;
import Model.LoginViewModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

/**
 * Created by Administrator on 5/31/2017.
 */
public class LoginActivity extends Activity {
    Button btnLogin;
    ImageButton btnSetting;
    TextView txtUserName,txtUserPass;
    CheckBox chkRemember ;
    dbConnector db;
    String SerialNumber="";

    @Override
    protected void onResume() {
        Boolean status = getSharedPreferences(app.file.name, 0).getBoolean(app.file.login, false);
        String DateLogin = getSharedPreferences(app.file.name, 0).getString(app.file.DateLogin,"");

        if(status&& DateLogin.equals(app.nowDate()))
        {
            String  query  = "SELECT * FROM Login ";
            Cursor c = null;
            c = db.select(query);
            if(c.moveToNext()) {


                app.Info.UserName = c.getString(c.getColumnIndex("fld_UserName"));
                app.Info.FirstName = c.getString(c.getColumnIndex("FirstName"));
                app.Info.LastName = c.getString(c.getColumnIndex("LastName"));
                app.Info.UserID = c.getInt(c.getColumnIndex("UserID"));
            }
            startActivity(new Intent(this , MainActivity.class));
            finish();
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);

        db = new dbConnector(this, app.database.DBNAME , null ,1);

        String url = PreferenceManager.getDefaultSharedPreferences(this).getString("ServiceUrl", getBaseContext().getString(R.string.textUrl));
        app.Communication.baseUrl = url;

        String isBIXOLON=PreferenceManager.getDefaultSharedPreferences(this).getString("Printer", "true");
        if (isBIXOLON.equals("true"))
        {
            app.Printer.IsBIXOLON=true;
        }
        else
        app.Printer.IsBIXOLON=false;

        txtUserName=(TextView)findViewById(R.id.txtUsername);
        txtUserPass=(TextView)findViewById(R.id.txtUserPass);
        chkRemember=(CheckBox)findViewById(R.id.chkRemember);

        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ChekLoginLocal())
                   Login();
            }
        });

        btnSetting=(ImageButton)findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });



        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);

            SerialNumber = (String) get.invoke(c, "sys.serialnumber", "error");
            if (SerialNumber.equals("error")) {
                SerialNumber = (String) get.invoke(c, "ril.serialnumber", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // Toast.makeText(getApplicationContext(),serialNumber, Toast.LENGTH_LONG).show();

    }

    private boolean ChekLoginLocal()
    {
        String query  = "SELECT * FROM Login ";

        Cursor c = null;
        c = db.select(query);

        Boolean emptyTable=!c.moveToNext();

        if(emptyTable==false)
        {
            //query  = "SELECT * FROM Login Where fld_UserName='"+txtUserName.getText().toString()+"' and UserPass='"+txtUserPass.getText().toString()+"'";
            query  = "SELECT * FROM Login Where fld_UserName= ? and UserPass= ?";
            c = db.select(query,txtUserName.getText().toString(),txtUserPass.getText().toString());
            if(c.moveToNext())
            {
                Boolean rememberLogin = chkRemember.isChecked();
                if(rememberLogin) {
                    getSharedPreferences(app.file.name, MODE_PRIVATE)
                            .edit()
                            .putBoolean(app.file.login, true)
                            .putString(app.file.DateLogin,app.nowDate())
                            .commit();
                }

                app.Info.UserName=c.getString(c.getColumnIndex("fld_UserName"));
                app.Info.FirstName=c.getString(c.getColumnIndex("FirstName"));
                app.Info.LastName=c.getString(c.getColumnIndex("LastName"));
                app.Info.UserID=c.getInt(c.getColumnIndex("UserID"));


                ClearAllTextBox();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.noLogin), Toast.LENGTH_SHORT).show();
            }
        }

        return emptyTable;

    }
    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3*/
    }
    private boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) && networkInfo.isConnected();
    }
    private void Login() {
        if (isNetworkConnected()) {
            if (!txtUserName.getText().toString().equals("") && txtUserPass.getText().length() <= 9) {
                Search search = new Search();
                search.execute(txtUserName.getText().toString(), txtUserPass.getText().toString());
            } else {
                ClearAllTextBox();

            }
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

    private void ClearAllTextBox() {
        txtUserPass.setText("");
        txtUserName.setText("");
        txtUserName.setFocusable(true);
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
    public class Search extends AsyncTask<String, Integer, LoginViewModel> {

        private String Error = "";
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(1);
        }

        @Override
        protected LoginViewModel doInBackground(String... params) {
            try {
                Error="";
                String url = app.Communication.baseUrl + "api/Login?username=" + params[0] + "&pass=" + params[1]+ "&serialNumber=" + SerialNumber;

                String results = Methods.httpGet(url);
                if(results.toLowerCase().contains("error"))
                {
                    Error = "ConnectToServer";
                    return null;
                }
                else {

                    Gson gson = new Gson();
                    LoginViewModel login = gson.fromJson(results, LoginViewModel.class);

                    return login;
                }


            } catch (NetworkOnMainThreadException ex)
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

        protected void onPostExecute(LoginViewModel results) {
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
                if (results != null) {

                    String versionName = "";
                    int versionCode = -1;
                    try {
                        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        versionName = packageInfo.versionName;
                        versionCode = packageInfo.versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    if(results.fld_Version!=0&& results.fld_Version!=versionCode)
                    {
                        Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.UpdateApp), Toast.LENGTH_LONG).show();
                        dismissDialog(1);
                        return;
                    }

                    Boolean rememberLogin = chkRemember.isChecked();
                    if (rememberLogin) {
                        getSharedPreferences(app.file.name, MODE_PRIVATE)
                                .edit()
                                .putBoolean(app.file.login, true)
                                .putString(app.file.DateLogin, app.nowDate())
                                .commit();
                    }

                    ContentValues values = new ContentValues();

                    values.put("fld_UserName", results.fld_UserName);
                    values.put("FirstName", results.FirstName);
                    values.put("LastName", results.LastName);
                    values.put("UserPass", txtUserPass.getText().toString());
                    values.put("UserID", results.UserID);
                    Boolean status = db.insert("Login", values);

                    app.Info.UserName = results.fld_UserName;
                    app.Info.FirstName = results.FirstName;
                    app.Info.LastName = results.LastName;
                    app.Info.UserID = results.UserID;


                    ClearAllTextBox();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.noLogin), Toast.LENGTH_SHORT).show();

                }
            }
           dismissDialog(1);
           ClearAllTextBox();
        }
    }




}

