package com.github.croper.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.croper.AspectRatioView;
import com.github.croper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * user author: didikee
 * create time: 2019-07-30 11:36
 * description: 
 */
public class AspectRatioAdapter extends RecyclerView.Adapter<AspectRatioAdapter.ViewHolder> {
    // 默认第一个选中
    private int mSelectedPosition = 0;
    private List<Pair<Integer, Integer>> data;
    private Context context;
    private OnAspectRatioChangedListener aspectRatioChangedListener;

    public AspectRatioAdapter(List<Pair<Integer, Integer>> data) {
        this.data = data;
    }

    public AspectRatioAdapter() {
        this(getAspectRatios());
    }

    public List<Pair<Integer, Integer>> getData() {
        return data;
    }

    public void setOnAspectRatioChangedListener(OnAspectRatioChangedListener aspectRatioChangedListener) {
        this.aspectRatioChangedListener = aspectRatioChangedListener;
    }

    public void setSelectedPosition(int position) {
        if (position >= 0 && position < getItemCount()) {
            mSelectedPosition = position;
        }
    }

    @NonNull
    @Override
    public AspectRatioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.tool_adapter_crop_aspect_item, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final AspectRatioAdapter.ViewHolder viewHolder, int i) {
        final Pair<Integer, Integer> pair = data.get(i);
        int iconColor = mSelectedPosition == i ? ContextCompat.getColor(context, R.color.colorAccent) : Color.LTGRAY;
        if (pair.first <= 0 && pair.second <= 0) {
            viewHolder.aspectRatioView.setColor(iconColor);
            viewHolder.aspectRatioView.setAspectRatio(pair.first, pair.second);
            viewHolder.textView.setTextColor(iconColor);
            viewHolder.textView.setText("自由");
        } else {
            viewHolder.aspectRatioView.setColor(iconColor);
            viewHolder.aspectRatioView.setAspectRatio(pair.first, pair.second);
            viewHolder.textView.setTextColor(iconColor);
            viewHolder.textView.setText(String.format(Locale.getDefault(), "%s:%s", pair.first, pair.second));
        }
        viewHolder.aspectRatioView.invalidate();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition == mSelectedPosition) {
                    //do nothing
                } else {
                    int old = mSelectedPosition;
                    mSelectedPosition = adapterPosition;
                    notifyItemChanged(old);
                    notifyItemChanged(mSelectedPosition);
                    if (aspectRatioChangedListener != null) {
                        aspectRatioChangedListener.onAspectRatioChanged(pair.first, pair.second);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AspectRatioView aspectRatioView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            aspectRatioView = itemView.findViewById(R.id.aspect);
            textView = itemView.findViewById(R.id.text);
        }
    }

    public interface OnAspectRatioChangedListener {
        void onAspectRatioChanged(int x, int y);
    }

    /**
     * 获取裁剪时的常用裁剪比例
     * @return
     */
    public static ArrayList<Pair<Integer, Integer>> getAspectRatios() {
        ArrayList<Pair<Integer, Integer>> aspectRatios = new ArrayList<>();
        aspectRatios.add(new Pair<Integer, Integer>(0, 0));
        aspectRatios.add(new Pair<Integer, Integer>(1, 1));
        aspectRatios.add(new Pair<Integer, Integer>(16, 9));
        aspectRatios.add(new Pair<Integer, Integer>(4, 3));
        aspectRatios.add(new Pair<Integer, Integer>(3, 2));
        aspectRatios.add(new Pair<Integer, Integer>(5, 4));
        aspectRatios.add(new Pair<Integer, Integer>(7, 5));
        return aspectRatios;
    }
}
