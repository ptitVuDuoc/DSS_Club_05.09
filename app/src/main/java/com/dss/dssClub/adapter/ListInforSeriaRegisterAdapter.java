package com.dss.dssClub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dss.dssClub.R;
import com.dss.dssClub.model.ListResultSerium;

import java.util.ArrayList;
import java.util.List;

public class ListInforSeriaRegisterAdapter extends RecyclerView.Adapter<ListInforSeriaRegisterAdapter.ViewHolder>{

    private Context mContext;
    private List<ListResultSerium> mListSeria = new ArrayList<>();
    private OnStickerListener mItemListener;


    public void setOnItemListGiftListener(OnStickerListener listener) {
        mItemListener = listener;
    }

    public ListInforSeriaRegisterAdapter(Context context, List<ListResultSerium> list) {
        this.mContext = context;
        this.mListSeria = list;


    }

    @Override
    public ListInforSeriaRegisterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_infor_seria, parent, false);
        return new ListInforSeriaRegisterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String txt = mContext.getString(R.string.code_seria)+": " +mListSeria.get(position).getSeria();
        holder.txtSeria.setText(txt);
        if(mListSeria.get(position).getIsSuccess()){
            holder.imgStatus.setImageResource(R.drawable.ic_success);
            holder.txtStatus.setText(mContext.getString(R.string.success_infor_regiter_seria));
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_bg_btn_history));
        }else {
            holder.imgStatus.setImageResource(R.drawable.ic_error);
            holder.txtStatus.setText(mContext.getString(R.string.error_infor_regiter_seria));
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_select_tab));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemListener != null){
                    mItemListener.onItemClickListener(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListSeria == null? 0 : mListSeria.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgStatus;
        private TextView txtSeria,txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSeria = itemView.findViewById(R.id.txt_seria);
            imgStatus = itemView.findViewById(R.id.ic_status);
            txtStatus = itemView.findViewById(R.id.txt_status);
        }
    }

    public interface OnStickerListener {
        void onItemClickListener(int position);
    }
    
}
