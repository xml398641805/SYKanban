package com.xmlspace.kanban;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MaterialListAdapter extends BaseAdapter {
    private List<MaterialItem> materialItemList;
    private LayoutInflater mInflater;
    private MyPreference myPreference;

    public MaterialListAdapter(Context context,List<MaterialItem> materialItems)
    {
        this.mInflater= LayoutInflater.from(context);
        this.materialItemList=materialItems;
        myPreference=new MyPreference(context);
    }

    @Override
    public int getCount() {
        return materialItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        float textSize=(float)myPreference.getListItemFontSize();
        if(convertView==null){
            convertView=(View)mInflater.inflate(R.layout.activity_material_kanban_list_content,null);
            holder=new ViewHolder();

            holder.tv_part=(TextView)convertView.findViewById(R.id.tv_part);
            holder.tv_bpcs=(TextView)convertView.findViewById(R.id.tv_bpcs);
            holder.tv_stock=(TextView)convertView.findViewById(R.id.tv_stock);
            holder.tv_delivery_need=(TextView)convertView.findViewById(R.id.tv_delivery_need);
            holder.tv_wait_delivery=(TextView)convertView.findViewById(R.id.tv_wait_delivery);
            holder.tv_min_stock=(TextView)convertView.findViewById(R.id.tv_min_stock);
            holder.tv_max_stock=(TextView)convertView.findViewById(R.id.tv_max_stock);
            holder.tv_urgent=(TextView)convertView.findViewById(R.id.tv_urgent);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        MaterialItem item=materialItemList.get(position);
        holder.tv_part.setText(item.partNo);
        holder.tv_part.setHeight(myPreference.getListShowRecordHigh());
        holder.tv_part.setTextSize(textSize);
        holder.tv_bpcs.setText(item.bPCS);
        holder.tv_bpcs.setTextSize(textSize);
        holder.tv_stock.setText(String.valueOf((int)Math.ceil(item.materialStock)));
        holder.tv_stock.setTextSize(textSize);
        holder.tv_delivery_need.setText(String.valueOf((int)Math.ceil(item.deliveryNeed)));
        holder.tv_delivery_need.setTextSize(textSize);
        holder.tv_wait_delivery.setText(String.valueOf((int)Math.ceil(item.waitDelivery)));
        holder.tv_wait_delivery.setTextSize(textSize);
        holder.tv_min_stock.setText(String.valueOf((int)Math.ceil(item.minStock)));
        holder.tv_min_stock.setTextSize(textSize);
        holder.tv_max_stock.setText(String.valueOf((int)Math.ceil(item.maxStock)));
        holder.tv_max_stock.setTextSize(textSize);
        if(item.materialStock<item.minStock) {
            holder.tv_urgent.setText(String.valueOf((int)Math.ceil(item.minStock-item.materialStock)));
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
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public final class ViewHolder{
        public TextView tv_part;
        public TextView tv_bpcs;
        public TextView tv_delivery_need;
        public TextView tv_wait_delivery;
        public TextView tv_stock;
        public TextView tv_min_stock;
        public TextView tv_max_stock;
        public TextView tv_urgent;
    }
}
