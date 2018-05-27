package com.xmlspace.kanban;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPreference {
    private int listTitleFontSize;
    private int listItemFontSize;
    private boolean listAutoTurnPage;
    private int listAutoTurnPageTime;
    private int returnWaitTime;
    private int listShowRecordNum;
    private int listShowRecordHigh;

    private Context mContext;
    private SharedPreferences preferences;

    public MyPreference(Context context){
        this.mContext=context;
        preferences=mContext.getSharedPreferences("Kanban", Activity.MODE_PRIVATE);
    }

    public boolean setPreference(int TitleFontSize,int ItemFontSize,boolean AutoTurnPage,int AutoTurnPageTime,int ReturnWaitTime,int ShowRecordNum,int ShowRecordHigh){
        listTitleFontSize=TitleFontSize;
        listItemFontSize=ItemFontSize;
        listAutoTurnPage=AutoTurnPage;
        listAutoTurnPageTime=AutoTurnPageTime;
        returnWaitTime=ReturnWaitTime;
        listShowRecordNum=ShowRecordNum;
        listShowRecordHigh=ShowRecordHigh;
        boolean result=false;
        try{
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("TitleFontSize",listTitleFontSize);
            editor.putInt("ItemFontSize",listItemFontSize);
            editor.putBoolean("AutoTurnPage",listAutoTurnPage);
            editor.putInt("AutoTurnPageTime",listAutoTurnPageTime);
            editor.putInt("ReturnWaitTime",returnWaitTime);
            editor.putInt("ShowRecordNum",listShowRecordNum);
            editor.putInt("ShowRecordHigh",listShowRecordHigh);
            editor.commit();
            result=true;
        }catch (Exception ex){

        }
        return result;
    }

    public int getListTitleFontSize(){
        try{
            listTitleFontSize=preferences.getInt("TitleFontSize",20);
        }catch(Exception ex){
            listTitleFontSize=20;
        }
        return listTitleFontSize;
    }
    public boolean setListTitleFontSize(int TitleFontSize){
        boolean result=false;
        try{
            listTitleFontSize=TitleFontSize;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("TitleFontSize",listTitleFontSize);
            editor.commit();
            result=true;
        }catch(Exception ex){

        }
        return result;
    }

    public int getListItemFontSize(){
        try{
            listItemFontSize=preferences.getInt("ItemFontSize",20);
        }catch (Exception ex){
            listItemFontSize=20;
        }
        return listItemFontSize;
    }
    public boolean setListItemFontSize(int ItemFontSize){
        boolean result=false;
        try {
            listItemFontSize = ItemFontSize;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("ItemFontSize",listItemFontSize);
            editor.commit();
            result=true;
        }catch(Exception ex){

        }
        return result;
    }

    public boolean getListAutoTurnPage(){
        try{
            listAutoTurnPage=preferences.getBoolean("AutoTurnPage",true);
        }catch(Exception ex){
            listAutoTurnPage=true;
        }
        return listAutoTurnPage;
    }
    public boolean setListAutoTurnPage(boolean AutoTurnPage){
        boolean result=false;
        try {
            listAutoTurnPage = AutoTurnPage;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("AutoTurnPage",listAutoTurnPage);
            editor.commit();
            result=true;
        }catch(Exception ex){

        }
        return result;
    }

    public int getListAutoTurnPageTime(){
        try{
            listAutoTurnPageTime=preferences.getInt("AutoTurnPageTime",5);
        }catch(Exception ex){
            listAutoTurnPageTime=5;
        }
        return listAutoTurnPageTime;
    }
    public boolean setListAutoTurnPageTime(int AutoTurnPageTime){
        boolean result=false;
        try {
            listAutoTurnPageTime = AutoTurnPageTime;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("AutoTurnPageTime",listAutoTurnPageTime);
            editor.commit();
            result=true;
        }catch (Exception ex){
            result=false;
        }
        return result;
    }

    public int getReturnWaitTime(){
        try{
            returnWaitTime=preferences.getInt("ReturnWaitTime",10);
        }catch(Exception ex){
            returnWaitTime=10;
        }
        return returnWaitTime;
    }
    public boolean setReturnWaitTime(int ReturnWaitTime){
        boolean result=false;
        try{
            returnWaitTime=ReturnWaitTime;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("ReturnWaitTime",returnWaitTime);
            editor.commit();
            result=true;
        }catch(Exception ex){
            result=false;
        }
        return result;
    }

    public int getListShowRecordNum(){
        try{
            listShowRecordNum=preferences.getInt("ShowRecordNum",5);
        }catch (Exception ex){
            listShowRecordNum=5;
        }
        return listShowRecordNum;
    }
    public boolean setListShowRecordNum(int ShowRecordNum){
        boolean result=false;
        try{
            listShowRecordNum = ShowRecordNum;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("ShowRecordNum",listShowRecordNum);
            editor.commit();
            result=true;
        }catch(Exception ex){

        }
        return result;
    }

    public int getListShowRecordHigh(){
        try{
            listShowRecordHigh=preferences.getInt("ShowRecordHigh",20);
        }catch(Exception ex){
            listShowRecordHigh=20;
        }
        return listShowRecordHigh;
    }
    public boolean setListShowRecordHigh(int ShowRecordHigh){
        boolean result=false;
        try {
            listShowRecordHigh = ShowRecordHigh;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("ShowRecordHigh",listShowRecordHigh);
            editor.commit();
            result=true;
        }catch (Exception ex){

        }
        return result;
    }


}
