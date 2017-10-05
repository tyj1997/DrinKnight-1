package com.kwong.drinknight.utils;
//没用到，只是一个类似模板的东西
import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.kwong.drinknight.R;
import com.kwong.drinknight.home_page.MainActivity;
import com.kwong.drinknight.login.Register;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChoosePhotoActivity extends AppCompatActivity {

    public int METHOD;
    public static final int TAKE_PHOTO = 1;
    public static final int FROM_ALBUM =0;

    private ImageView userImage;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userImage= (ImageView) LayoutInflater.from(ChoosePhotoActivity.this).inflate(R.layout.register, null).findViewById(R.id.user_image);
        Intent intent = getIntent();
        METHOD = intent.getIntExtra("method",0);
        if(METHOD == FROM_ALBUM)
        {
            if (ContextCompat.checkSelfPermission(ChoosePhotoActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ChoosePhotoActivity.this,
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
                imageUri = FileProvider.getUriForFile(ChoosePhotoActivity.this,"com.kwong.uploadfiletest.fileprovider",outputImage);
            }else{
                imageUri = Uri.fromFile(outputImage);
            }
            //启动相机
            Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }
        Log.d("ChoosePhoto","end");
        //finish();
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
                        userImage.setImageBitmap(bitmap);
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
        String imagePath = null;
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
            userImage.setImageBitmap(bitmap);
            Log.d("ChoosePhoto","displayImage end");
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }

        new UploadImage().execute(imagePath);
        Log.d("ChoosePhoto","toUpload");

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
