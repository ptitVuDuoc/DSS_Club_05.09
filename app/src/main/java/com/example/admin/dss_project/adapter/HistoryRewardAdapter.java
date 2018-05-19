package com.example.admin.dss_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.admin.dss_project.R;

import java.util.ArrayList;

public class HistoryRewardAdapter extends RecyclerView.Adapter<HistoryRewardAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListHistory = new ArrayList<>();
    private OnStickerListener mStickerListener;

    public HistoryRewardAdapter setOnStickerListener(OnStickerListener listener) {
        mStickerListener = listener;
        return this;
    }

    public HistoryRewardAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mListHistory = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_list_register_seria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return mListHistory == null? 0 : mListHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageSticker;
        private RelativeLayout mBtnIcon;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnStickerListener {
        void onItemStickerClickListener(int position);
    }
}
