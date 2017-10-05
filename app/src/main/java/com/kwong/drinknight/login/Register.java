package com.kwong.drinknight.login;

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
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kwong.drinknight.R;

import com.kwong.drinknight.utils.UploadImage;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;

import static com.kwong.drinknight.utils.Global.SERVER_URL;

/**
 * Created by Administrator on 2017/9/8.
 */

public class Register extends AppCompatActivity {

    private TextView UserName;
    private TextView UserAccount;
    private TextView UserPassword;
    private TextView UserPassword2;
    private TextView UserPhonenumber;
    private TextView UserEamil;
    private TextView UserHeight;
    private TextView UserWeight;
    private TextView UserAge;
    private Spinner UserGender;

    public static final int TAKE_PHOTO = 1;
    public static final int FROM_ALBUM =0;

    private ImageView UserImage;
    private Uri imageUri;
    private static String imagePath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        UserImage = (ImageView)findViewById(R.id.user_image);
        UserName=(TextView)findViewById(R.id.user_name);
        UserAccount=(TextView)findViewById(R.id.user_account);
        UserPassword=(TextView)findViewById(R.id.user_password);

        UserPassword2=(TextView)findViewById(R.id.user_password2);
        UserPhonenumber=(TextView)findViewById(R.id.user_phonenumber);
        UserEamil=(TextView)findViewById(R.id.user_email);
        UserHeight=(TextView)findViewById(R.id.user_height);
        UserWeight=(TextView)findViewById(R.id.user_weight);
        UserAge=(TextView)findViewById(R.id.user_age);
        UserGender=(Spinner) findViewById(R.id.user_gender);
        final Button Registers = (Button) findViewById(R.id.sign_in_button);
        Registers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(UserAccount.getText());
                if(UserName.getText().toString().equals("")||UserAccount.getText().toString().equals("")||UserPassword.getText().toString().equals("")
                        ||UserPassword2.getText().toString().equals("") ||UserPhonenumber.getText().toString().equals("")||UserEamil.getText().toString().equals("")
                        ||UserHeight.getText().toString().equals("")|| UserWeight.getText().toString().equals("")||UserAge.getText().toString().equals("")||UserGender.getSelectedItem().toString().equals(""))
                    Toast.makeText(Register.this,"请完整填写信息",Toast.LENGTH_SHORT).show();
                else if(!(UserPassword.getText().toString().equals(UserPassword2.getText().toString())))
                    Toast.makeText(Register.this,"两次输入密码请保持一致",Toast.LENGTH_SHORT).show();
                else {
                    SimpleDateFormat formatter  =  new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
                    Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
                    String time  =  formatter.format(curDate);

                    String urlPath = SERVER_URL+"/register/";
                    URL urls;
                    try {
                         urls=new URL(urlPath);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userName", UserName.getText().toString());
                        jsonObject.put("account", UserAccount.getText().toString());
                        if (UserGender.getSelectedItem().toString().equals("男"))
                            jsonObject.put("gender","man");
                        if (UserGender.getSelectedItem().toString().equals("女"))
                            jsonObject.put("gender","woman");
                        jsonObject.put("emailAddress", UserEamil.getText().toString());
                        jsonObject.put("age",Double.parseDouble( UserAge.getText().toString()));
                        jsonObject.put("password", UserPassword.getText().toString());
                        jsonObject.put("height", Double.parseDouble(UserHeight.getText().toString()));
                        jsonObject.put("phoneNumber", UserPhonenumber.getText().toString());
                        jsonObject.put("weight", Double.parseDouble(UserWeight.getText().toString()));
                        OkHttpClient okHttpClient = new OkHttpClient();
                        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)


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
                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Register.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }
                    //上传图片
                    String[] params = {imagePath,UserAccount.getText().toString()};
                    new UploadImage().execute(params);
                    Log.d("ChoosePhoto","toUpload");
                    finish();
                }
            }
        });

        View imageLayout=(View)findViewById(R.id.layout_image);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Register.this)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[] {"相册","拍照"}, 0,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if(which == FROM_ALBUM)
                                        {
                                            if (ContextCompat.checkSelfPermission(Register.this,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                                                ActivityCompat.requestPermissions(Register.this,
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
                                                imageUri = FileProvider.getUriForFile(Register.this,"com.kwong.uploadfiletest.fileprovider",outputImage);
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
            }
        });
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
                        UserImage.setImageBitmap(bitmap);
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
            UserImage.setImageBitmap(bitmap);
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

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.sign_in_button:
//                if(UserName.getText()==null||UserAccount.getText()==null||UserPassword.getText()==null||UserPassword2.getText()==null
//                        ||UserPhonenumber.getText()==null ||UserEamil.getText()==null||UserHeight.getText()==null||
//                        UserWeight.getText()==null||UserAge.getText()==null||UserGender.getSelectedItem()==null)
//                    Toast.makeText(Register.this,"请完整填写信息",Toast.LENGTH_SHORT).show();
//                else if(UserPassword.getText().toString()!=UserPassword.getText().toString())
//                    Toast.makeText(Register.this,"两次输入密码请保持一致",Toast.LENGTH_SHORT).show();
//                else {
//                    SimpleDateFormat formatter  =  new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
//                    Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
//                    String time  =  formatter.format(curDate);
//                    String urlPath = "http://10.206.13.81:8089/register/"+ UserAccount.getText().toString();
//                    URL urls;
//                    try {
//                        urls = new URL(urlPath);
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("userName", UserName.getText().toString());
//                        jsonObject.put("account", UserAccount.getText().toString());
//                        jsonObject.put("phoneNumber", UserPhonenumber.getText().toString());
//                        jsonObject.put("weight", UserWeight.getText().toString());
//                        jsonObject.put("gender",UserGender.getSelectedItem().toString());
//                        jsonObject.put("emailAddress", UserEamil.getText().toString());
//                        jsonObject.put("age", UserAge.getText().toString());
//                        jsonObject.put("password", UserPassword.getText().toString());
//                        jsonObject.put("registerTime", time);
//                        jsonObject.put("height", UserHeight.getText().toString());
//                        String content = String.valueOf(jsonObject);
//                        HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
//                        conn.setConnectTimeout(5000);
//                        conn.setDoOutput(true);
//                        conn.setRequestMethod("POST");
//                        conn.setRequestProperty("ser-Agent", "Fiddler");
//                        // 设置contentType
//                        conn.setRequestProperty("Content-Type", "application/json");
//                        OutputStream os = conn.getOutputStream();
//                        os.write(content.getBytes());
//                        os.close();
//                        int code = conn.getResponseCode();
//                        if (code == 200) {
//                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    finish();
//                }
//                break;
//            default:
//                break;
//        }
//    }
}