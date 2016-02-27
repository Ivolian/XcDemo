package com.unicorn.csp.xcdemo.fragment.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;
import com.unicorn.csp.xcdemo.R;
import com.unnamed.b.atv.model.TreeNode;

import java.io.Serializable;


public class TreeNodeViewHolder extends TreeNode.BaseNodeViewHolder<TreeNodeViewHolder.TreeNodeData> {

    public TreeNodeViewHolder(Context context) {
        super(context);
    }

    IconicsImageView iivToggle;

    @Override
    public View createNodeView(TreeNode node, TreeNodeData value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.item_tree_node, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.tv_node_text);
        tvValue.setText(value.name);
        iivToggle = (IconicsImageView) view.findViewById(R.id.iiv_toggle);
        if (value.leaf == 1) {
            iivToggle.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void toggle(boolean active) {
        iivToggle.setIcon(active ? "gmd-minus-square" : "gmd-plus-square");
    }


    public static class TreeNodeData implements Serializable{
        public String id;
        public String objectId;
        public String name;
        public int leaf;
        public boolean hasFetchChildren;

        public TreeNodeData(String id,String objectId, String name, int leaf) {
            this.id = id;
            this.objectId = objectId;
            this.name = name;
            this.leaf = leaf;
            this.hasFetchChildren = false;
        }
    }

}
