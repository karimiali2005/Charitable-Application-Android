package Sadraafzar.com.Charitable;

import Classess.DBImportExport;
import Classess.app;
import Classess.dbConnector;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 6/26/2017.
 */
public class SettingActivity extends Activity {
    Button btnLogin,btnExportDB,btnImportDB;
    RadioGroup radioPrinterGroup;
    RadioButton radioBIXOLON,radioRONGTA,radioPrinterButton;
    ImageButton imgBtnBack;
    EditText txtUserPass;
    TextView lblUserPass;
    Boolean flag=false;
    dbConnector db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settinglayout);

        db = new dbConnector(this, app.database.DBNAME , null ,1);

        txtUserPass=(EditText)findViewById(R.id.txtUserPass);
        lblUserPass=(TextView)findViewById(R.id.lblUserPass);
        radioPrinterGroup = (RadioGroup) findViewById(R.id.radioPrinterGroup);
        radioBIXOLON=(RadioButton)findViewById(R.id.radioBIXOLON);
        radioRONGTA=(RadioButton)findViewById(R.id.radioRONGTA);

        imgBtnBack=(ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });



        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==false)
                {
                    if(txtUserPass.getText().toString().equals("ali110"))
                    {
                        txtUserPass.setText(app.Communication.baseUrl);
                        lblUserPass.setText("آدرس");
                        txtUserPass.setText(app.Communication.baseUrl);
                        txtUserPass.setInputType(InputType.TYPE_CLASS_TEXT);
                        btnLogin.setText("ذخیره");
                        btnExportDB.setVisibility(View.VISIBLE);
                        btnImportDB.setVisibility(View.VISIBLE);
                        radioBIXOLON.setVisibility(View.VISIBLE);
                        radioRONGTA.setVisibility(View.VISIBLE);
                        if(app.Printer.IsBIXOLON)
                            radioBIXOLON.setChecked(true);
                        else
                            radioRONGTA.setChecked(true);
                        flag=true;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"رمز اشتباه هست",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    app.Communication.baseUrl = txtUserPass.getText().toString();
                    int selectedId = radioPrinterGroup.getCheckedRadioButtonId();
                    radioPrinterButton = (RadioButton) findViewById(selectedId);
                    if (radioPrinterButton.getTag().toString().equals("1"))
                        app.Printer.IsBIXOLON=true;
                    else
                    app.Printer.IsBIXOLON=false;
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ServiceUrl",txtUserPass.getText().toString()).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Printer",app.Printer.IsBIXOLON.toString()).apply();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnExportDB=(Button)findViewById(R.id.btnExportDB);
        btnExportDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DBImportExport.backupDatabase();

                    Toast.makeText(getApplicationContext(), "انجام شد", Toast.LENGTH_SHORT).show();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

        });


        btnImportDB=(Button)findViewById(R.id.btnImportDB);
        btnImportDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File sdCard = Environment.getExternalStorageDirectory();

                    DBImportExport.importDatabase(sdCard.getAbsolutePath()+"/MYDB.bin");
                    Toast.makeText(getApplicationContext(), "انجام شد", Toast.LENGTH_SHORT).show();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

        });
    }
}