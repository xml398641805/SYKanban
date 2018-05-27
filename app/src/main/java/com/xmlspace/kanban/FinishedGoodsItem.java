package com.xmlspace.kanban;

public class FinishedGoodsItem {
    public String Project;
    public String PartNo;
    public String BPCS;
    public String Type;
    public double ProductPlan;
    public double FinishedPlan;
    public double WaitProduct;
    public double ActualStock;
    public double MinStock;
    public double MaxStock;
    public double UrgentNeed;

    public FinishedGoodsItem(){
        Project = "";
        PartNo="";
        BPCS="";
        Type="";
        ProductPlan=0;
        FinishedPlan = 0;
        WaitProduct=0;
        ActualStock=0;
        MinStock=0;
        MaxStock=0;
        UrgentNeed=0;
    }

}
