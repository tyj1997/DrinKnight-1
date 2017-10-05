package com.kwong.drinknight.user_data_page;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kwong.drinknight.R;
import com.kwong.drinknight.login.Register;
import com.kwong.drinknight.utils.UploadImage;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;

import static com.kwong.drinknight.utils.Global.SERVER_URL;

public class UserDataActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView userPortrait;
    private EditText userNameText;
    private EditText userGenderText;
    private EditText userIdText;
    private EditText userAgeText;
    private EditText userHeightText;
    private EditText userWeightText;
    private EditText userPhoneNumberText;
    private EditText userEmailText;
    private EditText userRegisterTimeText;

    public static final int TAKE_PHOTO = 1;
    public static final int FROM_ALBUM =0;

    private Uri imageUri;
    private  String imagePath;

    private String imageUrl =SERVER_URL+"/media/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_data);
        Intent intent = getIntent();
        String[] datas = intent.getStringArrayExtra("user_datas");
        userPortrait = (ImageView) findViewById(R.id.user_portrait);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        userNameText = (EditText) findViewById(R.id.user_name);
        userNameText.setText(datas[0]);
        userIdText = (EditText) findViewById(R.id.user_id);
        userIdText.setText(datas[1]);
        userIdText.setFocusable(false);
        userIdText.setFocusableInTouchMode(false);
        userGenderText = (EditText) findViewById(R.id.user_gender);
        userGenderText.setText(datas[2]);
        userAgeText = (EditText) findViewById(R.id.user_age);
        userAgeText.setText(datas[3]);
        userHeightText = (EditText) findViewById(R.id.user_height);
        userHeightText.setText(datas[4]);
        userWeightText = (EditText) findViewById(R.id.user_weight);
        userWeightText.setText(datas[5]);
        userPhoneNumberText=(EditText) findViewById(R.id.user_phonenumber);
        userPhoneNumberText.setText(datas[6]);
        userEmailText=(EditText) findViewById(R.id.user_email);
        userEmailText.setText(datas[7]);
        userRegisterTimeText=(EditText) findViewById(R.id.user_registerTime);
        userRegisterTimeText.setText(datas[8]);
        userRegisterTimeText.setFocusable(false);
        userRegisterTimeText.setFocusableInTouchMode(false);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_backup);
        }
        String imageName = datas[9];
        imageUrl = imageUrl+imageName;
        Glide.with(UserDataActivity.this).load(imageUrl).error(R.mipmap.ic_launcher).into(userPortrait);
        View imageLayout=findViewById(R.id.user_image_layout);
        imageLayout.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){

            this.finish();  //finish当前activity
            overridePendingTransition(R.anim.in_from_left,
                    R.anim.out_from_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:
                UpdateUserData();
                UpdateUserImage();
                finish();
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    private void UpdateUserImage() {
        if(imagePath != null)
        {
            String[] params = {imagePath,userIdText.getText().toString()};
            new UploadImage().execute(params);
        }

    }


    public void UpdateUserData(){
        String urlPath = SERVER_URL+"/user/"+ userIdText.getText().toString()+"/profile/#";
        URL urls;
        try {
            urls=new URL(urlPath);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userName", userNameText.getText().toString());
            jsonObject.put("account", userIdText.getText().toString());

            jsonObject.put("gender",userGenderText.getText());
            jsonObject.put("emailAddress", userEmailText.getText().toString());
            jsonObject.put("age",Double.parseDouble( userAgeText.getText().toString()));
            jsonObject.put("height", Double.parseDouble(userHeightText.getText().toString()));
            jsonObject.put("phoneNumber", userPhoneNumberText.getText().toString());
            jsonObject.put("weight", Double.parseDouble(userWeightText.getText().toString()));
            OkHttpClient okHttpClient = new OkHttpClient();
            //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
            System.out.println(jsonObject.toString());
            String content = String.valueOf(jsonObject);
            HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("ser-Agent", "Fiddler");
            // 设置contentType

            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(content.getBytes());
            os.flush();
            os.close();
            System.out.println();
            int code = conn.getResponseCode();
            System.out.println(code);
            System.out.println(content);
            System.out.println(conn.getContent().toString());
            if (code == 200) {
                InputStream inputStream = null;
                Toast.makeText(UserDataActivity.this, "修改信息成功", Toast.LENGTH_SHORT).show();
                inputStream = conn.getInputStream();
                Intent intent=new Intent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                content = builder.toString();
                intent.putExtra("json_return",content);
                setResult(RESULT_OK,intent);

            }
            else
            {
                Toast.makeText(UserDataActivity.this, "修改信息失败", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }

    }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.user_image_layout:
                    new AlertDialog.Builder(UserDataActivity.this)
                            .setTitle("请选择")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setSingleChoiceItems(new String[] {"相册","拍照"}, 0,
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if(which == FROM_ALBUM)
                                            {
                                                if (ContextCompat.checkSelfPermission(UserDataActivity.this,
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                                                    ActivityCompat.requestPermissions(UserDataActivity.this,
                                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                                }else {
                                                    openAlbum();
                                                }
                                            }else{
                                                //创建File对象，用于储存拍照后的图片
                                                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                                                try{
                                                    if(outputImage.exists()){
                                                        outputImage.delete();
                                                    }
                                                    outputImage.createNewFile();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (Build.VERSION.SDK_INT >= 24){
                                                    imageUri = FileProvider.getUriForFile(UserDataActivity.this,"com.kwong.uploadfiletest.fileprovider",outputImage);
                                                }else{
                                                    imageUri = Uri.fromFile(outputImage);
                                                }
                                                //启动相机
                                                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                                                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                                startActivityForResult(intent1, TAKE_PHOTO);
                                            }
                                            Log.d("ChoosePhoto","end");
                                            //finish();
                                        }
                                    }

                            ).setNegativeButton("取消", null)
                            .show();
                    break;

                default:

            }

    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, FROM_ALBUM);//打开相册
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try{
                        //将拍摄的照片显示出来
                        Log.d("ChoosePhoto","show image start");
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        userPortrait.setImageBitmap(bitmap);
                        Log.d("ChoosePhoto","show image end");
                        //// FIXME: 2017/10/5 发送拍照所得照片到服务器
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case FROM_ALBUM:
                if (resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >=19){
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else {
                        //4.4以下系统使用这个方法处理
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        Log.d("ChoosePhoto","handleImageOnKitKat");
        imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID +"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data){
        Log.d("ChoosePhoto","handleImageBeforeKitKat");
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    private void displayImage(String imagePath) {
        if (imagePath != null){
            Log.d("ChoosePhoto","displayImage");
            Bitmap  bitmap = BitmapFactory.decodeFile(imagePath);
            userPortrait.setImageBitmap(bitmap);
            Log.d("ChoosePhoto","displayImage end");
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }



    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection获取真实的图片地址
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}