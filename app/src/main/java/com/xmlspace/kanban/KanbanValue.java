package com.xmlspace.kanban;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KanbanValue implements Serializable {
    public String testString;
    public boolean errorSign;
    public String errorMessage;

    //原材料数据
    public boolean enabledShowMANotice;
    public boolean enabledShowMAOPDescription;
    public List<String> mANotice;
    public List<String> mAOPDescription;
    public int materialCount;
    public List<MaterialItem> materialItems;
    public PagesInfo materialPagesInfo;

    //成品数据
    public boolean enabledShowFGNotice;
    public boolean enabledShowFGOPDescription;
    public List<String> fGNotice;
    public List<String> fGOPDescription;
    public int finishedGoodsAlarmCount;
    public List<FinishedGoodsItem> finishedGoodsItems;
    public PagesInfo finishedGoodsPagesInfo;
    public List<String> finishedGoodsProjectList;
    public List<ProductPopupItem> productPopupItems;

    //半成品数据
    public int semiFinishedAlarmCount;
    public List<SemiFinishedItem> semiFinishedItems;
    public PagesInfo semiFinishedPagesInfo;

    public KanbanValue(){
        testString="";
        enabledShowMANotice=false;
        enabledShowMAOPDescription=false;
        mANotice=new ArrayList<>();
        mAOPDescription=new ArrayList<>();
        materialCount=0;
        materialItems=new ArrayList<>();
        materialPagesInfo=new PagesInfo();

        enabledShowFGNotice=false;
        enabledShowFGOPDescription=false;
        fGNotice=new ArrayList<>();
        fGOPDescription=new ArrayList<>();
        finishedGoodsAlarmCount=0;
        finishedGoodsItems = new ArrayList<>();
        finishedGoodsPagesInfo=new PagesInfo();
        finishedGoodsProjectList=new ArrayList<>();
        productPopupItems=new ArrayList<>();

        semiFinishedAlarmCount=0;
        semiFinishedItems=new ArrayList<>();
        semiFinishedPagesInfo=new PagesInfo();
    }
}
