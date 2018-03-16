package com.carl_yang.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.ArtStudentApplication;
import com.carl_yang.common.MyUtils;
import com.carl_yang.httpintent.domain.VideoShowDomain;
import com.carl_yang.interfaces.RecycleViewInterface;
import com.carl_yang.stuart.R;

import java.util.List;

/**
 * Created by Loren Yang on 2017/10/20.
 */

public class FavoriteAdapter extends BaseRecyclerAdapter<FavoriteAdapter.FavoriteAdapterViewHolder> implements View.OnClickListener {

    private Context myContext;
    private List<VideoShowDomain> personCollectList;
    private int largeCardHeight, smallCardHeight;

    private RecycleViewInterface.OnItemOnClickListener mOnItemOnClickListener=null;

    public FavoriteAdapter(List<VideoShowDomain> list, Context context) {
        this.personCollectList = list;
        this.myContext=context;
        largeCardHeight = MyUtils.dp2px(context, 150);
        smallCardHeight = MyUtils.dp2px(context, 100);
    }

    public void setData(List<VideoShowDomain> list,Context context) {
        this.personCollectList = list;
        this.myContext=context;
        notifyDataSetChanged();
    }

    public void reSet(){
        notifyDataSetChanged();
    }

    public void insert(VideoShowDomain pc, int position) {
        insert(personCollectList, pc, position);
    }

    public void remove(int position) {
        remove(personCollectList, position);
    }

    public void clear() {
        clear(personCollectList);
    }

    @Override
    public FavoriteAdapterViewHolder getViewHolder(View view) {
        return new FavoriteAdapterViewHolder(view, false);
    }

    @Override
    public FavoriteAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.videogridview, parent, false);
        FavoriteAdapterViewHolder vh = new FavoriteAdapterViewHolder(v,true);
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(FavoriteAdapterViewHolder holder, int position, boolean isItem) {
        VideoShowDomain pc = personCollectList.get(position);
        String load_url="";
        if(pc.getType().equals("1")){
            load_url= ArtStudentApplication.upVersionUrl+"ART/"+pc.getP_url()+"";
            holder.iconVideo.setVisibility(View.VISIBLE);
            holder.iconVideo.bringToFront();
        }else{
            holder.iconVideo.setVisibility(View.GONE);
            load_url= ArtStudentApplication.upVersionUrl+"ART/"+pc.getP_url()+"_320.jpg";
        }
        System.out.println("------picture url---------"+load_url);
        Glide
                .with(myContext)
                .load(load_url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .dontAnimate()
                .into(holder.imageView);
        holder.itemView.setTag(position);
        holder.strView.setText(pc.getIamgename());
        holder.strView1.setText(pc.getAuth());
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            holder.rootView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
        }
    }

    @Override
    public int getAdapterItemCount() {
        return personCollectList.size();
    }

    public class FavoriteAdapterViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        ImageView imageView;
        ImageView iconVideo;
        TextView strView;
        TextView strView1;

        public FavoriteAdapterViewHolder(View itemView,boolean isItem) {
            super(itemView);
            if(isItem) {
                rootView=itemView.findViewById(R.id.videosgridview_rootview);
                imageView = (ImageView) itemView.findViewById(R.id.videosgridview_image);
                iconVideo= (ImageView) itemView.findViewById(R.id.videosgridview_videoicon);
                strView = (TextView) itemView.findViewById(R.id.videosgridview_str);
                strView1 = (TextView) itemView.findViewById(R.id.videosgridview_str1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println("clicke " + v.getTag()+"::"+mOnItemOnClickListener);
        if(mOnItemOnClickListener!=null){
            mOnItemOnClickListener.onItemOnClick(v, (Integer) v.getTag());
        }
    }

    public void setmOnItemOnClickListener(RecycleViewInterface.OnItemOnClickListener listener ){
        this.mOnItemOnClickListener=listener;
    }

}
