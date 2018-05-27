package com.xmlspace.kanban;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public  class WebServiceUtil2 {

    // 定义webservice的命名空间
    private static final String SERVICE_NAMESPACE = "http://tempuri.org/";
    // 定义webservice提供服务的url
    private static final String SERVICE_URL = "http://192.168.2.104/kanban/KanbanWebservice.asmx";

    //设置工作类型
    private enum WorkType{
        getHelloWord,getEnabledShowFGNotice,getEnabledShowFGOPDescription,getEnabledShowMANotice,getEnabledShowMAOPDescription,
        getFGNotice,getFGOPDescription,getFinishedGoodsAlarmCount,getFinishedGoodsItems,getFinishedGoodsPagesInfo,
        getFinishedGoodsProjectList,getMANotice,getMAOPDescription,getMaterialCount,getMaterialItems,getMaterialPagesInfo,
        getProductPopupItems,getSemiFinishedAlarmCount,getSemiFinishedItems,getSemiFinishedPagesInfo
    }

    private static String[] MethodNames={
            "HelloWorld","GetEnabledShowFGNotice","GetEnabledShowFGOPDescription","GetEnabledShowMANotice","GetEnabledShowMAOPDescription",
            "GetFGNotice","GetFGOPDescription","GetFinishedGoodsAlarmCount","GetFinishedGoodsItems","GetFinishedGoodsPagesInfo",
            "GetFinishedGoodsProjectList","GetMANotice","GetMAOPDescription","GetMaterialCount","GetMaterialItems","GetMaterialPagesInfo",
            "GetProductPopupItems","GetSemiFinishedAlarmCount","GetSemiFinishedItems","GetSemiFinishedPagesInfo"
    };

    //Web筛选数据输入参数
    public enum ShowType { InitShow,ShowAll, Where, NextPage, PreviousPage }
    private static String[] ShowTypeString={ "InitShow","ShowAll", "Where", "NextPage", "PreviousPage"};
    private static ShowType currentShowType;
    private static Double currentPageNum;
    private static Double currentItemsOfPage;
    private static String currentWhere;
    private static String currentProductItem;

    public WebServiceUtil2(){

    }

    //获取原材料分页信息
    public static KanbanValue getMaterialPageInfo(double ItemsOfPage){
        currentItemsOfPage=ItemsOfPage;
        return RunMethod(WorkType.getMaterialPagesInfo);
    }
    //获取成品分页信息
    public static KanbanValue getFinishedGoodsPageInfo(double ItemsOfPage){
        currentItemsOfPage= ItemsOfPage;
        return RunMethod(WorkType.getFinishedGoodsPagesInfo);
    }
    //获取半成品分页信息
    public static KanbanValue getSemiFinishedPageInfo(double ItemsOfPage){
        currentItemsOfPage=ItemsOfPage;
        return RunMethod(WorkType.getSemiFinishedPagesInfo);
    }

    //获取原材料看板数据
    public static KanbanValue getMaterialItems(ShowType showType,double pageNum,double itemsOfPage,String Where){
        currentPageNum=pageNum;
        currentItemsOfPage=itemsOfPage;
        currentWhere=Where;
        currentShowType=showType;
        return RunMethod(WorkType.getMaterialItems);
    }
    //获取成品看板数据
    public static KanbanValue getFinishedGoodsItems(ShowType showType,double pageNum,double itemsOfPage,String Where){
        currentPageNum=pageNum;
        currentItemsOfPage=itemsOfPage;
        currentWhere=Where;
        currentShowType=showType;
        return RunMethod(WorkType.getFinishedGoodsItems);
    }
    //获取半成品看板数据
    public static KanbanValue getSemiFinishedItems(ShowType showType,double pageNum,double itemsOfPage,String Where){
        currentPageNum=pageNum;
        currentItemsOfPage=itemsOfPage;
        currentWhere = Where;
        currentShowType=showType;
        return RunMethod(WorkType.getSemiFinishedItems);
    }

    //执行主体方法
    private static KanbanValue RunMethod(WorkType CurrentWorkType){
        HttpTransportSE ht;
        SoapSerializationEnvelope envelope;
        SoapObject soapObject;
        KanbanValue kanbanValue=new KanbanValue();
        try{
            ht = new HttpTransportSE(SERVICE_URL,3000);
            ht.debug = true;
            soapObject = new SoapObject(SERVICE_NAMESPACE,MethodNames[CurrentWorkType.ordinal()]);
            //region 设置方法输入参数
            switch(CurrentWorkType){
                case getMaterialItems:
                case getSemiFinishedItems:
                case getFinishedGoodsItems:
                    soapObject.addProperty("type",ShowTypeString[currentShowType.ordinal()]);
                    soapObject.addProperty("PageNum",String.valueOf(currentPageNum));
                    soapObject.addProperty("ItemsOfPage",String.valueOf(currentItemsOfPage));
                    soapObject.addProperty("Where",currentWhere);
                    break;
                case getProductPopupItems:
                    soapObject.addProperty("ProductItem",currentProductItem);
                    break;
                case getMaterialPagesInfo:
                case getSemiFinishedPagesInfo:
                case getFinishedGoodsPagesInfo:
                    soapObject.addProperty("ItemsOfPage",String.valueOf(currentItemsOfPage));
                    break;
            }
            //endregion
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;

            String str=SERVICE_NAMESPACE+MethodNames[CurrentWorkType.ordinal()];
            ht.call(str,envelope);
            if(envelope.getResponse()!=null){
                SoapObject r=(SoapObject)envelope.bodyIn;
                String act=MethodNames[CurrentWorkType.ordinal()]+"Result";

                //region 获取返回数据
                switch(CurrentWorkType){
                    case getHelloWord:
                        kanbanValue.testString=r.getProperty(act).toString();
                        break;
                    case getEnabledShowMANotice:
                        kanbanValue.enabledShowMANotice=r.getProperty(act).equals("true");
                        break;
                    case getEnabledShowMAOPDescription:
                        kanbanValue.enabledShowMAOPDescription=r.getProperty(act).equals("true");
                        break;
                    case getMANotice:
                        kanbanValue.mANotice=ConvertToStringArray((SoapObject)r.getProperty(act));
                        break;
                    case getMAOPDescription:
                        kanbanValue.mAOPDescription=ConvertToStringArray((SoapObject)r.getProperty(act));
                        break;
                    case getMaterialCount:
                        kanbanValue.materialCount=Integer.valueOf(r.getProperty(act).toString());
                        break;
                    case getMaterialItems:
                        kanbanValue.materialItems=ConvertToMaterialItem((SoapObject)r.getProperty(act));
                        break;
                    case getFinishedGoodsItems:
                        kanbanValue.finishedGoodsItems=ConvertToFinishedGoodsItem((SoapObject)r.getProperty(act));
                        break;
                    case getSemiFinishedItems:
                        kanbanValue.semiFinishedItems=ConvertToSemiFinishedItem((SoapObject)r.getProperty(act));
                    case getMaterialPagesInfo:
                        kanbanValue.materialPagesInfo=ConvertToPagesInfo((SoapObject)r.getProperty(act));
                        break;
                    case getFinishedGoodsPagesInfo:
                        kanbanValue.finishedGoodsPagesInfo=ConvertToPagesInfo((SoapObject)r.getProperty(act));
                        break;
                    case getSemiFinishedPagesInfo:
                        kanbanValue.semiFinishedPagesInfo=ConvertToPagesInfo((SoapObject)r.getProperty(act));
                }
                //endregion
                kanbanValue.errorSign=false;
            }
            else{
                kanbanValue.errorSign=true;
                kanbanValue.errorMessage="WebService获取数据失败!";
            }
        }catch(Exception ex){
            kanbanValue.errorSign=true;
            kanbanValue.errorMessage="WebService获取数据失败,返回的错误信息："+ex.toString();
        }
        return kanbanValue;
    }

    //从返回数据提取数组字符串
    @Nullable
    private static List<String> ConvertToStringArray(SoapObject detail){
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
    @Nullable
    private static PagesInfo ConvertToPagesInfo(SoapObject detail){
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
    @Nullable
    private static List<MaterialItem> ConvertToMaterialItem(SoapObject detail){
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
    @Nullable
    private static List<FinishedGoodsItem> ConvertToFinishedGoodsItem(SoapObject detail){
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
    @Nullable
    private static List<SemiFinishedItem> ConvertToSemiFinishedItem(SoapObject detail){
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
    @Nullable
    private static List<ProductPopupItem> ConvertToProductPopupItem(SoapObject detail){
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
