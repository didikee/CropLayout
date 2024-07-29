package com.github.croper.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidx.StorageUriUtils;
import com.androidx.UriUtils;
import com.androidx.media.MediaUriInfo;
import com.github.croper.CropLayoutView;
import com.github.croper.OnCropParamsChangeListener;
import com.github.croper.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.net.UriCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifDrawable;

/**
 * user author: didikee
 * create time: 2019-08-05 11:57
 * description: 
 */
public class CropDelegate {
    private static final String TAG = "CropDelegate";
    private FrameLayout mMediaContainer;
    private CropLayoutView mCropLayoutView;
    private RecyclerView mRecyclerView;
    private VideoView mVideoView;
    private Uri mGifUri = null;
    private Activity mActivity;
    private int mWidth, mHeight;

    private int lastPosition = -1;
    private CropBundle mCropBundle;
    private OnCropChangeListener onCropChangeListener;
    private AspectRatioAdapter aspectRatioAdapter;


    public CropDelegate(Activity activity) {
        mActivity = activity;
    }

    public void bindView(@NonNull FrameLayout mediaContainer, @Nullable RecyclerView recyclerView) {
        this.mMediaContainer = mediaContainer;
        this.mRecyclerView = recyclerView;
    }

    public void setData(Uri uri) {
        this.mGifUri = uri;
    }

    public void setRestoreCropData(CropBundle cropData) {
        this.mCropBundle = cropData;
    }

    public void setOnCropChangeListener(OnCropChangeListener onCropChangeListener) {
        this.onCropChangeListener = onCropChangeListener;
    }

