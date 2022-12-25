package Model;

import java.util.Date;

/**
 * Created by Administrator on 6/24/2017.
 */
public class tblBoxReportModel {
    public int      fk_BoxID    ;
    public String   fld_Date    ;
    public int      fk_Agent1   ;
    public int      fk_Agent2   ;
    public short    fk_StatusID ;
    public short    fk_LevelID  ;
    public Date     fld_AddDate ;
    public String   fld_Adder   ;
    public int      fk_MissionID;
    public String   fld_Descr   ;
}
