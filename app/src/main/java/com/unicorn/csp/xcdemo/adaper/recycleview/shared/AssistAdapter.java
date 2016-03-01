package com.unicorn.csp.xcdemo.adaper.recycleview.shared;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.model.AssistObject;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AssistAdapter extends RecyclerView.Adapter<AssistAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<AssistObject> dataList = new ArrayList<>();

    private List<Integer> selectedList = new ArrayList<>();


    public AssistAdapter(List<AssistObject> dataList, List<Integer> selectedList) {
        this.dataList = dataList;
        this.selectedList = selectedList;
    }

    public void setDataList(List<AssistObject> dataList) {
        this.dataList = dataList;
    }

    public void setSelectedList(List<Integer> selectedList) {
        this.selectedList = selectedList;
    }

    // ================================== 公开的方法 ==================================

    public void clearSelected() {
        selectedList.clear();
        notifyDataSetChanged();
        notifyCab();
    }

    public int getSelectedCount() {
        return selectedList.size();
    }

    public void selectAll() {
        for (AssistObject assistObject : dataList) {
            int index = dataList.indexOf(assistObject);
            if (!selectedList.contains(index)) {
                selectedList.add(index);
            }
        }
        notifyDataSetChanged();
        notifyCab();
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.photo)
        SimpleDraweeView ivPhoto;

        @Bind(R.id.name)
        TextView tvName;

        @Bind(R.id.phone)
        TextView tvPhone;

        @Bind(R.id.checkbox)
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.root)
        public void onCliCK() {
            select();
        }

        private void select() {
            Integer position = getAdapterPosition();
            if (selectedList.contains(position)) {
                selectedList.remove(position);
            } else {
                selectedList.add(position);
            }
            notifyItemChanged(position);
            notifyCab();
        }
    }

    private void notifyCab() {
        EventBus.getDefault().post(new Object(), "assist_select");
    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_assist, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        AssistObject assistObject = dataList.get(position);
        String photoUrl = ConfigUtils.getBaseUrl() + "/api/v1/hems/artificer/picture/" + assistObject.getObjectId();
//        if (assistObject.getName().equals("王春晶")){
//            ToastUtils.show(photoUrl);
//        }
        Uri uri = Uri.parse(photoUrl);
        viewHolder.ivPhoto.setImageURI(uri);
        viewHolder.tvName.setText(assistObject.getName());
        viewHolder.tvPhone.setText(assistObject.getTelephone());
        viewHolder.checkBox.setChecked(selectedList.contains(position));
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    //

}