    public void start() {
        mMediaContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mMediaContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Context context = mActivity;
                RectF src = handleViewWithLoopPlay();
                if (src == null) {
                    Toast.makeText(context, R.string.exception_unknown_error, Toast.LENGTH_SHORT).show();
                    mActivity.finish();
                    Log.d(TAG, "矩形内容为空");
                    return;
                }
                Log.d(TAG, "矩形: " + src.toString());
                int width = mMediaContainer.getWidth();
                int height = mMediaContainer.getHeight();

                RectF dst = new RectF(0, 0, width, height);
                Log.d(TAG, "容器: " + dst.toString());
                RectF displayRectF = UiUtil.setRectToRectCenterFit(src, dst);
                Log.d(TAG, "显示: " + displayRectF.toString());

                int color = ContextCompat.getColor(context, R.color.colorAccent);
                mCropLayoutView = new CropLayoutView(context);
                mCropLayoutView.setGuidelines(CropLayoutView.GUIDELINES_ON_TOUCH);
                mCropLayoutView.setBorderColor(color);
                mCropLayoutView.setGuideLineColor(color);
                mCropLayoutView.setCornerColor(color);
                mCropLayoutView.setOnCropParamsChangeListener(new OnCropParamsChangeListener() {
                    @Override
                    public void onCropChange(float left, float top, float right, float bottom) {
                        if (mWidth > 0 && mHeight > 0) {
                            float cropWidth = (right - left) * mWidth;
                            float cropHeight = (bottom - top) * mHeight;
                            if (onCropChangeListener != null) {
                                onCropChangeListener.onCropChange(cropWidth, cropHeight);
                            }
                        }
                    }
                });
                FrameLayout.LayoutParams cropParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                cropParams.gravity = Gravity.CENTER;
                cropParams.setMargins((int) displayRectF.left, (int) displayRectF.top, (int) (width - displayRectF.right), (int) (height - displayRectF.bottom));
                mMediaContainer.addView(mCropLayoutView, cropParams);

                // 恢复数据
                if (mCropBundle != null && mRecyclerView != null && aspectRatioAdapter != null) {
                    // find location
                    final int aspectX = mCropBundle.getAspectX();
                    final int aspectY = mCropBundle.getAspectY();
                    List<Pair<Integer, Integer>> data = aspectRatioAdapter.getData();
                    if (data == null || data.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < data.size(); i++) {
                        Pair<Integer, Integer> pair = data.get(i);
                        if (pair.first == aspectX && pair.second == aspectY) {
                            final int index = i;
                            mCropLayoutView.post(new Runnable() {
                                @Override
                                public void run() {
                                    aspectRatioAdapter.setSelectedPosition(index);
                                    aspectRatioAdapter.notifyDataSetChanged();
                                    mRecyclerView.smoothScrollToPosition(index);

                                    // 必须等view的大小确定了才可以
                                    mCropLayoutView.recovery(mCropBundle.getCrop(), aspectX, aspectY, mCropBundle.isFix());
                                }
                            });
                            return;
                        }
                    }
                }
            }
        });

        // init recyclerview
        if (mRecyclerView != null) {
            aspectRatioAdapter = new AspectRatioAdapter();
            aspectRatioAdapter.setOnAspectRatioChangedListener(onAspectRatioChangedListener);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.HORIZONTAL, false));
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(aspectRatioAdapter);
        }
    }


    /**
     * 初始化媒体的播放
     * 然后返回媒体在布局中的位置
     *
     * 一般都会在布局中居中
     */
    private RectF handleViewWithLoopPlay() {
        Context context = mActivity;
        RectF showRectF = new RectF();
        // TODO 优化，从文件头中读取文件格式
        MediaUriInfo baseInfo = UriUtils.getBaseInfo(mActivity.getContentResolver(), mGifUri);
        String mimeType = "";
        if (baseInfo != null) {
            mimeType = baseInfo.getMimeType();
        }
        if (mimeType.endsWith("gif")) {
            //gif
            try {
                GifDrawable gifDrawable = new GifDrawable(mActivity.getContentResolver(), mGifUri);
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageDrawable(gifDrawable);
                mMediaContainer.addView(imageView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                mWidth = gifDrawable.getIntrinsicWidth();
                mHeight = gifDrawable.getIntrinsicHeight();
                if (onCropChangeListener != null) {
                    onCropChangeListener.onMediaInit(mWidth, mHeight);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (mimeType.endsWith("mp4")) {

            mVideoView = new VideoView(context);
            mVideoView.setVideoURI(mGifUri);
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.start();
                }
            });
            FrameLayout.LayoutParams videoParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            videoParams.gravity = Gravity.CENTER;
            mMediaContainer.addView(mVideoView, videoParams);

            VideoMetaData videoMetaData = MediaMetadataHelper.getVideoMetaData(context, mGifUri);
            if (videoMetaData != null) {
                int[] realSize = videoMetaData.getRealSize();
                mWidth = realSize[0];
                mHeight = realSize[1];
            }
        } else {
            // NOT Support
        }
        if (mWidth <= 0 || mHeight <= 0) {
            return null;
        }
        showRectF.set(0, 0, mWidth, mHeight);
        return showRectF;
    }

    private AspectRatioAdapter.OnAspectRatioChangedListener onAspectRatioChangedListener = new AspectRatioAdapter.OnAspectRatioChangedListener() {
        @Override
        public void onAspectRatioChanged(int x, int y) {
            setAspectRatio(x, y);
        }
    };

    /**
     * 设置比例
     * @param x 大于等于0
     * @param y 大于等于0
     */
    public void setAspectRatio(int x, int y) {
        if (x <= 0 && y <= 0) {
            mCropLayoutView.setFixedAspectRatio(false);
        } else {
            mCropLayoutView.setFixedAspectRatio(true);
            mCropLayoutView.setAspectRatio(x, y);
        }
    }

    public boolean isFixAspectRatio() {
        return mCropLayoutView.isFixAspectRatio();
    }

    public int getAspectRatioX() {
        return mCropLayoutView.getAspectRatioX();
    }

    public int getAspectRatioY() {
        return mCropLayoutView.getAspectRatioY();
    }

    public RectF getCropLocation() {
        return mCropLayoutView.getCropRectF();
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public Uri getUri() {
        return mGifUri;
    }

    public void onPause() {
        if (mVideoView != null) {
            lastPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
    }

    public void onResume() {
        if (mVideoView != null && lastPosition >= 0) {
            mVideoView.seekTo(lastPosition);
            mVideoView.start();
        }
    }

    public void onDestroy() {

    }

    public interface OnCropChangeListener {
        void onMediaInit(int width, int height);

        void onCropChange(float cropWidth, float cropHeight);
    }

}
