package com.github.croper.helper;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * user author: didikee
 * create time: 3/20/19 9:38 AM
 * description:  CropUIActivity 这个类的数据存储对象
 */
public class CropBundle implements Parcelable {
    private RectF crop;
    private int aspectX;
    private int aspectY;
    private boolean isFix;

    public CropBundle(RectF crop, int aspectX, int aspectY, boolean isFix) {
        this.crop = crop;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        this.isFix = isFix;
    }

    public RectF getCrop() {
        return crop;
    }

    public int getAspectX() {
        return aspectX;
    }

    public int getAspectY() {
        return aspectY;
    }

    public boolean isFix() {
        return isFix;
    }

    protected CropBundle(Parcel in) {
        crop = in.readParcelable(RectF.class.getClassLoader());
        aspectX = in.readInt();
        aspectY = in.readInt();
        isFix = in.readByte() != 0;
    }

    public static final Creator<CropBundle> CREATOR = new Creator<CropBundle>() {
        @Override
        public CropBundle createFromParcel(Parcel in) {
            return new CropBundle(in);
        }

        @Override
        public CropBundle[] newArray(int size) {
            return new CropBundle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(crop, flags);
        dest.writeInt(aspectX);
        dest.writeInt(aspectY);
        dest.writeByte((byte) (isFix ? 1 : 0));
    }
}
