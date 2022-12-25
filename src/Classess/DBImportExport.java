package Classess;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 9/25/2017.
 */
public final class DBImportExport {
    private DBImportExport() throws Exception {
        throw new Exception();
    }
    public static void backupDatabase() throws IOException {
        //Open your local db as the input stream

        String inFileName = "/data/data/Sadraafzar.com.Charitable/databases/"+app.database.DBNAME;
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()+"/MYDB";
        //Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        //Close the streams
        output.flush();
        output.close();
        fis.close();
    }


    public static String DB_FILEPATH = "/data/data/Sadraafzar.com.Charitable/databases/"+app.database.DBNAME;

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public static   boolean importDatabase(String dbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.


        //close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
         //   getWritableDatabase().close();
            return true;
        }
        return false;
    }

    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }



}
