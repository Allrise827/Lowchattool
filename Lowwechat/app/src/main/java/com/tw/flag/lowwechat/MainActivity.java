package com.tw.flag.lowwechat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import static com.tw.flag.lowwechat.Msg.TYPE_RECEIVE;

public class MainActivity extends AppCompatActivity {

    private Uri imgUri;

    private List<Msg> msgList  = new ArrayList<>();

    private EditText inputText;

    private Button send_text;
    private Button logout_acc;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    private String receiverid;

    private String senderid;

    private Msg msg_send;

    private Msg msg_receive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inputText = (EditText) findViewById(R.id.input_text);
        send_text = (Button) findViewById(R.id.right);

        logout_acc=(Button)findViewById(R.id.logout);

        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);


        Intent intent = getIntent();
        senderid = intent.getStringExtra("extra_data");
        if (senderid.equals("666")){
            receiverid = "777";
        }else {
            receiverid = "666";
        }

        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }


    @Override
     protected void onDestroy(){
         NIMClient.getService(MsgServiceObserve.class)
                 .observeReceiveMessage(incomingMessageObserver, false);
     }

    private Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> messages) {
                    // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                    for (IMMessage message : messages) {
                            String content = message.getContent();
                            msg_receive = new Msg(content,TYPE_RECEIVE);
                            msgList.add(msg_receive);
                            adapter.notifyItemInserted(msgList.size() - 1);
                            msgRecyclerView.scrollToPosition(msgList.size() - 1);


                    }
                }
            };

   /* private Bitmap setImage(IMMessage message){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        String path = ((ImageAttachment)message.getAttachment()).getThumbPath();
        Bitmap bitmap =  BitmapFactory.decodeFile(path,options);
        return bitmap;
    }*/

     public void logout(View v){
         NIMClient.getService(AuthService.class).logout();
         startActivity(new Intent(MainActivity.this,LoginActivity.class));
         finish();
     }

    public void send(View v){

        String content = inputText.getText().toString();
        msg_send = new Msg(content,Msg.YYPE_SEND);
        // 创建文本消息
        IMMessage message = MessageBuilder.createTextMessage(receiverid, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                content // 文本内容
        );

        NIMClient.getService(MsgService.class).sendMessage(message,false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                msgList.add(msg_send);
                adapter.notifyItemInserted(msgList.size() - 1);
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                inputText.setText("");
            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(MainActivity.this,"消息发送失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
       // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。

    }

    /* protected void onActivityResult(int requestCode,int resultCode,Intent data){
        MainActivity.super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == AppCompatActivity.RESULT_OK && requestCode ==100){
            showImg();
        }
    }

   void showImg(){
        int iw,ih,vw,vh;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUri.getPath(),option);

        iw = option.outWidth;
        ih = option.outHeight;
        vw = 175;
        vh = 278;

        int scaleFactor = Math.min(iw/vw , ih/vh);//计算缩小比率

        option.inJustDecodeBounds = false;
        option.inSampleSize = scaleFactor;

        bmp = BitmapFactory.decodeFile(imgUri.getPath() , option);


    }

     public void photo(View v){

         Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
         it.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
         startActivityForResult(it,100);

         String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
         String fname = "p" + System.currentTimeMillis() + ".jpg";
         imgUri = Uri.parse("file://" + dir + "/" + fname);


         msg_image_send = new Msg(null,bmp,Msg.YYPE_SEND,Msg.TYPE_IMAGE);
         File file = new File(" /storage/emulated/0/DCIM/Camera/IMG_20170430_183853.jpg");

         IMMessage message = MessageBuilder.createImageMessage(receiverid,
                 SessionTypeEnum.P2P,
                 file,null);
         NIMClient.getService(MsgService.class).sendMessage(message,false).setCallback(new RequestCallback<Void>() {


             @Override
             public void onSuccess(Void param) {
                 msgList.add(msg_image_send );
                 adapter.notifyItemInserted(msgList.size() - 1);
                 msgRecyclerView.scrollToPosition(msgList.size() - 1);
                 inputText.setText("");
                 Toast.makeText(MainActivity.this,"要啊",Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onFailed(int code) {
                 Toast.makeText(MainActivity.this,"不要啊",Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onException(Throwable exception) {
             }
         });
     }

   /*将从服务器接收到的图片消息转化为Bitmap格式以适应Msg类的消息类型*/

}


