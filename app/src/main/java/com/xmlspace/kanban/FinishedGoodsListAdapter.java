package com.xmlspace.kanban;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FinishedGoodsListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private MyPreference myPreference;
    private List<FinishedGoodsItem> finishedGoodsItemList;

    public FinishedGoodsListAdapter(Context context, List<FinishedGoodsItem> finishedGoodsItems){
        this.mInflater= LayoutInflater.from(context);
        this.finishedGoodsItemList=finishedGoodsItems;
        myPreference=new MyPreference(context);
    }

    @Override
    public int getCount() {
        return finishedGoodsItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return finishedGoodsItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        float textSize=(float)myPreference.getListItemFontSize();
        if(convertView==null){
            convertView=(View)mInflater.inflate(R.layout.activity_finished_goods_kanban_list_content,null);
            holder=new ViewHolder();

            holder.tv_project=(TextView)convertView.findViewById(R.id.tv_project);
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
        FinishedGoodsItem item=finishedGoodsItemList.get(position);
        holder.tv_project.setText(item.Project);
        holder.tv_project.setTextSize(textSize);
        holder.tv_part.setText(item.PartNo);
        holder.tv_part.setHeight(myPreference.getListShowRecordHigh());
        holder.tv_part.setTextSize(textSize);
        holder.tv_bpcs.setText(item.BPCS);
        holder.tv_bpcs.setTextSize(textSize);
        holder.tv_type.setText(item.Type);
        holder.tv_type.setTextSize(textSize);
        holder.tv_finished_product_plan.setText(String.valueOf((int)Math.ceil(item.FinishedPlan))+"/"+String.valueOf((int)Math.ceil(item.ProductPlan)));
        holder.tv_finished_product_plan.setTextSize(textSize);
        holder.tv_wait_product.setText(String.valueOf((int)Math.ceil(item.WaitProduct)));
        holder.tv_wait_product.setTextSize(textSize);
        holder.tv_actual_stock.setText(String.valueOf((int)Math.ceil(item.ActualStock)));
        holder.tv_actual_stock.setTextSize(textSize);
        holder.tv_min_stock.setText(String.valueOf((int)Math.ceil(item.MinStock)));
        holder.tv_min_stock.setTextSize(textSize);
        holder.tv_max_stock.setText(String.valueOf((int)Math.ceil(item.MaxStock)));
        holder.tv_max_stock.setTextSize(textSize);
        if(item.ActualStock<item.MinStock) {
            holder.tv_urgent.setText(String.valueOf((int)Math.ceil(item.MinStock-item.ActualStock)));
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
        public TextView tv_project;
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
