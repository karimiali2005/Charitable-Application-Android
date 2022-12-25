package Classess;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.graphics.*;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.EventLogTags;
import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.ghasemkiani.util.icu.PersianCalendar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 5/31/2017.
 */
public class app {
    public static class Info
    {
        public static String UserName;
        public static String FirstName;
        public static String LastName;
        public static int UserID ;
    }
    public static class  Communication
    {
        static public String baseUrl;
    }
    public static class Printer
    {
        public static Boolean IsBIXOLON;


    }
    public static class file
    {
        public static final String name = "fileShare";
        public static final String login = "login";
        public static final String DateLogin="DateLogin";
    }

    public static class database
    {

        public static final String DBNAME = "CharitableDB";
    }

    public static class print
    {
        public static String Transferee;
        public static String BoxCode;
        public static String SarFasl;
        public static String FishNo;
        public static String PiecePrice;
        public static String PaperPrice;
        public static String SumPrice;
        public static String Description;
        public static String Agent1Name;
        public static String Agent1Code;
        public static String Agent2Name;
        public static String Agent2Code;
        public static String Message;


    }

    public static String now(String DATE_FORMAT_NOW) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    public static String nowDate()
    {
        PersianCalendar persianCalendar1 = new PersianCalendar();
        int year=persianCalendar1.get(Calendar.YEAR);
        int month=(persianCalendar1.get(Calendar.MONTH) + 1);
        int day=persianCalendar1.get(Calendar.DAY_OF_MONTH);
       // DecimalFormat df = new DecimalFormat("00");
        String result=year+ "/" ;
        if(month>=10)
            result+=month+ "/";
        else
            result+="0"+month+ "/";

        if(day>=10)
            result+=day;
        else
            result+="0"+day;

       // return  year+ "/" +df.format(month) + "/" +df.format(day);
        return result;

    }
    public static Bitmap drawText(String text, int textWidth, int textSize,Layout.Alignment alignment)
    {
       return drawText(text,textWidth,textSize,alignment,false);
    }
    public static Bitmap drawText(String text, int textWidth, int textSize,Layout.Alignment alignment,Boolean Bold) {
        // Get text dimensions
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        if(Bold)
        {
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        }
        textPaint.setTextSize(textSize);
        StaticLayout mTextLayout = new StaticLayout(text, textPaint,
                textWidth, alignment, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap b = Bitmap.createBitmap(textWidth, mTextLayout.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);

        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.WHITE);


        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();

        return b;
    }


    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }


}
