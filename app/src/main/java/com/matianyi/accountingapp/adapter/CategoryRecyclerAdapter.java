package com.matianyi.accountingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.bean.CategoryResBean;
import com.matianyi.accountingapp.bean.RecordBean;
import com.matianyi.accountingapp.util.GlobalUtil;

import java.util.LinkedList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    public static final String TAG = "RecyclerAdapter";

    private LayoutInflater mInflater;
    private Context mContext;

    public LinkedList<CategoryResBean> cellList;

    public String getSelected() {
        return selected;
    }

    private String selected="";

    CategoryViewHolder myViewHolder;

    public boolean tag = true;

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }

    private OnCategoryClickListener onCategoryClickListener;

    public CategoryRecyclerAdapter(Context context){
        // GlobalUtil.getInstance().setContext(context);
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

        cellList = GlobalUtil.getInstance().costRes;
        if (cellList.size() > 20){
            cellList.clear();
            GlobalUtil.getInstance().addRes();
            cellList  = GlobalUtil.getInstance().costRes;
        }
        selected = cellList.get(0).title;

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Log.d(TAG, "onCreateViewHolder: " + "create, " + "viewType = " + viewType);
        View view = mInflater.inflate(R.layout.cell_category,parent,false);
        myViewHolder = new CategoryViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        final CategoryResBean res = cellList.get(position);

        holder.imageView.setImageResource(res.resBlack);
        holder.textView.setText(res.title);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = res.title;
                notifyDataSetChanged();

                if (onCategoryClickListener!=null){
                    onCategoryClickListener.onClick(res.title);
                }

            }
        });

        if (holder.textView.getText().toString().equals(selected)){
            holder.background.setBackgroundResource(R.drawable.bg_edit_text);
        }
        else {
            holder.background.setBackgroundResource(R.color.white);
        }

    }

    public void changeType(RecordBean.RecordType type){
        if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
            cellList = GlobalUtil.getInstance().costRes;
            if (cellList.size() > 20){
                cellList.clear();
                GlobalUtil.getInstance().addRes();
                cellList  = GlobalUtil.getInstance().costRes;
            }
        }else if (type == RecordBean.RecordType.RECORD_TYPE_INCOME){
            cellList = GlobalUtil.getInstance().earnRes;
            if (cellList.size() > 7){
                cellList.clear();
                GlobalUtil.getInstance().addRes();
                cellList  = GlobalUtil.getInstance().earnRes;
            }
        }

        selected = cellList.get(0).title;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return cellList.size();
    }

    public interface OnCategoryClickListener{
        void onClick(String category);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CategoryViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder{

    public RelativeLayout background;
    public ImageView imageView;
    public TextView textView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        background = itemView.findViewById(R.id.cell_background);
        imageView = itemView.findViewById(R.id.imageView_category);
        textView = itemView.findViewById(R.id.textView_category);
    }
}
