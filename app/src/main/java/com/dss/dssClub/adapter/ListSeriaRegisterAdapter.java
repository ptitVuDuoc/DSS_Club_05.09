package com.dss.dssClub.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dss.dssClub.R;
import com.dss.dssClub.listen.OnLoadMoreListener;
import com.dss.dssClub.model.ListGift;

import java.util.ArrayList;
import java.util.List;

public class ListSeriaRegisterAdapter extends RecyclerView.Adapter<ListSeriaRegisterAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mListSeria = new ArrayList<>();
    private OnStickerListener mItemListener;


    public void setOnItemListGiftListener(OnStickerListener listener) {
        mItemListener = listener;
    }

    public ListSeriaRegisterAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mListSeria = list;


    }

    @Override
    public ListSeriaRegisterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_list_seria, parent, false);
        return new ListSeriaRegisterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String txt = mContext.getString(R.string.code_seria)+": " +mListSeria.get(position);
        holder.txtSeria.setText(txt);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemListener != null){
                    mItemListener.onItemClickDeleteListener(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListSeria == null? 0 : mListSeria.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView delete;
        private TextView txtSeria;

        public ViewHolder(View itemView) {
            super(itemView);

            txtSeria = itemView.findViewById(R.id.txt_seria);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnStickerListener {
        void onItemClickDeleteListener(int position);
    }
    
}
