package Model;

import java.util.List;

/**
 * Created by Administrator on 6/23/2017.
 */
public class SendMissionModel {
    public SendMissionModel(List<tblBoxModel> tblBoXModels,List<tblBoxReportModel> tblBoxReportModels,List<tblBoxDischargeItemModel> tblBoxDischargeItemModels)
    {
        TblBoXModels=tblBoXModels;
        TblBoxReportModels=tblBoxReportModels;
        TblBoxDischargeItemModels=tblBoxDischargeItemModels;

    }
    public List<tblBoxModel>                TblBoXModels                ;
    public List<tblBoxReportModel>          TblBoxReportModels          ;
    public List<tblBoxDischargeItemModel>   TblBoxDischargeItemModels   ;
}
