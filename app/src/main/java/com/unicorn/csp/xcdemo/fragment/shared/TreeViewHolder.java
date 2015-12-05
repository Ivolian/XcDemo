package com.unicorn.csp.xcdemo.fragment.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;
import com.unnamed.b.atv.model.TreeNode;

public class TreeViewHolder extends TreeNode.BaseNodeViewHolder<TreeViewHolder.TreeItem> {

    public TreeViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, TreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.item_tree_view, null, false);
        TextView tvText = (TextView) view.findViewById(R.id.tv_text);
        tvText.setText(value.text);

        return view;
    }
    public static class TreeItem {
        public int icon;
        public String text;

        public TreeItem(int icon, String text) {
            this.icon = icon;
            this.text = text;
        }
    }
}