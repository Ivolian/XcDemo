package com.unicorn.csp.xcdemo.activity.shared;


import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.fragment.shared.TreeNodeViewHolder;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONObject;

public class TreeChooseActivity extends ToolbarActivity {


    // ================================== extra ==================================

    @InjectExtra("title")
    String title;

    String tag = "department";


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_choose);
        initToolbar(title, true);
        fetchRootNodeChildren();
        enableSlideFinish();
    }

    private void fetchRootNodeChildren() {
        Request<JSONArray> jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                ConfigUtils.getBaseUrl() + "/api/v1/hems/" + tag + "/tree?id=Root",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        TreeNode root = TreeNode.root();
                        for (int i = 0; i != response.length(); i++) {
                            root.addChild(getTreeNode(JSONUtils.getJSONObject(response, i)));
                        }
                        initTreeView(root);
                        ((FrameLayout) findViewById(R.id.fl_tree_view_container)).addView(treeView.getView());
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }


    // ================================== initTreeView ==================================

    AndroidTreeView treeView;

    private void initTreeView(TreeNode root) {
        treeView = new AndroidTreeView(this, root);
        treeView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                TreeNodeViewHolder.TreeNodeData treeNodeData = (TreeNodeViewHolder.TreeNodeData) value;
                if (treeNodeData.leaf == 0 && !treeNodeData.hasFetchChildren) {
                    fetchTreeNodeChildren(node, treeNodeData);
                }
                if (treeNodeData.leaf == 1) {
                    // todo
                    ToastUtils.show("选中: " + treeNodeData.name);
                }
            }
        });
        treeView.setDefaultViewHolder(TreeNodeViewHolder.class);
        // 不是我不想用动画，数据量太大，用动画要等待。。。
        treeView.setDefaultAnimation(false);
        treeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom, false);

    }


    // ================================== fetchTreeNodeChildren ==================================

    private void fetchTreeNodeChildren(final TreeNode node, final TreeNodeViewHolder.TreeNodeData treeNodeData) {
        Request<JSONArray> jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                ConfigUtils.getBaseUrl() + "/api/v1/hems/" + tag + "/tree?id=" + treeNodeData.id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i != response.length(); i++) {
                            node.addChild(getTreeNode(JSONUtils.getJSONObject(response, i)));
                        }
                        treeNodeData.hasFetchChildren = true;
                        treeView.expandNode(node);
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    private TreeNode getTreeNode(JSONObject jsonObject) {
        String id = JSONUtils.getString(jsonObject, "id", "");
        String name = JSONUtils.getString(jsonObject, "name", "");
        int leaf = JSONUtils.getInt(jsonObject, "leaf", 1);
        return new TreeNode(new TreeNodeViewHolder.TreeNodeData(id, name, leaf));
    }

}
