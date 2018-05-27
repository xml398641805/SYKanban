package com.xmlspace.kanban;

public class MaterialItem {
    public String partNo;
    public String bPCS;
    public double planNeed;
    public double cqQTY;
    public double wipStock;
    public double deliveryNeed;
    public double finishedDelivery;
    public double waitDelivery;
    public double stdPackage;
    public double needPackage;
    public double materialStock;
    public double minStock;
    public double maxStock;
    public double lessThanMinStock;
    public double highThanMaxStock;
    public String deliveryDate;
    public double deliveryQTY;

    public MaterialItem(){
        partNo="";
        bPCS="";
        planNeed=0;
        cqQTY=0;
        wipStock=0;
        deliveryNeed=0;
        finishedDelivery=0;
        waitDelivery=0;
        stdPackage=0;
        needPackage=0;
        materialStock=0;
        minStock=0;
        maxStock=0;
        lessThanMinStock=0;
        highThanMaxStock=0;
        deliveryDate="";
        deliveryQTY=0;
    }
}
