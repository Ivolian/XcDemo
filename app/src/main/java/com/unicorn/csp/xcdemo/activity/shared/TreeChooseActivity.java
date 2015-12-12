package com.unicorn.csp.xcdemo.activity.shared;


import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.fragment.shared.TreeNodeViewHolder;
import com.unicorn.csp.xcdemo.fragment.shared.WorkOrderQueryFragment;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONObject;

public class TreeChooseActivity extends ToolbarActivity {


    // ================================== extra ==================================

    @InjectExtra("tag")
    String tag;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_choose);
        initToolbar(getTitleByTag(), true);
        fetchRootNodeChildren();
        enableSlideFinish();
    }

    private void fetchRootNodeChildren() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1" + getMiddlePartUrlByTag() + "/tree?id=" + getRootIdlByTag();
        Request<JSONArray> jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                url,
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
                    finishWithResult(treeNodeData);
                }
            }
        });
        treeView.setDefaultNodeLongClickListener(new TreeNode.TreeNodeLongClickListener() {
            @Override
            public boolean onLongClick(TreeNode node, Object value) {
                    finishWithResult((TreeNodeViewHolder.TreeNodeData)value);
                return true;
            }
        });
        treeView.setDefaultViewHolder(TreeNodeViewHolder.class);
        treeView.setDefaultAnimation(false);
        treeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom, false);
    }

    private void finishWithResult(TreeNodeViewHolder.TreeNodeData treeNodeData) {
        Intent intent = new Intent();
        intent.putExtra("treeNodeData", treeNodeData);
        setResult(getResultCodeByTag(), intent);
        finish();
    }


    // ================================== fetchTreeNodeChildren ==================================

    private void fetchTreeNodeChildren(final TreeNode node, final TreeNodeViewHolder.TreeNodeData treeNodeData) {
        String url = ConfigUtils.getBaseUrl() + "/api/v1" + getMiddlePartUrlByTag() + "/tree?id=" + treeNodeData.id;
        Request<JSONArray> jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                url,
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


    // ================================== byTag methods ==================================

    private String getTitleByTag() {
        switch (tag) {
            case WorkOrderQueryFragment.WORK_ORDER_TYPE_TAG:
                return "选择工单类型";
            case WorkOrderQueryFragment.DEPARTMENT_TAG:
                return "选择科室";
            case WorkOrderQueryFragment.EQUIPMENT_TAG:
                return "选择维修设备";
            case WorkOrderQueryFragment.WORK_ORDER_STATUS_TAG:
                return "选择工单状态";
            case WorkOrderQueryFragment.EMERGENCY_DEGREE_TAG:
                return "选择工单级别";
        }
        return null;
    }

    private String getMiddlePartUrlByTag() {
        switch (tag) {
            case WorkOrderQueryFragment.DEPARTMENT_TAG:
                return "/hems/department";
            case WorkOrderQueryFragment.EQUIPMENT_TAG:
                return "/hems/equipment";
            case WorkOrderQueryFragment.WORK_ORDER_TYPE_TAG:
            case WorkOrderQueryFragment.WORK_ORDER_STATUS_TAG:
            case WorkOrderQueryFragment.EMERGENCY_DEGREE_TAG:
                return "/system/code";
        }
        return null;
    }

    private String getRootIdlByTag() {
        switch (tag) {
            case WorkOrderQueryFragment.WORK_ORDER_TYPE_TAG:
                return "9193088f-940b-4305-8e7c-41cf3854c962";
            case WorkOrderQueryFragment.DEPARTMENT_TAG:
                return "1";
            case WorkOrderQueryFragment.EQUIPMENT_TAG:
                return "Root";
            case WorkOrderQueryFragment.WORK_ORDER_STATUS_TAG:
                return "04810cbc-d923-4158-b0a1-a0291759fc3a";
            case WorkOrderQueryFragment.EMERGENCY_DEGREE_TAG:
                return "8573c420-8a0b-45bd-ae7d-42fda56c990e";
        }
        return null;
    }

    private int getResultCodeByTag() {
        switch (tag) {
            case WorkOrderQueryFragment.WORK_ORDER_TYPE_TAG:
                return WorkOrderQueryFragment.WORK_ORDER_TYPE_CODE;
            case WorkOrderQueryFragment.DEPARTMENT_TAG:
                return WorkOrderQueryFragment.DEPARTMENT_CODE;
            case WorkOrderQueryFragment.EQUIPMENT_TAG:
                return WorkOrderQueryFragment.EQUIPMENT_CODE;
            case WorkOrderQueryFragment.WORK_ORDER_STATUS_TAG:
                return WorkOrderQueryFragment.WORK_ORDER_STATUS_CODE;
            case WorkOrderQueryFragment.EMERGENCY_DEGREE_TAG:
                return WorkOrderQueryFragment.EMERGENCY_DEGREE_CODE;
        }
        return 0;
    }


}
