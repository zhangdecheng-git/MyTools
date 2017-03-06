package tools.zhang.com.mytools.camera;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import tools.zhang.com.mytools.R;
import tools.zhang.com.mytools.utils.ToastUtil;

/**
 * Created by zhangdecheng on 2016/11/26.
 */
public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";

    public static final int REQUEST_CODE_TAKE_PHOTO = 111;
    public static final int REQUEST_CODE_SELECT_PIC = 112;
    public static final int REQUEST_CODE_CROP_PIC = 113;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 114;


    private final File mFileHeadshotPath = new File(Environment.getExternalStorageDirectory().getPath() + "/360Download/avatar/crop");
    private File mFileHeadshot;
    private final File mFileHeadCameraPath = new File(Environment.getExternalStorageDirectory().getPath() + "/360Download/avatar");
    private File mFileHeadCameraPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        findViewById(R.id.take_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(CameraActivity.this, "cammae", 1);
                checkCameraPermission();
            }
        });

        findViewById(R.id.select_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPic();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            if (granted) {
                takePhoto();
            }
            ToastUtil.show(CameraActivity.this,  "onRequestPermissionsResult granted=" + granted, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO: // 如果是调用相机拍照时
            case REQUEST_CODE_SELECT_PIC: // 如果是直接从相册获取
            case REQUEST_CODE_CROP_PIC: // 取得裁剪后的图片
                handleOnActivityResult(requestCode, resultCode, data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TAKE_PHOTO: // 如果是调用相机拍照时
                    if (mFileHeadCameraPic != null) {
                        startPhotoZoom(Uri.fromFile(mFileHeadCameraPic));
                    }
                    break;

                case REQUEST_CODE_SELECT_PIC: // 如果是直接从相册获取
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
                    break;

                case REQUEST_CODE_CROP_PIC: // 取得裁剪后的图片
                    deleteHeader();
                    String fileName = null;
                    if (data != null) {
                        fileName = setPicToView(data);
                    }
                    if (fileName != null) {
//                        postAvatarPicToServer(fileName);
                        ToastUtil.show(CameraActivity.this, "fielname:"+fileName, 1);
                    }
                    break;

                default:
                    break;
            }
        } else {
            switch (requestCode) {
                case REQUEST_CODE_CROP_PIC:
                    deleteHeader();
                    break;

                default:
                    break;
            }
        }
    }

    private void checkCameraPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);
            } else {
                takePhoto();
            }
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        if (!isExternalMediaMounted()) {
            ToastUtil.show(this, "no sdcard", Toast.LENGTH_LONG);
            return;
        }

        try {
            mFileHeadCameraPic = new File(mFileHeadCameraPath, "360head_from_carema_" + System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File path = mFileHeadCameraPic.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileHeadCameraPic));
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        } catch (Exception e) {
        }
    }

    private boolean isExternalMediaMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    private void selectPic() {
        if (!isExternalMediaMounted()) {
            ToastUtil.show(this, "no sdcard", Toast.LENGTH_LONG);
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
        } catch (ActivityNotFoundException e) {
            ToastUtil.show(this, "对不起，您的手机没有安装图库", Toast.LENGTH_SHORT);
        } catch (Exception e) {
        }
    }

    private void startPhotoZoom(Uri uri) {
        try {
            ensureHeadshotFile();

            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 150);
            intent.putExtra("outputY", 150);
            if (!shouldSaveCroppedImageToFile()) {
                intent.putExtra("return-data", true);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileHeadshot));
            startActivityForResult(intent, REQUEST_CODE_CROP_PIC);
        } catch (ActivityNotFoundException e) {
            ToastUtil.show(this, "对不起，您的手机没有安装图库", Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ToastUtil.show(this, "error:" + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    private void ensureHeadshotFile() {
        try {
            if (!mFileHeadshotPath.exists()) {
                mFileHeadshotPath.mkdirs();
            } else {
                File[] files = mFileHeadshotPath.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        f.delete();
                    }
                }
            }
            mFileHeadshot = new File(mFileHeadshotPath, "avatar_" + System.currentTimeMillis() + ".jpg");
        } catch (Exception e) {
        }
    }

    /**
     * 如果返回<code>true</code>， 则crop image保存到sd卡（以避免intent传值40k大小限制的问题）；如果<code>false</code>，则直接返回bitmap.
     */
    private boolean shouldSaveCroppedImageToFile() {
        return !Build.MODEL.toLowerCase().startsWith("me860");//摩托罗拉ME860不支持保存到SD卡
    }

    private void deleteHeader() {
        if (mFileHeadCameraPic != null && mFileHeadCameraPic.exists() && mFileHeadCameraPic.isFile()) {
            mFileHeadCameraPic.delete();
        }
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private String setPicToView(Intent picdata) {
        if (mFileHeadshot == null) {
            return "";
        }
        String res = null;
        if (shouldSaveCroppedImageToFile()) {
            if (mFileHeadshot.exists()) {
                res = mFileHeadshot.getAbsolutePath();
            }

        } else {
            Bundle extras = picdata.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mFileHeadshot);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    res = mFileHeadshot.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "onActivityResult REQUEST_ID_CROP_IMAGE error ", e);
                    return null;
                } finally {
                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
        return res;
    }
}
