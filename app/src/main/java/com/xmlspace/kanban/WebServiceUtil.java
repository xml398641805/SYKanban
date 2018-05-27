package com.xmlspace.kanban;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class WebServiceUtil extends Thread {

    // 定义webservice的命名空间
    private static final String SERVICE_NAMESPACE = "http://tempuri.org/";
    // 定义webservice提供服务的url
    private static final String SERVICE_URL = "http://10.194.19.114/kanban/KanbanWebservice.asmx";
    //设置消息，通知主线程进行相关操作
    private Handler handler;
    //设置工作类型
    public enum WorkType{
         HelloWord,GetEnabledShowFGNotice,GetEnabledShowFGOPDescription,GetEnabledShowMANotice,GetEnabledShowMAOPDescription,
        GetFGNotice,GetFGOPDescription,GetFinishedGoodsAlarmCount,GetFinishedGoodsItems,GetFinishedGoodsPagesInfo,
        GetFinishedGoodsProjectList,GetMANotice,GetMAOPDescription,GetMaterialCount,GetMaterialItems,GetMaterialPagesInfo,
        GetProductPopupItems,GetSemiFinishedAlarmCount,GetSemiFinishedItems,GetSemiFinishedPagesInfo
    }
    //当前工作类型
    public static WorkType CurrentWorkType;

    private static String[] MethodNames={
            "HelloWorld","GetEnabledShowFGNotice","GetEnabledShowFGOPDescription","GetEnabledShowMANotice","GetEnabledShowMAOPDescription",
            "GetFGNotice","GetFGOPDescription","GetFinishedGoodsAlarmCount","GetFinishedGoodsItems","GetFinishedGoodsPagesInfo",
            "GetFinishedGoodsProjectList","GetMANotice","GetMAOPDescription","GetMaterialCount","GetMaterialItems","GetMaterialPagesInfo",
            "GetProductPopupItems","GetSemiFinishedAlarmCount","GetSemiFinishedItems","GetSemiFinishedPagesInfo"
    };

    public static int[] MessageHandler={
            0x100,0x101,0x102,0x103,0x104,0x105,0x106,0x107,0x108,0x109,0x110,0x111,0x112,0x113,0x114,0x115,
            0x116,0x117,0x118,0x119
    };

    //事件句柄静态类
    public static class messaageHandler{
        public static final int HelloWorld=0x100;
        public static final int GetEnabledShowFGNotice=0x101;
        public static final int GetEnabledShowFGOPDescription=0x102;
        public static final int GetEnabledShowMANotice=0x103;
        public static final int GetEnabledShowMAOPDescription=0x104;
        public static final int GetFGNotice=0x105;
        public static final int GetFGOPDescription=0x106;
        public static final int GetFinishedGoodsAlarmCount=0x107;
        public static final int GetFinishedGoodsItems=0x108;
        public static final int GetFinishedGoodsPagesInfo=0x109;
        public static final int GetFinishedGoodsProjectList=0x110;
        public static final int GetMANotice=0x111;
        public static final int GetMAOPDescription=0x112;
        public static final int GetMaterialCount=0x113;
        public static final int GetMaterialItems=0x114;
        public static final int GetMaterialPagesInfo=0x115;
        public static final int GetProductPopupItems=0x116;
        public static final int GetSemiFinishedAlarmCount=0x117;
        public static final int GetSemiFinishedItems=0x118;
        public static final int GetSemiFinishedPagesInfo=0x119;
        public static final int ReturnErrorMessage=0x200;
    }

    //WebService功能参数
    private  KanbanValue kanbanValue;

    //Web筛选数据输入参数
    public enum ShowType { InitShow,ShowAll, Where, NextPage, PreviousPage }
    public String[] ShowTypeString={ "InitShow","ShowAll", "Where", "NextPage", "PreviousPage"};
    private ShowType currentShowType;
    private Double currentPageNum;
    private Double currentItemsOfPage;
    private String currentWhere;
    private String currentProductItem;

    public WebServiceUtil(Handler handler){
        super();
        this.handler=handler;
        kanbanValue=new KanbanValue();
    }

    //开始一个新的工作任务
    private boolean executeMethod(WorkType workType) {
        if (CurrentWorkType != null) {
            /*if (this.getState() != State.TERMINATED) {
                Message msg=PackMessageWithError("上一个查询线程没有正确结束！");
                handler.sendMessage(msg);
                return false;
            }*/
            CurrentWorkType = null;
        }
        CurrentWorkType = workType;
        this.start();
        return true;
    }

    //测试Web通讯能力
    public void getTestString(){
        executeMethod(WorkType.HelloWord);
    }
    //原材料方法
    public void getEnabledShowMANotice(){
        executeMethod(WorkType.GetEnabledShowMANotice);
    }
    public void getEnabledShowMAOPDescription(){
        executeMethod(WorkType.GetEnabledShowMAOPDescription);
    }
    public void getMANotice(){
        executeMethod(WorkType.GetMANotice);
    }
    public void getMAOPDescription(){
        executeMethod(WorkType.GetMAOPDescription);
    }
    public void getMaterialCount(){
        executeMethod(WorkType.GetMaterialCount);
    }
    public void getMaterialItems(ShowType Type,Double PageNum,Double ItemsOfPage,String Where){
            currentShowType=Type;
            currentPageNum=PageNum;
            currentItemsOfPage=ItemsOfPage;
            currentWhere=Where;
            executeMethod(WorkType.GetMaterialItems);
    }
    public void getMaterialPagesInfo(Double ItemsOfPage){
        currentItemsOfPage=ItemsOfPage;
        executeMethod(WorkType.GetMaterialPagesInfo);
    }


    //成品方法
    public void getEnabledShowFGNotice(){
        executeMethod(WorkType.GetEnabledShowFGNotice);
    }
    public void getEnabledShowFGOPDescription(){
        executeMethod(WorkType.GetEnabledShowFGOPDescription);
    }
    public void getFGNotice(){
        executeMethod(WorkType.GetFGNotice);
    }
    public void getFGOPDescription(){
        executeMethod(WorkType.GetFGOPDescription);
    }
    public void getFinishedGoodsAlarmCount(){
        executeMethod(WorkType.GetFinishedGoodsAlarmCount);
    }
    public void getFinishedGoodsItems(ShowType Type,Double PageNum,Double ItemsOfPage,String Where){
        currentShowType = Type;
        currentPageNum = PageNum;
        currentItemsOfPage = ItemsOfPage;
        currentWhere = Where;
        executeMethod(WorkType.GetFinishedGoodsItems);
    }
    public void getFinishedGoodsPagesInfo(Double ItemsOfPage){
        currentItemsOfPage=ItemsOfPage;
        executeMethod(WorkType.GetFinishedGoodsPagesInfo);
    }
    public void getFinishedGoodsProjectList(){
        executeMethod(WorkType.GetFinishedGoodsProjectList);
    }
    public void getProductPopupItems(String ProductItem){
        currentProductItem=ProductItem;
        executeMethod(WorkType.GetProductPopupItems);
    }

    //半成品方法
    public void getSemiFinishedAlarmCount(){
        executeMethod(WorkType.GetSemiFinishedAlarmCount);
    }
    public void getSemiFinishedItems(ShowType Type,Double PageNum,Double ItemsOfPage,String Where){
        currentShowType=Type;
        currentPageNum=PageNum;
        currentItemsOfPage=ItemsOfPage;
        currentWhere=Where;
        executeMethod(WorkType.GetSemiFinishedItems);
    }
    public void getSemiFinishedPagesInfo(Double ItemsOfPage){
        currentItemsOfPage=ItemsOfPage;
        executeMethod(WorkType.GetSemiFinishedPagesInfo);
    }

    @Override
    public void run() {
        super.run();

    }

    private void RunMethod(){
        HttpTransportSE ht;
        SoapSerializationEnvelope envelope;
        SoapObject soapObject;
        Bundle bundle=null;

        int type=CurrentWorkType.ordinal();
        boolean Result=true;
        String ReturnMessage="";
        try{
            bundle = new Bundle();
            ht = new HttpTransportSE(SERVICE_URL,3000);
            ht.debug = true;

            soapObject = new SoapObject(SERVICE_NAMESPACE,MethodNames[type]);

            //region 设置方法输入参数
            switch(CurrentWorkType){
                case GetMaterialItems:
                case GetSemiFinishedItems:
                case GetFinishedGoodsItems:
                    soapObject.addProperty("type",ShowTypeString[currentShowType.ordinal()]);
                    soapObject.addProperty("PageNum",String.valueOf(currentPageNum));
                    soapObject.addProperty("ItemsOfPage",String.valueOf(currentItemsOfPage));
                    soapObject.addProperty("Where",currentWhere);
                    break;
                case GetProductPopupItems:
                    soapObject.addProperty("ProductItem",currentProductItem);
                    break;
                case GetMaterialPagesInfo:
                case GetSemiFinishedPagesInfo:
                case GetFinishedGoodsPagesInfo:
                    soapObject.addProperty("ItemsOfPage",String.valueOf(currentItemsOfPage));
                    break;
            }
            //endregion

            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;

            String str=SERVICE_NAMESPACE+MethodNames[type];
            ht.call(str,envelope);
            if(envelope.getResponse()!=null){
                SoapObject r=(SoapObject)envelope.bodyIn;
                String act=MethodNames[type]+"Result";

                //region 获取返回数据
                switch(CurrentWorkType){
                    case HelloWord:
                        kanbanValue.testString=r.getProperty(act).toString();
                        break;
                    case GetEnabledShowMANotice:
                        kanbanValue.enabledShowMANotice=r.getProperty(act).equals("true");
                        break;
                    case GetEnabledShowMAOPDescription:
                        kanbanValue.enabledShowMAOPDescription=r.getProperty(act).equals("true");
                        break;
                    case GetMANotice:
                        kanbanValue.mANotice=ConvertToStringArray((SoapObject)r.getProperty(act));
                        break;
                    case GetMAOPDescription:
                        kanbanValue.mAOPDescription=ConvertToStringArray((SoapObject)r.getProperty(act));
                        break;
                    case GetMaterialCount:
                        kanbanValue.materialCount=Integer.valueOf(r.getProperty(act).toString());
                        break;
                    case GetMaterialItems:
                        kanbanValue.materialItems=ConvertToMaterialItem((SoapObject)r.getProperty(act));
                        break;
                    case GetMaterialPagesInfo:
                        kanbanValue.materialPagesInfo=ConvertToPagesInfo((SoapObject)r.getProperty(act));
                        break;
                }
                //endregion
                bundle.putSerializable("KanbanValue",kanbanValue);

                Result=true;
            }
            else{
                Result=false;
                ReturnMessage="读取Web数据失败!";
            }
        }catch(Exception ex){
            Result=false;
            ReturnMessage=ex.toString();
        }finally {
            Message m=new Message();
            m.what=MessageHandler[type];
            bundle.putBoolean("Result",Result);
            bundle.putString("ReturnMessage",ReturnMessage);
            m.setData(bundle);
            handler.sendMessage(m);
        }
    }

    //将错误信息装进包中，准备发送到主线程
    private Message PackMessageWithError(String errorMessage){
        Bundle bundle=new Bundle();
        bundle.putBoolean("Result",false);
        bundle.putString("ReturnMessage",errorMessage);

        Message msg=new Message();
        msg.setData(bundle);
        return msg;
    }

    //从返回数据提取数组字符串
    public List<String> ConvertToStringArray(SoapObject detail){
        try{
            List<String> r= new ArrayList<>();
            for(int i=0;i<detail.getPropertyCount();i++){
                r.add(detail.getProperty(i).toString());
            }
            return r;
        }catch(Exception ex){
            return null;
        }
    }

    //从返回数据提取分页信息
    public PagesInfo ConvertToPagesInfo(SoapObject detail){
        try{
            PagesInfo p=new PagesInfo();
            p.TotalPages=Double.parseDouble(detail.getProperty("TotalPages").toString());
            p.TotalItems=Double.parseDouble(detail.getProperty("TotalItems").toString());
            p.ItemsOfPage=Double.parseDouble(detail.getProperty("ItemsOfPage").toString());
            return p;
        }catch(Exception ex){
            return null;
        }
    }

    //从返回数据提取数据项
    public List<MaterialItem> ConvertToMaterialItem(SoapObject detail){
        try{
            List<MaterialItem> m=new ArrayList<>();
            for(int i=0;i<detail.getPropertyCount();i++){
                MaterialItem mm=new MaterialItem();
                SoapObject r=(SoapObject)detail.getProperty(i);
                mm.partNo=r.getProperty("PartNo").toString();
                mm.bPCS=r.getProperty("BPCS").toString();
                mm.planNeed=Double.parseDouble(r.getProperty("PlanNeed").toString());
                mm.cqQTY=Double.parseDouble(r.getProperty("CQ").toString());
                mm.wipStock=Double.parseDouble(r.getProperty("WIPStock").toString());
                mm.deliveryNeed=Double.parseDouble(r.getProperty("DeliveryNeed").toString());
                mm.finishedDelivery=Double.parseDouble(r.getProperty("FinishedDelivery").toString());
                mm.waitDelivery=Double.parseDouble(r.getProperty("WaitDelivery").toString());
                mm.stdPackage=Double.parseDouble(r.getProperty("StdPackage").toString());
                mm.needPackage=Double.parseDouble(r.getProperty("NeedPackage").toString());
                mm.materialStock=Double.parseDouble(r.getProperty("MaterialStock").toString());
                mm.minStock=Double.parseDouble(r.getProperty("MinStock").toString());
                mm.maxStock=Double.parseDouble(r.getProperty("MaxStock").toString());
                mm.lessThanMinStock=Double.parseDouble(r.getProperty("LessThanMinStock").toString());
                mm.highThanMaxStock=Double.parseDouble(r.getProperty("HighThanMaxStock").toString());
                mm.deliveryDate=r.getProperty("DeliveryDate").toString();
                mm.deliveryQTY=Double.parseDouble(r.getProperty("DeliveryQTY").toString());
                m.add(mm);
            }
            return m;
        }catch(Exception ex){
            return null;
        }

    }
    public List<FinishedGoodsItem> ConvertToFinishedGoodsItem(SoapObject detail){
        try{
            List<FinishedGoodsItem> f=new ArrayList<>();
            for(int i=0;i<detail.getPropertyCount();i++){
                SoapObject r=(SoapObject)detail.getProperty(i);
                FinishedGoodsItem ff=new FinishedGoodsItem();
                ff.Project=r.getProperty("Project").toString();
                ff.PartNo=r.getProperty("PartNo").toString();
                ff.BPCS=r.getProperty("BPCS").toString();
                ff.Type=r.getProperty("Type").toString();
                ff.ProductPlan=Double.parseDouble(r.getProperty("ProductPlan").toString());
                ff.FinishedPlan=Double.parseDouble(r.getProperty("FinishedPlan").toString());
                ff.WaitProduct=Double.parseDouble(r.getProperty("WaitProduct").toString());
                ff.ActualStock=Double.parseDouble(r.getProperty("ActualStock").toString());
                ff.MinStock=Double.parseDouble(r.getProperty("MinStock").toString());
                ff.MaxStock=Double.parseDouble(r.getProperty("MaxStock").toString());
                ff.UrgentNeed=Double.parseDouble(r.getProperty("UrgentNeed").toString());
                f.add(ff);
            }
            return f;
        }catch(Exception ex){
            return null;
        }
    }
    public List<SemiFinishedItem> ConvertToSemiFinishedItem(SoapObject detail){
        try{
            List<SemiFinishedItem> s=new ArrayList<>();
            for(int i=0;i<detail.getPropertyCount();i++){
                SoapObject r=(SoapObject)detail.getProperty(i);
                SemiFinishedItem ss=new SemiFinishedItem();
                ss.partNo=r.getProperty("PartNo").toString();
                ss.bPCS=r.getProperty("BPCS").toString();
                ss.type=r.getProperty("Type").toString();
                ss.minPackage=Double.parseDouble(r.getProperty("MinPackage").toString());
                ss.productPlan=Double.parseDouble(r.getProperty("ProductPlan").toString());
                ss.finishedPlan=Double.parseDouble(r.getProperty("FinishedPlan").toString());
                ss.waitProduct=Double.parseDouble(r.getProperty("WaitProduct").toString());
                ss.actualStock=Double.parseDouble(r.getProperty("ActualStock").toString());
                ss.minStock=Double.parseDouble(r.getProperty("MinStock").toString());
                ss.maxStock=Double.parseDouble(r.getProperty("MaxStock").toString());
                ss.urgentNeed=Double.parseDouble(r.getProperty("UrgentNeed").toString());
                s.add(ss);
            }
            return  s;
        }catch(Exception ex){
            return null;
        }
    }
    public List<ProductPopupItem> ConvertToProductPopupItem(SoapObject detail){
        try{
            List<ProductPopupItem> p=new ArrayList<>();
            for(int i=0;i<detail.getPropertyCount();i++){
                SoapObject r=(SoapObject)detail.getProperty(i);
                ProductPopupItem pp=new ProductPopupItem();
                pp.parentItem=r.getProperty("ParentItem").toString();
                pp.bPCS=r.getProperty("BPCS").toString();
                pp.drawing=r.getProperty("Drawing").toString();
                pp.quantity= Double.parseDouble(r.getProperty("Quantity").toString());
                pp.productPlan=Double.parseDouble(r.getProperty("ProductPlan").toString());
                pp.planNeed=Double.parseDouble(r.getProperty("PlanNeed").toString());
                pp.wipStock=Double.parseDouble(r.getProperty("WIPStock").toString());
                pp.finishedDelivery=Double.parseDouble(r.getProperty("FinishedDelivery").toString());
                pp.materialStock=Double.parseDouble(r.getProperty("MaterialStock").toString());
                pp.stdPackage=Double.parseDouble(r.getProperty("StdPackage").toString());
                pp.minStock=Double.parseDouble(r.getProperty("MinStock").toString());
                pp.maxStock=Double.parseDouble(r.getProperty("MaxStock").toString());
                pp.deliveryDate=r.getProperty("DeliveryDate").toString();
                pp.lessThenMinStock=Double.parseDouble(r.getProperty("LessThenMinStock").toString());
                pp.highThenMaxStock=Double.parseDouble(r.getProperty("HighThenMaxStock").toString());
                pp.deliveryNeed=Double.parseDouble(r.getProperty("DeliveryNeed").toString());

                p.add(pp);
            }
            return p;
        }catch(Exception ex){
            return null;
        }
    }
}

