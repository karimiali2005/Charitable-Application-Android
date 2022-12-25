package Model;

import java.util.Date;

/**
 * Created by Administrator on 6/9/2017.
 */
public class tblBoxDischargeItemModel {
    public int    fk_HeaderID       ;
    public short  fk_AreaID         ;
    public short  fld_Location      ;
    public String fld_FishNo        ;
    public int    fk_BoxID          ;
    public String fld_DischargeTime ;
    public long   fld_PiecePrice    ;
    public long   fld_PaperPrice    ;
    public short   fk_SarFasl        ;
    public String fld_Address       ;
    public String fld_Transferee    ;
    public String fld_Adder         ;
    public Date   fld_AddDate       ;
    public double fld_latitude      ;
    public double fld_longitude     ;
    public String fld_description   ;
}
