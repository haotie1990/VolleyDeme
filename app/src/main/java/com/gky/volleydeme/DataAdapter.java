package com.gky.volleydeme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 凯阳 on 2016/6/20.
 */
public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements View.OnClickListener{

    private static final int VIEW_TYPE_IMG = 0;
    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_ITEM = 2;

    private List<ItemResponseData> mDatas;

    private LayoutInflater mInflater;

    private Context mContext;

    public DataAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<ItemResponseData> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_IMG){
            View view = mInflater.inflate(R.layout.item_image_layout, parent, false);
            return new ImageHolder(view);
        }else if(viewType == VIEW_TYPE_TITLE){
            View view = mInflater.inflate(R.layout.item_title_layout, parent, false);
            return new TitleHolder(view);
        }else if(viewType == VIEW_TYPE_ITEM){
            View view = mInflater.inflate(R.layout.item_content_layout, parent, false);
            view.setOnClickListener(this);
            return new ItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ItemResponseData itemData = mDatas.get(position);
        if(viewType == VIEW_TYPE_IMG){
            String url = itemData.getUrl();
            ImageHolder imageHolder = (ImageHolder) holder;
            ImageLoader.ImageListener listener = NetUtils.getInstance().getImageLoader()
                .getImageListener(imageHolder.getImageView(),0,0);
            NetUtils.getInstance().getImageLoader().get(url, listener, 0, 0);
        }else if(viewType == VIEW_TYPE_TITLE){
            String title = mDatas.get(position+1).getType();
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.setTitle(title);
        }else if(viewType == VIEW_TYPE_ITEM){
            String desc = itemData.getDescription(), who = itemData.getWho();
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.setContent(desc, who);
            itemHolder.setTag(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return VIEW_TYPE_IMG;
        }else if(mDatas.get(position).getType() == ItemResponseData.ITEM_TYPE_NONE){
            return VIEW_TYPE_TITLE;
        }else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size():0;
    }

    @Override
    public void onClick(View v) {
        int index = (int)v.getTag();
        ItemResponseData itemData = mDatas.get(index);
        String url = itemData.getUrl();
        String desc = itemData.getDescription();
        Intent i = new Intent("com.gky.volleydeme.web");
        i.putExtra("Url", url);
        i.putExtra("Desc", desc);
        mContext.startActivity(i);
    }

    class ImageHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_photo)
        ImageView mPhoto;

        public ImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getImageView(){
            return mPhoto;
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        TextView mTitle;

        public TitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTitle(String title){
            mTitle.setText(title);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_content)
        TextView mContent;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setContent(String desc, String who){
            String content = String.format("%s(%s)", desc, who);
            int first = content.indexOf("("), last = content.indexOf(")");
            SpannableStringBuilder builder = new SpannableStringBuilder(content);
            builder.setSpan(new ForegroundColorSpan(Color.GRAY), first, last+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContent.setText(builder);
        }

        public void setTag(int index){
            itemView.setTag(index);
        }
    }
}
