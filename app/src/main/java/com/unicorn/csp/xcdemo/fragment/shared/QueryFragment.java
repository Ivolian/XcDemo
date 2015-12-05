package com.unicorn.csp.xcdemo.fragment.shared;

import android.widget.FrameLayout;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import butterknife.Bind;


public class QueryFragment extends ButterKnifeFragment {

    @Bind(R.id.fl_tree_view_container)
    FrameLayout flTreeViewContainer;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test;
    }


    @Override
    public void initViews() {

        TreeNode root = TreeNode.root();

        TreeNode parent = new TreeNode(new TreeViewHolder.TreeItem(1, "临床维修"));
        root.addChild(parent);

        TreeNode child1 = new TreeNode(new TreeViewHolder.TreeItem(1, "宗维"));
        TreeNode child2 = new TreeNode(new TreeViewHolder.TreeItem(1, "强电"));
        TreeNode child3 = new TreeNode(new TreeViewHolder.TreeItem(1, "弱电"));

        parent.addChildren(child1, child2,child3);

        AndroidTreeView androidTreeView = new AndroidTreeView(getActivity(), root);
        androidTreeView.setDefaultViewHolder(TreeViewHolder.class);
        androidTreeView.setDefaultAnimation(true);
        androidTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);

        flTreeViewContainer.addView(androidTreeView.getView());

    }
}
