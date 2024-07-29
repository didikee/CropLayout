package com.didikee.croplayout.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.croper.helper.CropBundle;
import com.github.croper.helper.CropDelegate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * user author: didikee
 * create time: 3/18/19 12:01 PM
 * description: 针对图片，gif，视频的裁剪
 *
 * 满足一下几点需求：
 * 1. 能显示裁剪的区域
 * 2. 知道区域，预处理显示
 * 3. 得到显示区域
 *
 * 注意：此UI类仅仅是显示出哪里需要裁剪，只会给出需要裁剪的矩形
 *
 */
public class CropUIActivity extends AppCompatActivity {
    private static final String PATH = "path";
    public static final int CROP_REQUEST = 4000;
    public static final String CROP = "CropBundle";
    private Uri mediaUri;

    private CropDelegate mCropDelegate;

    public static void start(Activity activity, Uri mediaUri) {
        Intent intent = new Intent(activity, CropUIActivity.class);
        intent.putExtra(PATH, mediaUri);
        activity.startActivityForResult(intent, CROP_REQUEST);
    }

    public static void start(Activity activity, Uri mediaUri, @Nullable CropBundle cropBundle) {
        Intent intent = new Intent(activity, CropUIActivity.class);
        intent.putExtra(PATH, mediaUri);
        intent.putExtra(CROP, cropBundle);
        activity.startActivityForResult(intent, CROP_REQUEST);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mediaUri = getIntent().getParcelableExtra(PATH);
        }
        if (mediaUri == null) {
            Toast.makeText(this, "exception_invalid_media_path", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setContentView(R.layout.media_activity_crop_ui);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("");

        FrameLayout mMediaContainer = findViewById(R.id.container);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        mCropDelegate = new CropDelegate(this);
        mCropDelegate.bindView(mMediaContainer, recyclerView);
        mCropDelegate.setOnCropChangeListener(new CropDelegate.OnCropChangeListener() {
            @Override
            public void onMediaInit(int width, int height) {

            }

            @Override
            public void onCropChange(float cropWidth, float cropHeight) {

            }
        });
        mCropDelegate.setData(mediaUri);

        // 是否恢复状态
        CropBundle cropBundle = getIntent().getParcelableExtra(CROP);
        mCropDelegate.setRestoreCropData(cropBundle);

        mCropDelegate.start();
    }


    @Override
    public void onPause() {
        super.onPause();
        mCropDelegate.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCropDelegate.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveAndExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存并退出
     */
    private void saveAndExit() {
        RectF cropLocation = mCropDelegate.getCropLocation();
        if (cropLocation != null) {
            int aspectRatioX = 0;
            int aspectRatioY = 0;
            boolean fixAspectRatio = mCropDelegate.isFixAspectRatio();
            if (fixAspectRatio) {
                aspectRatioX = mCropDelegate.getAspectRatioX();
                aspectRatioY = mCropDelegate.getAspectRatioY();
            }
            Intent data = new Intent();
            CropBundle cropBundle = new CropBundle(cropLocation, aspectRatioX, aspectRatioY, fixAspectRatio);
            data.putExtra(CROP, cropBundle);
            setResult(RESULT_OK, data);
        }
        finish();
    }
}
