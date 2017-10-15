package com.kwong.drinknight.share;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kwong.drinknight.R;
import com.kwong.drinknight.ranking_page.RankingActivity;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.kwong.drinknight.utils.Global.SERVER_URL;
import static com.kwong.drinknight.utils.Global.userData;
import static com.kwong.drinknight.utils.Global.volumeDose;

public class ShareImageActivity extends AppCompatActivity {

    //以下5项都是为了生成分享图片的
    private LinearLayout shareView;
    private ImageView portrait;
    private ImageView bg;
    private  TextView rank;
    private TextView vol;

    private boolean flag =true;//保证只分享一次
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_image);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (flag) {
            shareView = (LinearLayout) findViewById(R.id.shared_image);
            System.out.print("GetBitmap " + String.valueOf(shareView.getMeasuredWidth()) + "  " + String.valueOf(shareView.getMeasuredHeight()));
            Log.d("GetBitmap", String.valueOf(shareView.getMeasuredWidth()));
            portrait = (ImageView) findViewById(R.id.shared_portrait);
            bg = (ImageView) findViewById(R.id.shared_backgroud);
            rank = (TextView) findViewById(R.id.shared_rank);
            vol = (TextView) findViewById(R.id.shared_volume);
            try {
                setShareView();  //设计分享图片

            } catch (Exception e) {
                e.printStackTrace();
            }
            flag = false;//不再分享
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(3000);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    showShare();
                }
            }.start();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    private void showShare() {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //手机截图
        //Bitmap btmap = GetBitmap.getViewDrawingCacheBitmap(shareView);
        Bitmap btmap = GetBitmap.getLinearLayoutBitmap(shareView);

        //Bitmap btmap = GetBitmap.getViewBitmap(shareView);
        //Bitmap btmap = GetBitmap.getRecyclerViewBitmap(recyclerView,"/sdcard/");
        String uri = GetBitmap.createShareFile(btmap);
        oks.setImagePath(uri);
        //oks.setImageUrl(SERVER_URL+"/user/"+userData.getAccount()+"/image/");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);

    }

    private void setShareView() {
        String imageUrl = SERVER_URL+"/user/"+userData.getAccount()+"/image/";
        Glide.with(ShareImageActivity.this).load(imageUrl).error(R.drawable.user_0).placeholder(R.mipmap.ic_launcher).into(portrait);
        Glide.with(ShareImageActivity.this).load(imageUrl).error(R.drawable.user_0).placeholder(R.mipmap.ic_launcher).into(bg);
        rank.setText("??");
        vol.setText(String.valueOf(volumeDose));
    }
}
