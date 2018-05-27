package com.xmlspace.kanban;

public class SemiFinishedItem {
    public String partNo;
    public String bPCS;
    public String type;
    public double minPackage;
    public double productPlan;
    public double finishedPlan;
    public double waitProduct;
    public double actualStock;
    public double minStock;
    public double maxStock;
    public double urgentNeed;

    public SemiFinishedItem(){
        partNo="";
        bPCS="";
        type="";
        minPackage=0;
        productPlan=0;
        finishedPlan=0;
        waitProduct=0;
        actualStock=0;
        minStock=0;
        maxStock=0;
        urgentNeed=0;
    }
}
