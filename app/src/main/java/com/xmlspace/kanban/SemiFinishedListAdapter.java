package com.xmlspace.kanban;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SemiFinishedListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private MyPreference myPreference;
    private List<SemiFinishedItem> semiFinishedList;

    public SemiFinishedListAdapter(Context context,List<SemiFinishedItem> semifinishedItems){
        this.mInflater= LayoutInflater.from(context);
        this.semiFinishedList=semifinishedItems;
        myPreference=new MyPreference(context);
    }

    @Override
    public int getCount() {
        return semiFinishedList.size();
    }

    @Override
    public Object getItem(int position) {
        return semiFinishedList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        float textSize=(float)myPreference.getListItemFontSize();
        if(convertView==null){
            convertView=(View)mInflater.inflate(R.layout.activity_semi_finished_kanban_list_content,null);
            holder=new ViewHolder();

            holder.tv_part=(TextView)convertView.findViewById(R.id.tv_part);
            holder.tv_bpcs=(TextView)convertView.findViewById(R.id.tv_bpcs);
            holder.tv_type=(TextView)convertView.findViewById(R.id.tv_type);
            holder.tv_finished_product_plan=(TextView)convertView.findViewById(R.id.tv_finished_product_plan);
            holder.tv_wait_product=(TextView)convertView.findViewById(R.id.tv_wait_product);
            holder.tv_actual_stock=(TextView)convertView.findViewById(R.id.tv_actual_stock);
            holder.tv_min_stock=(TextView)convertView.findViewById(R.id.tv_min_stock);
            holder.tv_max_stock=(TextView)convertView.findViewById(R.id.tv_max_stock);
            holder.tv_urgent=(TextView)convertView.findViewById(R.id.tv_urgent);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        SemiFinishedItem item=semiFinishedList.get(position);
        holder.tv_part.setText(item.partNo);
        holder.tv_part.setHeight(myPreference.getListShowRecordHigh());
        holder.tv_part.setTextSize(textSize);
        holder.tv_bpcs.setText(item.bPCS);
        holder.tv_bpcs.setTextSize(textSize);
        holder.tv_type.setText(item.type);
        holder.tv_type.setTextSize(textSize);
        holder.tv_finished_product_plan.setText(String.valueOf((int)Math.ceil(item.finishedPlan))+"/"+String.valueOf((int)Math.ceil(item.productPlan)));
        holder.tv_finished_product_plan.setTextSize(textSize);
        holder.tv_wait_product.setText(String.valueOf((int)Math.ceil(item.waitProduct)));
        holder.tv_wait_product.setTextSize(textSize);
        holder.tv_actual_stock.setText(String.valueOf((int)Math.ceil(item.actualStock)));
        holder.tv_actual_stock.setTextSize(textSize);
        holder.tv_min_stock.setText(String.valueOf((int)Math.ceil(item.minStock)));
        holder.tv_min_stock.setTextSize(textSize);
        holder.tv_max_stock.setText(String.valueOf((int)Math.ceil(item.maxStock)));
        holder.tv_max_stock.setTextSize(textSize);
        if(item.actualStock<item.minStock) {
            holder.tv_urgent.setText(String.valueOf((int)Math.ceil(item.minStock-item.actualStock)));
        }
        else{
            holder.tv_urgent.setText("0");
        }
        holder.tv_urgent.setTextSize(textSize);
        if(position%2==0){
            convertView.setBackgroundResource(R.color.itemInterval);
        }
        else{
            convertView.setBackgroundResource(R.color.blankWhite);
        }

        ViewGroup.LayoutParams p=parent.getLayoutParams();
        p.height=myPreference.getListShowRecordHigh();
        convertView.setLayoutParams(p);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public final class ViewHolder{
        public TextView tv_part;
        public TextView tv_bpcs;
        public TextView tv_type;
        public TextView tv_finished_product_plan;
        public TextView tv_wait_product;
        public TextView tv_actual_stock;
        public TextView tv_min_stock;
        public TextView tv_max_stock;
        public TextView tv_urgent;
    }
}
