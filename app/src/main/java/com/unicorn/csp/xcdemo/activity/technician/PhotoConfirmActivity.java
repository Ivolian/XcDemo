package com.unicorn.csp.xcdemo.activity.technician;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.utils.ImageUtils;

import butterknife.Bind;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;


//@P
public class PhotoConfirmActivity extends ToolbarActivity {

    // ================================== fields ==================================

    final int CONFIRM_PHOTO_RESULT_CODE = 1234;


    // ========================== extra ==========================

    @InjectExtra("photoPath")
    String photoPath;


    // ========================== view ==========================

    @Bind(R.id.iv_photo)
    ImageView ivPhoto;

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;


    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_confirm);
        initToolbar("拍照确认", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {
        initIvPhoto();
        initEtDescription();
    }

    private void initIvPhoto() {
        ivPhoto.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        BitmapFactory.Options options = ImageUtils.getFactoryOptions(ivPhoto.getWidth(), ivPhoto.getHeight(), photoPath);
                        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
                        ivPhoto.setImageBitmap(bitmap);
                        new PhotoViewAttacher(ivPhoto);
                        ivPhoto.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void initEtDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ========================== onClick ==========================

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        // todo upload photo
        Intent intent = new Intent();
        intent.putExtra("photoTempFileName", "");
        intent.putExtra("description", etDescription.getText().toString().trim());
        setResult(CONFIRM_PHOTO_RESULT_CODE, intent);
        finish();
    }

}
