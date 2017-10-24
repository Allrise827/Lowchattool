package com.tw.flag.lowwechat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CHENGXUYUAN on 2017/5/9.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> mMsgList;

    ImageView sendImage;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout receiveLayout;
        LinearLayout sendLayout;
        TextView  receiveMsg;
        TextView  sendMsg;

        ImageView receiveImage;
        ImageView sendImage;

        public ViewHolder(View view) {
            super(view);
            receiveLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            sendLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            receiveMsg = (TextView) view.findViewById(R.id.left_msg);

            sendMsg = (TextView) view.findViewById(R.id.right_msg);

        }
    }
    public MsgAdapter(List<Msg> msgList){
        mMsgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.msg_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int posiition){
        Msg msg = mMsgList.get(posiition);
        if (msg.getType() == Msg.TYPE_RECEIVE) {
            holder.receiveLayout.setVisibility(View.VISIBLE);
            holder.sendLayout.setVisibility(View.GONE);
            holder.receiveMsg.setText(msg.getContent());
            holder.receiveMsg.setMaxWidth(800);

        } else if (msg.getType() == Msg.YYPE_SEND){
            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.receiveLayout.setVisibility(View.GONE);
            holder.sendMsg.setMaxWidth(800);
            holder.sendMsg.setText(msg.getContent());

        }
    }
    @Override
    public int getItemCount(){

        return mMsgList.size();
    }
}

