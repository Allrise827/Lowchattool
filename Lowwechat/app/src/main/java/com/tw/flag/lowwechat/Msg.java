package com.tw.flag.lowwechat;

/**
 * Created by CHENGXUYUAN on 2017/5/9.
 */

public class Msg {

    public static final int TYPE_RECEIVE = 0;
    public static final int YYPE_SEND = 1;
    private String content;
    private int type;


    public Msg(String content,int type){
        this.content = content;
        this.type = type;

    }

    public String getContent(){

        return content;
    }

    public int getType(){

        return type;
    }



}
