package com.xmlspace.kanban;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FinishedGoodsKanban extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //region 看板列表属性
    //ListView中显示的数据项列表
    private List<FinishedGoodsItem> finishedGoodsItemList;
    private FinishedGoodsListAdapter finishedGoodsListAdapter;
    //ListView显示分页信息
    private PagesInfo finishedgoodsPageInfo;
    //当前显示页数
    private int currentShowPage;
    //endregion

    //region 程序执行控制属性
    //显示显示操作类型
    private enum workType{InitShow, NextShow, PreviousShow, AllShow, WaitShow,ErrorStatus}
    //当前显示操作类型
    private workType currentWorkType;
    //当前等待完成的工作任务
    private enum WaitProcess{WaitShowTitle,WaitInitPage,WaitShowData,Finished}
    private WaitProcess currentWaitProcess;
    //循环执行任务定时器
    private Timer mainTimer;
    //定时器循环周期,单位：秒
    private int timerInterval=3;
    //记录上次执行任务的时间
    private Date lastOperateTime;
    //返回参数
    KanbanValue kanbanValue;

    //系统配置参数
    MyPreference myPreference;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_goods_kanban);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //region 设置主屏幕按钮事件
        Button bt_show_less_min=(Button)findViewById(R.id.bt_show_less_min);
        Button bt_show_high_max=(Button)findViewById(R.id.bt_show_high_max);
        Button bt_show_finished_delivery=(Button)findViewById(R.id.bt_show_finished_delivery);
        Button bt_show_unfinished_delivery=(Button)findViewById(R.id.bt_show_unfinished_delivery);

        bt_show_less_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShowLessThanMin();
            }
        });
        bt_show_high_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShowHighThanMax();
            }
        });
        bt_show_finished_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShowFinishedDelivery();
            }
        });
        bt_show_unfinished_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShowUnFinishedDelivery();
            }
        });
        //endregion

        //初始化系统参数
        myPreference=new MyPreference(this);
        //设置看板列表标题
        initShowTitleWithPreference();
        //设置当前显示操作类型为初始显示
        currentWorkType=workType.InitShow;
        currentShowPage=1;
        //初始化主定时器
        initTimer();
    }

    //初始化主定时器
    private void initTimer(){
        mainTimer=new Timer();
        //任务执行主体
        TimerTask tTask=new TimerTask() {
            @Override
            public void run() {
                if(currentWaitProcess==null|| currentWaitProcess==WaitProcess.Finished) {
                    executeMethod();
                }
            }
        };
        mainTimer.schedule(tTask,1000,timerInterval*1000);
    }

    //循环执行主体方法
    private void executeMethod(){
        Date nowTime=new Date();
        long interval=0;
        switch(currentWorkType){
            case InitShow:
                currentWaitProcess=WaitProcess.WaitInitPage;
                if(initPageInfo()){
                    currentWaitProcess=WaitProcess.WaitShowData;
                    if(initShowItemList()){
                        currentWaitProcess=WaitProcess.Finished;
                        currentWorkType=workType.NextShow;
                    }
                    lastOperateTime=new Date();
                }
                break;
            case NextShow:
                interval=(nowTime.getTime()-lastOperateTime.getTime())/1000;
                if(interval>myPreference.getListAutoTurnPageTime()&&myPreference.getListAutoTurnPage()){
                    currentWaitProcess=WaitProcess.WaitShowData;
                    currentWorkType=workType.NextShow;
                    if(initShowItemList()){
                        currentWaitProcess=WaitProcess.Finished;
                    }
                    lastOperateTime=new Date();
                }
                break;
            case PreviousShow:
                interval=(nowTime.getTime()-lastOperateTime.getTime())/1000;
                if(interval>myPreference.getListAutoTurnPageTime()){
                    currentWaitProcess=WaitProcess.WaitShowData;
                    currentWorkType=workType.PreviousShow;
                    if(initShowItemList()){
                        currentWaitProcess=WaitProcess.Finished;
                    }
                    lastOperateTime=new Date();
                }
                break;
            case WaitShow:
                interval=(nowTime.getTime()-lastOperateTime.getTime())/1000;
                if(interval>myPreference.getReturnWaitTime()){
                    currentWorkType=workType.InitShow;
                    currentWaitProcess=WaitProcess.WaitShowData;
                    if(initShowItemList()){
                        currentWaitProcess=WaitProcess.Finished;
                        currentWorkType=workType.NextShow;
                    }
                    lastOperateTime=new Date();
                }
                break;
        }
        //mainThreadIsRun=false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finished_goods_kanban, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_finished_goods_config){
            ShowPreferenceAlert();
        }else if(id==R.id.nav_finished_goods_exit){
            Toast.makeText(this, "退出系统", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //显示看板数据，并设置列表高度
    private void initListHigh(List<FinishedGoodsItem> finishedGoodsItems){
        finishedGoodsListAdapter=new FinishedGoodsListAdapter(FinishedGoodsKanban.this,finishedGoodsItems);
        ListView lv=(ListView)findViewById(R.id.finished_goods_list_item);
        lv.setAdapter(finishedGoodsListAdapter);

        int toolbarHigh=140;
        int listTitleHigh=80;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        ViewGroup.LayoutParams p=lv.getLayoutParams();
        p.height=dm.heightPixels-toolbarHigh-listTitleHigh-15;
        lv.setLayoutParams(p);
    }

    //显示系统配置对话框
    private void ShowPreferenceAlert(){
        AlertDialog.Builder builder;
        builder=new AlertDialog.Builder(this);
        View v= LayoutInflater.from(this).inflate(R.layout.preference_popup,null);
        builder.setView(v);

        //列表标题字体大小设置
        final SeekBar seekBar_list_title_size=(SeekBar)v.findViewById(R.id.seekBar_list_title_size);
        final TextView tv_example_list_title_show=(TextView)v.findViewById(R.id.tv_example_list_title_show);
        seekBar_list_title_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_example_list_title_show.setTextSize((float)progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //列表项字体大小设置
        final SeekBar seekBar_list_item_size=(SeekBar)v.findViewById(R.id.seekBar_list_item_size);
        final TextView tv_example_list_item_show=(TextView)v.findViewById(R.id.tv_example_list_item_show);
        seekBar_list_item_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_example_list_item_show.setTextSize((float) progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //列表自动翻页选项设置
        final Switch switch_enabled_auto_turn_page=(Switch)v.findViewById(R.id.switch_enabled_auto_turn_page);
        //列表自动翻页时间间隔
        final SeekBar seekBar_auto_turn_page_time=(SeekBar)v.findViewById(R.id.seekBar_auto_turn_page_time);
        final TextView tv_auto_turn_page_time_show=(TextView)v.findViewById(R.id.tv_auto_turn_page_time_show);
        seekBar_auto_turn_page_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_auto_turn_page_time_show.setText(String.valueOf(progress)+"秒");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //列表显示记录行数设置
        final SeekBar seekBar_list_show_record_num=(SeekBar)v.findViewById(R.id.seekBar_list_show_record_num);
        final TextView tv_list_show_record_num_show=(TextView)v.findViewById(R.id.tv_list_show_record_num_show);
        seekBar_list_show_record_num.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_list_show_record_num_show.setText(String.valueOf(progress)+"行");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //列表显示记录行高设置
        final SeekBar seekBar_list_record_high_num=(SeekBar)v.findViewById(R.id.seekBar_list_record_high_num);
        final TextView tv_list_record_high_show=(TextView)v.findViewById(R.id.tv_list_record_high_show);
        seekBar_list_record_high_num.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_list_record_high_show.setText(String.valueOf(progress)+"px");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //看板暂停自动返回时间设置
        final SeekBar seekBar_return_wait_time=(SeekBar)v.findViewById(R.id.seekBar_return_wait_time);
        final TextView tv_return_wait_time_show=(TextView)v.findViewById(R.id.tv_return_wait_time_show);
        seekBar_return_wait_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_return_wait_time_show.setText(String.valueOf(progress)+"秒");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myPreference.setPreference(seekBar_list_title_size.getProgress(), seekBar_list_item_size.getProgress(),
                        switch_enabled_auto_turn_page.isChecked(), seekBar_auto_turn_page_time.getProgress(),seekBar_return_wait_time.getProgress(),
                        seekBar_list_show_record_num.getProgress(), seekBar_list_record_high_num.getProgress());
                initShowTitleWithPreference();
                currentWaitProcess= WaitProcess.Finished;
                currentWorkType= workType.InitShow;
            }});

        AlertDialog preference=builder.create();
        preference.show();
        preference.getWindow().setLayout(960,1000);

        seekBar_list_title_size.setProgress(myPreference.getListTitleFontSize());
        tv_example_list_title_show.setTextSize(myPreference.getListTitleFontSize());
        seekBar_list_item_size.setProgress(myPreference.getListItemFontSize());
        tv_example_list_item_show.setTextSize(myPreference.getListItemFontSize());
        switch_enabled_auto_turn_page.setChecked(myPreference.getListAutoTurnPage());
        seekBar_auto_turn_page_time.setProgress(myPreference.getListAutoTurnPageTime());
        tv_auto_turn_page_time_show.setText(String.valueOf(myPreference.getListAutoTurnPageTime())+"秒");
        seekBar_list_show_record_num.setProgress(myPreference.getListShowRecordNum());
        tv_list_show_record_num_show.setText(String.valueOf(myPreference.getListShowRecordNum())+"行");
        seekBar_list_record_high_num.setProgress(myPreference.getListShowRecordHigh());
        tv_list_record_high_show.setText(String.valueOf(myPreference.getListShowRecordHigh())+"PX");
        seekBar_return_wait_time.setProgress(myPreference.getReturnWaitTime());
        tv_return_wait_time_show.setText(String.valueOf(myPreference.getReturnWaitTime())+"秒");
    }

    //初始化看板标题
    private boolean initShowTitleWithPreference(){
        if(myPreference==null){
            return false;
        }
        try{
            float textSize=myPreference.getListTitleFontSize();
            TextView tv_0=(TextView)findViewById(R.id.tv_title_0);
            tv_0.setTextSize(textSize);
            TextView tv_1=(TextView)findViewById(R.id.tv_title_1);
            tv_1.setTextSize(textSize);
            TextView tv_2=(TextView)findViewById(R.id.tv_title_2);
            tv_2.setTextSize(textSize);
            TextView tv_3=(TextView)findViewById(R.id.tv_title_3);
            tv_3.setTextSize(textSize);
            TextView tv_4=(TextView)findViewById(R.id.tv_title_4);
            tv_4.setTextSize(textSize);
            TextView tv_5=(TextView)findViewById(R.id.tv_title_5);
            tv_5.setTextSize(textSize);
            TextView tv_6=(TextView)findViewById(R.id.tv_title_6);
            tv_6.setTextSize(textSize);
            TextView tv_7=(TextView)findViewById(R.id.tv_title_7);
            tv_7.setTextSize(textSize);
            TextView tv_8=(TextView)findViewById(R.id.tv_title_8);
            tv_8.setTextSize(textSize);
            TextView tv_9=(TextView)findViewById(R.id.tv_title_9);
            tv_9.setTextSize(textSize);
        }catch(Exception ex){
            Toast.makeText(this, "设置看板标题失败，返回错误信息：" + ex.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //启动初始化分页信息
    private boolean initPageInfo(){
        if(myPreference==null){
            return false;
        }
        try{
            int showLineNum=myPreference.getListShowRecordNum();
            kanbanValue=WebServiceUtil2.getFinishedGoodsPageInfo((double)showLineNum);
            if(kanbanValue.errorSign||kanbanValue.finishedGoodsPagesInfo==null) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv_page = (TextView) findViewById(R.id.tv_page);
                        tv_page.setText("分页信息错误，请等待下次更新！");
                        Toast.makeText(FinishedGoodsKanban.this, kanbanValue.errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
            finishedgoodsPageInfo=kanbanValue.finishedGoodsPagesInfo;
            currentShowPage=1;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv_page = (TextView) findViewById(R.id.tv_page);
                    tv_page.setText("总页数："+String.valueOf((int)finishedgoodsPageInfo.TotalPages)+"，当前第1页！");
                }
            });
            kanbanValue=null;
        }catch(Exception ex){
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv_page=(TextView)findViewById(R.id.tv_page);
                    tv_page.setText("分页信息错误！");
                    Toast.makeText(FinishedGoodsKanban.this, "启动初始化分页信息失败", Toast.LENGTH_SHORT).show();
                }
            });
            return false;
        }
        return true;
    }

    //启动初始化数据显示
    private boolean initShowItemList(){
        try{
            double showRecordNum=(double)myPreference.getListShowRecordNum();
            switch(currentWorkType){
                case AllShow:
                    kanbanValue=WebServiceUtil2.getFinishedGoodsItems(WebServiceUtil2.ShowType.ShowAll,0.0,0.0,"");
                    break;
                case NextShow:
                    if(currentShowPage>=finishedgoodsPageInfo.TotalPages){
                        currentShowPage=1;
                        currentWorkType= workType.InitShow;
                    }else{
                        currentShowPage+=1;
                    }
                    kanbanValue=WebServiceUtil2.getFinishedGoodsItems(WebServiceUtil2.ShowType.NextPage,(double)currentShowPage,showRecordNum,"");
                    break;
                case PreviousShow:
                    if(currentShowPage==1){
                        currentShowPage=(int)finishedgoodsPageInfo.TotalPages;
                        currentWorkType= workType.InitShow;
                    }else {
                        currentShowPage-=1;
                    }
                    kanbanValue=WebServiceUtil2.getFinishedGoodsItems(WebServiceUtil2.ShowType.PreviousPage,(double)currentShowPage,(double)myPreference.getListShowRecordNum(),"");
                    break;
                case InitShow:
                default:
                    kanbanValue=WebServiceUtil2.getFinishedGoodsItems(WebServiceUtil2.ShowType.InitShow,(double)currentShowPage,showRecordNum,"");
                    break;
            }
            if(kanbanValue.errorSign||kanbanValue.finishedGoodsItems.size()==0){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FinishedGoodsKanban.this, "看板当前不存在可用数据！", Toast.LENGTH_SHORT).show();
                        TextView tv_page = (TextView) findViewById(R.id.tv_page);
                        tv_page.setText("看板当前不存在可用数据，请等待下次更新！");
                    }
                });
                return false;
            }else {
                finishedGoodsItemList=kanbanValue.finishedGoodsItems;
                kanbanValue=null;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示看板数据，并设置列表高度
                        initListHigh(finishedGoodsItemList);
                        TextView tv_page = (TextView) findViewById(R.id.tv_page);
                        if(currentWorkType== workType.AllShow){
                            tv_page.setText("当前显示全部数据记录！");
                        }else {
                            tv_page.setText("总页数："+String.valueOf((int)finishedgoodsPageInfo.TotalPages)+"，当前第"+String.valueOf(currentShowPage)+"页！");
                        }
                    }
                });
            }
        }catch(Exception ex){
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FinishedGoodsKanban.this, "启动显示数据记录失败", Toast.LENGTH_SHORT).show();
                    TextView tv_page = (TextView) findViewById(R.id.tv_page);
                    tv_page.setText("看板当前不存在可用数据，请等待下次更新！");
                }
            });
            return false;
        }
        return true;
    }

    //显示低于最小库存的记录
    private void initShowLessThanMin(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if(initShowWhereItemList(" Where ActualStock<MinStock")){
                    currentWorkType= workType.WaitShow;
                    currentWaitProcess= WaitProcess.Finished;
                    lastOperateTime=new Date();
                }
            }
        });

        currentWaitProcess= WaitProcess.WaitShowData;
        thread.start();
    }

    //显示高于最大库存的记录
    private void initShowHighThanMax(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if(initShowWhereItemList(" Where ActualStock>MaxStock")){
                    currentWorkType= workType.WaitShow;
                    currentWaitProcess= WaitProcess.Finished;
                    lastOperateTime=new Date();
                }
            }
        });

        currentWaitProcess= WaitProcess.WaitShowData;
        thread.start();
    }

    //显示已完成计划的记录
    private void initShowFinishedDelivery(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if(initShowWhereItemList(" Where WaitProduct=0")){
                    currentWorkType= workType.WaitShow;
                    currentWaitProcess= WaitProcess.Finished;
                    lastOperateTime=new Date();
                }
            }
        });

        currentWaitProcess= WaitProcess.WaitShowData;
        thread.start();
    }

    //显示未完成计划的记录
    private void initShowUnFinishedDelivery(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if(initShowWhereItemList(" Where WaitProduct>0")){
                    currentWorkType= workType.WaitShow;
                    currentWaitProcess= WaitProcess.Finished;
                    lastOperateTime=new Date();
                }
            }
        });

        currentWaitProcess= WaitProcess.WaitShowData;
        thread.start();
    }

    //启动初始化筛选数据显示
    private boolean initShowWhereItemList(String Where){
        try{
            kanbanValue=WebServiceUtil2.getFinishedGoodsItems(WebServiceUtil2.ShowType.Where,0.0,0.0,Where);
            if(kanbanValue.errorSign||kanbanValue.finishedGoodsItems.size()==0){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FinishedGoodsKanban.this, "在指定筛选条件下，看板当前不存在可用数据！", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }else {
                finishedGoodsItemList=kanbanValue.finishedGoodsItems;
                kanbanValue=null;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示看板数据，并设置列表高度
                        initListHigh(finishedGoodsItemList);
                        TextView tv_page = (TextView) findViewById(R.id.tv_page);
                        tv_page.setText("当前显示指定筛选条件下的数据记录！");
                    }
                });
            }
        }catch(Exception ex){
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FinishedGoodsKanban.this, "启动筛选显示数据记录失败", Toast.LENGTH_SHORT).show();
                }
            });
            return false;
        }
        return true;
    }
}
