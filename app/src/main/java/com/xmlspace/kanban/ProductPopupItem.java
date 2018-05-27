package com.xmlspace.kanban;

public class ProductPopupItem {
    public String parentItem;
    public String bPCS;
    public String drawing;
    public double quantity;
    public double productPlan;
    public double planNeed;
    public double wipStock;
    public double finishedDelivery;
    public double materialStock;
    public double stdPackage;
    public double minStock;
    public double maxStock;
    public String deliveryDate;
    public double lessThenMinStock;
    public double highThenMaxStock;
    public double deliveryNeed;

    public ProductPopupItem(){
        parentItem="";
        bPCS="";
        drawing="";
        quantity=0;
        productPlan=0;
        planNeed=0;
        wipStock=0;
        finishedDelivery=0;
        materialStock=0;
        stdPackage=0;
        minStock=0;
        maxStock=0;
        deliveryDate="";
        lessThenMinStock=0;
        highThenMaxStock=0;
        deliveryNeed=0;
    }
}
