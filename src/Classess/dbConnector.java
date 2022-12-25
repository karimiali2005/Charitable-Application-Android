package Classess;

import Model.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class dbConnector  extends SQLiteOpenHelper{

	Context context;
	public dbConnector(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

		this.context=context;
		create_Tables() ;
	}

	public  void backupDatabase(String databaseName) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			String packageName = context.getApplicationInfo().packageName;

			if (sd.canWrite()) {
				String currentDBPath = String.format("//data//%s//databases//%s",
						packageName, databaseName);
				String backupDBPath = String.format("debug_%s.sqlite", packageName);
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
			throw new Error(e);
		}
	}


	@Override
	public void onCreate(SQLiteDatabase arg0) {
	 
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	private void create_Tables() {

		//create Table tblBox
		String query = 
				"CREATE TABLE IF NOT EXISTS tblBox (" +
				"id INTEGER PRIMARY KEY,"+
				"PKIDtblBoxDichargeHeader       INTEGER   , " +
				"fld_DischargeNo    TEXT                            , " +
				"fld_DischargeDate    TEXT                            , " +
				"fk_Agent1ID     INTEGER                               ," +
				"fk_Agent2ID     INTEGER                              , " +
				"PKIDtblDischargeArea     INTEGER                            ,   " +
				"fk_MissionID     INTEGER                            ,   " +
				"fk_AreaID     INTEGER                            ,   " +
				"fld_Location     INTEGER                            ,   " +
				"PKIDtblBox     INTEGER                            ,   " +
				"fk_Type     INTEGER                            ,   " +
				"fld_Code    TEXT                            , " +
				"fld_AssignDate    TEXT                            , " +
				"fld_Transferee    TEXT                            , " +
				"fk_LocationType    INTEGER                            , " +
				"fk_CityID    INTEGER                            , " +
				"fld_Order    INTEGER                            , " +
				"fld_DischargePeriod    INTEGER                            , " +
				"fld_MainStreet    TEXT                            , " +
				"fld_ByStreet    TEXT                            , " +
				"fld_Alley    TEXT                            , " +
				"fld_Impasse    TEXT                            , " +
				"fld_No    TEXT                            , " +
				"fld_Floor    TEXT                            , " +
				"fld_UnitNo    TEXT                            , " +
				"fld_job    TEXT                            , " +
				"fld_POBOX    TEXT                            , " +
				"fld_HomeTel    TEXT                            , " +
				"fld_WorkTel    TEXT                            , " +
				"fld_Mobile    TEXT                            , " +
				"fk_LastStatus    INTEGER                            , " +
				"fk_LevelID    INTEGER                            , " +
				"fld_ReleaseDate    TEXT                            , " +
				"fk_Agent1IDtblBox    INTEGER                            , " +
				"fk_Agent2IDtblBox    INTEGER                            , " +
				"fk_AssignType    INTEGER                            , " +
				"fld_Descr    TEXT                            , " +
				"fld_EmptyPerod    INTEGER                            , " +
				"fk_RowStatus    INTEGER                            , " +
				"fld_Adder    TEXT                            , " +
				"fld_AddDate    TEXT                            , " +
				"fld_Editor    TEXT                            , " +
				"fld_EditDate    TEXT                            , " +
				"fld_AddressNote    TEXT                            , " +
				"fld_street3    TEXT                            , " +
				"fld_alley3    TEXT                            ," +
				"fld_latitude      DOUBLE , " +
				"fld_longitude     DOUBLE  " +
				"); ";
		
		
		this.exec(query);
		//create Table tblArea
		query =
				"CREATE TABLE IF NOT EXISTS tblArea (" +
						"PKID            INTEGER , " +
						"fld_AreaCode    INTEGER , " +
						"fld_AreaTitle   TEXT      " +
						"); ";
		this.exec(query);

		//create Table tblBoxLevel
		query =
				"CREATE TABLE IF NOT EXISTS tblBoxLevel (" +
						"PKID           INTEGER , " +
						"fld_BoxLevel   TEXT      " +
						"); ";
		this.exec(query);

		//create Table tblBoxStatus
		query =
				"CREATE TABLE IF NOT EXISTS tblBoxStatus (" +
						"PKID            INTEGER , " +
						"fld_BoxStatus   TEXT      " +
						"); ";
		this.exec(query);

		//create Table tblLocationType
		query =
				"CREATE TABLE IF NOT EXISTS tblLocationType (" +
						"PKID               INTEGER , " +
						"fld_LocationType   TEXT      " +
						"); ";
		this.exec(query);

		//create Table tblSarFasl
		query =
				"CREATE TABLE IF NOT EXISTS tblSarFasl (" +
						"PKID         INTEGER , " +
						"fld_SarFasl  TEXT      " +
						"); ";
		this.exec(query);

		//create Table tblBoxDischargeItem
		query =
				"CREATE TABLE IF NOT EXISTS  tblBoxDischargeItem (" +
						"fk_HeaderID       INTEGER , " +
						"fk_AreaID     	   INTEGER , " +
						"fld_Location      INTEGER , " +
						"fld_FishNo        TEXT    ,  " +
						"fk_BoxID          INTEGER , " +
						"fld_DischargeTime TEXT     , " +
						"fld_PiecePrice    INTEGER , " +
						"fld_PaperPrice    INTEGER ,  " +
						"fk_SarFasl        INTEGER , " +
						"fld_Address       TEXT , " +
						"fld_Transferee    TEXT , " +
						"fld_Adder		   TEXT , " +
						"fld_AddDate       TEXT , " +
						"fld_latitude      DOUBLE , " +
						"fld_longitude     DOUBLE , " +
						"fld_description     TEXT  " +
						"); ";
		this.exec(query);


		//create Table tblBoxReport
		query =
				"CREATE TABLE IF NOT EXISTS  tblBoxReport (" +
						"fk_BoxID       INTEGER , " +
						"fld_Date        TEXT    ,  " +
						"fk_Agent1          INTEGER , " +
						"fk_Agent2         INTEGER ,"+
						"fk_StatusID INTEGER    , " +
						"fk_LevelID INTEGER    , " +
						"fld_AddDate TEXT    , " +
						"fld_Adder TEXT    , " +
						"fk_MissionID INTEGER   ,  " +
						"fld_Descr TEXT     " +
						"); ";
		this.exec(query);

		//create Table Login
		query =
				"CREATE TABLE IF NOT EXISTS  Login (" +
						"fld_UserName       TEXT , " +
						"FirstName        TEXT    ,  " +
						"LastName          TEXT , " +
						"UserPass         TEXT ,"+
						"UserID INTEGER    , " +
						"Agent1ID INTEGER    , " +
						"fld_Name1 TEXT    , " +
						"fld_Family1 TEXT    , " +
						"fld_Code1 INTEGER    , " +
						"Agent2ID INTEGER     ," +
						"fld_Name2 TEXT     ," +
						"fld_Family2 TEXT     ," +
						"fld_Code2 INTEGER     ," +
						"fld_Message TEXT     " +
						"); ";
		this.exec(query);

		//Delete Table
		//query ="Delete From tblBoxDischargeItem";
		//this.exec(query);


	}

	private void delete_Tables(String table){
		//Delete Table
		String query ="delete from "+table;
		this.exec(query);
	}


	public void exec(String query) {

		try {
			this.getWritableDatabase().execSQL(query);
		} catch (Exception e) {

		}

	}


	public Boolean insert(String table , ContentValues values) {


		try {
			this.getWritableDatabase().insert(table, null , values);
			return true;
		} catch (Exception e) {
			 return false;
		}
		 
	}
	public Boolean update(String table , ContentValues values,String strFilter) {


		try {
			this.getWritableDatabase().update(table,values,strFilter,null);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean insertBulkBox(ReceiveMissionModel[] list) {


		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {

			for (ReceiveMissionModel box : list) {

				Cursor c = null;
				String query = "SELECT * FROM tblBox WHERE PKIDtblBox = " + box.PKIDtblBox ;

				c = select(query);

				if(!c.moveToNext()) {
                    ContentValues values = new ContentValues();
					values.put("PKIDtblBoxDichargeHeader", box.PKIDtblBoxDichargeHeader);
					values.put("fld_DischargeNo", box.fld_DischargeNo);
					values.put("fld_DischargeDate", box.fld_DischargeDate);

					values.put("fk_Agent1ID", box.fk_Agent1ID);
					values.put("fk_Agent2ID", box.fk_Agent2ID);
					values.put("PKIDtblDischargeArea", box.PKIDtblDischargeArea);
					values.put("fk_MissionID", box.fk_MissionID);
					values.put("fk_AreaID", box.fk_AreaID);
					values.put("fld_Location", box.fld_Location);
					values.put("PKIDtblBox", box.PKIDtblBox);
					values.put("fk_Type", box.fk_Type);
					values.put("fld_Code", box.fld_Code);
					values.put("fld_AssignDate", box.fld_AssignDate);
					values.put("fld_Transferee", box.fld_Transferee);
					values.put("fk_LocationType", box.fk_LocationType);
					values.put("fk_CityID", box.fk_CityID);
					values.put("fld_Order", box.fld_Order);
					values.put("fld_DischargePeriod", box.fld_DischargePeriod);
					values.put("fld_MainStreet", box.fld_MainStreet);
					values.put("fld_ByStreet", box.fld_ByStreet);
					values.put("fld_Alley", box.fld_Alley);
					values.put("fld_Impasse", box.fld_Impasse);
					values.put("fld_No", box.fld_No);
					values.put("fld_Floor", box.fld_Floor);
					values.put("fld_UnitNo", box.fld_UnitNo);
					values.put("fld_job", box.fld_job);
					values.put("fld_POBOX", box.fld_POBOX);
					values.put("fld_HomeTel", box.fld_HomeTel);
					values.put("fld_WorkTel", box.fld_WorkTel);
					values.put("fld_Mobile", box.fld_Mobile);
					values.put("fk_LastStatus", box.fk_LastStatus);
					values.put("fk_LevelID", box.fk_LevelID);
					values.put("fld_ReleaseDate", box.fld_ReleaseDate);
					values.put("fk_Agent1IDtblBox", box.fk_Agent1IDtblBox);
					values.put("fk_Agent2IDtblBox", box.fk_Agent2IDtblBox);
					values.put("fk_AssignType", box.fk_AssignType);
					values.put("fld_Descr", box.fld_Descr);
					values.put("fld_EmptyPerod", box.fld_EmptyPerod);
					values.put("fk_RowStatus", box.fk_RowStatus);
					values.put("fld_Adder", box.fld_Adder);
					values.put("fld_Editor", box.fld_Editor);
					values.put("fld_AddressNote", box.fld_AddressNote);
					values.put("fld_street3", box.fld_street3);
					values.put("fld_alley3", box.fld_alley3);
					values.put("fld_latitude", box.fld_latitude);
					values.put("fld_longitude", box.fld_longitude);

					db.insert("tblBox", null, values);
				}
				else
                {
                    if(box.fld_DischargeDate.compareTo(c.getString(c.getColumnIndex("fld_DischargeDate")))>0)
                    {
                        int id= c.getInt(c.getColumnIndex("PKIDtblBoxDichargeHeader"));
                        int tblBoxId=c.getInt(c.getColumnIndex("PKIDtblBox"));

                        ContentValues values = new ContentValues();
                        values.put("PKIDtblBoxDichargeHeader", box.PKIDtblBoxDichargeHeader);
                        values.put("fld_DischargeNo", box.fld_DischargeNo);
                        values.put("fld_DischargeDate", box.fld_DischargeDate);

                        values.put("fk_Agent1ID", box.fk_Agent1ID);
                        values.put("fk_Agent2ID", box.fk_Agent2ID);
                        values.put("PKIDtblDischargeArea", box.PKIDtblDischargeArea);
                        values.put("fk_MissionID", box.fk_MissionID);
                        values.put("fk_AreaID", box.fk_AreaID);
                        values.put("fld_Location", box.fld_Location);
                        values.put("PKIDtblBox", box.PKIDtblBox);
                        values.put("fk_Type", box.fk_Type);
                        values.put("fld_Code", box.fld_Code);
                        values.put("fld_AssignDate", box.fld_AssignDate);
                        values.put("fld_Transferee", box.fld_Transferee);
                        values.put("fk_LocationType", box.fk_LocationType);
                        values.put("fk_CityID", box.fk_CityID);
                        values.put("fld_Order", box.fld_Order);
                        values.put("fld_DischargePeriod", box.fld_DischargePeriod);
                        values.put("fld_MainStreet", box.fld_MainStreet);
                        values.put("fld_ByStreet", box.fld_ByStreet);
                        values.put("fld_Alley", box.fld_Alley);
                        values.put("fld_Impasse", box.fld_Impasse);
                        values.put("fld_No", box.fld_No);
                        values.put("fld_Floor", box.fld_Floor);
                        values.put("fld_UnitNo", box.fld_UnitNo);
                        values.put("fld_job", box.fld_job);
                        values.put("fld_POBOX", box.fld_POBOX);
                        values.put("fld_HomeTel", box.fld_HomeTel);
                        values.put("fld_WorkTel", box.fld_WorkTel);
                        values.put("fld_Mobile", box.fld_Mobile);
                        values.put("fk_LastStatus", box.fk_LastStatus);
                        values.put("fk_LevelID", box.fk_LevelID);
                        values.put("fld_ReleaseDate", box.fld_ReleaseDate);
                        values.put("fk_Agent1IDtblBox", box.fk_Agent1IDtblBox);
                        values.put("fk_Agent2IDtblBox", box.fk_Agent2IDtblBox);
                        values.put("fk_AssignType", box.fk_AssignType);
                        values.put("fld_Descr", box.fld_Descr);
                        values.put("fld_EmptyPerod", box.fld_EmptyPerod);
                        values.put("fk_RowStatus", box.fk_RowStatus);
                        values.put("fld_Adder", box.fld_Adder);
                        values.put("fld_Editor", box.fld_Editor);
                        values.put("fld_AddressNote", box.fld_AddressNote);
                        values.put("fld_street3", box.fld_street3);
                        values.put("fld_alley3", box.fld_alley3);
                        values.put("fld_latitude", box.fld_latitude);
                        values.put("fld_longitude", box.fld_longitude);

                        String strFilter="PKIDtblBoxDichargeHeader="+id+" and PKIDtblBox="+tblBoxId;

                        db.update("tblBox", values,strFilter,null);

                        ContentValues values2 = new ContentValues();
                        values2.put("fk_HeaderID", box.PKIDtblBoxDichargeHeader);

                        String strFilter2="fk_HeaderID="+id+" and fk_BoxID="+tblBoxId;

                        db.update("tblBoxDischargeItem", values2,strFilter2,null);

                    }
                }
			}
			db.setTransactionSuccessful();
			return true;
		} finally {
			db.endTransaction();
			return false;
		}

	}


	public Boolean insertBulkFixedData(ReceiveFixedDataModel list) {


		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			//Insert Into All tblArea
			List<tblAreaModel> tblAreaModelList=list.TblAreaModels;
			ContentValues valuesArea = new ContentValues();
			delete_Tables("tblArea");
			for (tblAreaModel tblArea : tblAreaModelList) {
				valuesArea.put("PKID", tblArea.PKID);
				valuesArea.put("fld_AreaCode", tblArea.fld_AreaCode);
				valuesArea.put("fld_AreaTitle", tblArea.fld_AreaTitle);
				db.insert("tblArea", null, valuesArea);
			}

			//Insert Into All tblBoxLevel
			List<tblBoxLevelModel> tblBoxLevelModelList=list.TblBoxLevelModels;
			ContentValues valuesBoxLevel = new ContentValues();
			delete_Tables("tblBoxLevel");
			for (tblBoxLevelModel tblBoxLevel : tblBoxLevelModelList) {
				valuesBoxLevel.put("PKID", tblBoxLevel.PKID);
				valuesBoxLevel.put("fld_BoxLevel", tblBoxLevel.fld_BoxLevel);
				db.insert("tblBoxLevel", null, valuesBoxLevel);
			}

			//Insert Into All tblBoxLevel
			List<tblBoxStatusModel> tblBoxStatusModelList=list.TblBoxStatusModels;
			ContentValues valuesBoxStatus = new ContentValues();
			delete_Tables("tblBoxStatus");
			for (tblBoxStatusModel tblBoxStatus : tblBoxStatusModelList) {
				valuesBoxStatus.put("PKID", tblBoxStatus.PKID);
				valuesBoxStatus.put("fld_BoxStatus", tblBoxStatus.fld_BoxStatus);
				db.insert("tblBoxStatus", null, valuesBoxStatus);
			}

			//Insert Into All tblLocationType
			List<tblLocationTypeModel> tblLocationTypeModelList=list.TblLocationTypeModels;
			ContentValues valuesLocationType = new ContentValues();
			delete_Tables("tblLocationType");
			for (tblLocationTypeModel tblLocationType : tblLocationTypeModelList) {
				valuesLocationType.put("PKID", tblLocationType.PKID);
				valuesLocationType.put("fld_LocationType", tblLocationType.fld_LocationType);
				db.insert("tblLocationType", null, valuesLocationType);
			}

			//Insert Into All tblSarFasl
			List<tblSarFaslModel> tblSarFaslModelList=list.TblSarFaslModels;
			ContentValues valuesSarFasl = new ContentValues();
			delete_Tables("tblSarFasl");
			for (tblSarFaslModel tblSarFasl : tblSarFaslModelList) {
				valuesSarFasl.put("PKID", tblSarFasl.PKID);
				valuesSarFasl.put("fld_SarFasl", tblSarFasl.fld_SarFasl);
				db.insert("tblSarFasl", null, valuesSarFasl);
			}

			db.setTransactionSuccessful();
			return true;
		} finally {
			db.endTransaction();
			return false;
		}

	}

	public Boolean updateMissionMessage(MissionMessageViewModel list) {



		try {

			ContentValues values = new ContentValues();

			values.put("Agent1ID", list.fk_Agent1ID);
			values.put("fld_Name1", list.fld_Name1);
			values.put("fld_Family1", list.fld_Family1);
			values.put("fld_Code1", list.fld_Code1);
			values.put("Agent2ID", list.fk_Agent2ID);
			values.put("fld_Name2", list.fld_Name2);
			values.put("fld_Family2", list.fld_Family2);
			values.put("fld_Code2", list.fld_Code2);
			values.put("fld_Message", list.fld_Message);
			String strFilter=" UserID="+app.Info.UserID;
			this.getWritableDatabase().update("Login",values,strFilter,null);




			return true;
		} finally {

			return false;
		}

	}

	public Cursor select (String query) {
		
		Cursor c = null;
		c =  this.getWritableDatabase().rawQuery(query, null);
		
		
		return c;
	}
	public Cursor select (String query,String param1, String param2) {

		Cursor c = null;
		c =  this.getWritableDatabase().rawQuery(query, new String[]{param1, param2});


		return c;
	}
	
	public boolean DropDatabase()
	{
		try {
			this.context.deleteDatabase(app.database.DBNAME);
			return true;
		}catch (Exception ex)
		{
			return false;
		}

	}
	

}
