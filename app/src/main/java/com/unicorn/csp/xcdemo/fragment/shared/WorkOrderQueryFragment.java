package com.unicorn.csp.xcdemo.fragment.shared;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.TreeChooseActivity;
import com.unicorn.csp.xcdemo.activity.shared.WorkOrderQueryResultActivity;
import com.unicorn.csp.xcdemo.component.MenuUtils;
import com.unicorn.csp.xcdemo.fragment.shared.base.ButterKnifeFragment;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.EditTextUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;


public class WorkOrderQueryFragment extends ButterKnifeFragment {


    // ================================== getLayoutResId ==================================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_work_order_query;
    }


    // ================================== menu ==================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_only_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setIcon(MenuUtils.getSearchIconDrawable(getActivity()));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            query();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // ================================== workOrderType tree ==================================

    public static final int WORK_ORDER_TYPE_CODE = 2333_1;

    public static final String WORK_ORDER_TYPE_TAG = "workOrderType";

    @BindView(R.id.et_work_order_type)
    MaterialEditText etWorkOrderType;

    TreeNodeViewHolder.TreeNodeData tndWorkOrderType;

    @OnClick(R.id.et_work_order_type)
    public void workOrderTypeOnClick() {
        startTreeChooseActivity(WORK_ORDER_TYPE_TAG, WORK_ORDER_TYPE_CODE);
    }

    @OnFocusChange(R.id.et_work_order_type)
    public void workOrderTypeOnFocus(boolean focused) {
        if (focused) {
            startTreeChooseActivity(WORK_ORDER_TYPE_TAG, WORK_ORDER_TYPE_CODE);
        }
    }


    // ================================== department tree ==================================

    public static final int DEPARTMENT_CODE = 2333_2;

    public static final String DEPARTMENT_TAG = "department";

    @BindView(R.id.et_department)
    MaterialEditText etDepartment;

    TreeNodeViewHolder.TreeNodeData tndDepartment;

    @OnClick(R.id.et_department)
    public void departmentOnClick() {
        startTreeChooseActivity(DEPARTMENT_TAG, DEPARTMENT_CODE);
    }

    @OnFocusChange(R.id.et_department)
    public void departmentOnFocus(boolean focused) {
        if (focused) {
            startTreeChooseActivity(DEPARTMENT_TAG, DEPARTMENT_CODE);
        }
    }


    // ================================== equipment tree ==================================

    public static final int EQUIPMENT_CODE = 2333_3;

    public static final String EQUIPMENT_TAG = "equipment";

    @BindView(R.id.et_equipment)
    MaterialEditText etEquipment;

    TreeNodeViewHolder.TreeNodeData tndEquipment;

    @OnClick(R.id.et_equipment)
    public void equipmentOnClick() {
        startTreeChooseActivity(EQUIPMENT_TAG, EQUIPMENT_CODE);
    }

    @OnFocusChange(R.id.et_equipment)
    public void equipmentOnFocus(boolean focused) {
        if (focused) {
            startTreeChooseActivity(EQUIPMENT_TAG, EQUIPMENT_CODE);
        }
    }


    // ================================== workOrderStatus tree ==================================

    public static final int WORK_ORDER_STATUS_CODE = 2333_4;

    public static final String WORK_ORDER_STATUS_TAG = "workOrderStatus";

    @BindView(R.id.et_work_order_status)
    MaterialEditText etWorkOrderStatus;

    TreeNodeViewHolder.TreeNodeData tndWorkOrderStatus;

    @OnClick(R.id.et_work_order_status)
    public void workOrderStatusOnClick() {
        startTreeChooseActivity(WORK_ORDER_STATUS_TAG, WORK_ORDER_STATUS_CODE);
    }

    @OnFocusChange(R.id.et_work_order_status)
    public void workOrderStatusOnFocus(boolean focused) {
        if (focused) {
            startTreeChooseActivity(WORK_ORDER_STATUS_TAG, WORK_ORDER_STATUS_CODE);
        }
    }


    // ================================== emergencyDegree tree ==================================

    public static final int EMERGENCY_DEGREE_CODE = 2333_5;

    public static final String EMERGENCY_DEGREE_TAG = "emergencyDegree";

    @BindView(R.id.et_emergency_degree)
    MaterialEditText etEmergencyDegree;

    TreeNodeViewHolder.TreeNodeData tndEmergencyDegree;

    @OnClick(R.id.et_emergency_degree)
    public void emergencyDegreeOnClick() {
        startTreeChooseActivity(EMERGENCY_DEGREE_TAG, EMERGENCY_DEGREE_CODE);
    }

    @OnFocusChange(R.id.et_emergency_degree)
    public void emergencyDegreeOnFocus(boolean focused) {
        if (focused) {
            startTreeChooseActivity(EMERGENCY_DEGREE_TAG, EMERGENCY_DEGREE_CODE);
        }
    }


    // ================================== onActivityResult ==================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WORK_ORDER_TYPE_CODE && resultCode == WORK_ORDER_TYPE_CODE) {
            tndWorkOrderType = (TreeNodeViewHolder.TreeNodeData) data.getSerializableExtra("treeNodeData");
            etWorkOrderType.setText(tndWorkOrderType.name);
        }
        if (requestCode == DEPARTMENT_CODE && resultCode == DEPARTMENT_CODE) {
            tndDepartment = (TreeNodeViewHolder.TreeNodeData) data.getSerializableExtra("treeNodeData");
            etDepartment.setText(tndDepartment.name);
        }
        if (requestCode == EQUIPMENT_CODE && resultCode == EQUIPMENT_CODE) {
            tndEquipment = (TreeNodeViewHolder.TreeNodeData) data.getSerializableExtra("treeNodeData");
            etEquipment.setText(tndEquipment.name);
        }
        if (requestCode == WORK_ORDER_STATUS_CODE && resultCode == WORK_ORDER_STATUS_CODE) {
            tndWorkOrderStatus = (TreeNodeViewHolder.TreeNodeData) data.getSerializableExtra("treeNodeData");
            etWorkOrderStatus.setText(tndWorkOrderStatus.name);
        }
        if (requestCode == EMERGENCY_DEGREE_CODE && resultCode == EMERGENCY_DEGREE_CODE) {
            tndEmergencyDegree = (TreeNodeViewHolder.TreeNodeData) data.getSerializableExtra("treeNodeData");
            etEmergencyDegree.setText(tndEmergencyDegree.name);
        }
    }


    // ================================== startTreeChooseActivity ==================================

    private void startTreeChooseActivity(String tag, int requestCode) {
        Intent intent = new Intent(getActivity(), TreeChooseActivity.class);
        intent.putExtra("tag", tag);
        startActivityForResult(intent, requestCode);
    }


    // ================================== begin repair date ==================================

    @BindView(R.id.et_begin_repair_date)
    MaterialEditText etBeginRepairDate;

    @OnClick(R.id.et_begin_repair_date)
    public void beginRepairDateOnClick() {
        chooseBeginRepairDate();
    }

    @OnFocusChange(R.id.et_begin_repair_date)
    public void beginRepairDateOnFocus(boolean focused) {
        if (focused) {
            chooseBeginRepairDate();
        }
    }

    int beginYear = 0, beginMonth = 0, beginDay = 0;

    private void chooseBeginRepairDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        beginYear = year;
                        beginMonth = monthOfYear;
                        beginDay = dayOfMonth;
                        etBeginRepairDate.setText(getDateString(year, monthOfYear, dayOfMonth));
                    }
                },
                beginYear == 0 ? now.get(Calendar.YEAR) : beginYear,
                beginMonth == 0 ? now.get(Calendar.MONTH) : beginMonth,
                beginDay == 0 ? now.get(Calendar.DAY_OF_MONTH) : beginDay
        );
        datePickerDialog.show(getActivity().getFragmentManager(), "beginRepairDate");
    }


    // ================================== end repair date ==================================

    @BindView(R.id.et_end_repair_date)
    MaterialEditText etEndRepairDate;

    @OnClick(R.id.et_end_repair_date)
    public void endRepairDateOnClick() {
        chooseEndRepairDate();
    }

    @OnFocusChange(R.id.et_end_repair_date)
    public void endRepairDateOnFocus(boolean focused) {
        if (focused) {
            chooseEndRepairDate();
        }
    }

    int endYear = 0, endMonth = 0, endDay = 0;

    private void chooseEndRepairDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        endYear = year;
                        endMonth = monthOfYear;
                        endDay = dayOfMonth;
                        etEndRepairDate.setText(getDateString(year, monthOfYear, dayOfMonth));
                    }
                },
                endYear == 0 ? now.get(Calendar.YEAR) : endYear,
                endMonth == 0 ? now.get(Calendar.MONTH) : endMonth,
                endDay == 0 ? now.get(Calendar.DAY_OF_MONTH) : endDay
        );
        datePickerDialog.show(getActivity().getFragmentManager(), "endRepairDate");
    }

    private String getDateString(int year, int month, int day) {
        month++;
        String dateString = year + "-";
        dateString += (month > 9 ? month : "0" + month);
        dateString += "-";
        dateString += (day > 9 ? day : "0" + day);
        return dateString;
    }


    // ================================== query ==================================

    public void query() {
        Intent intent = new Intent(getActivity(), WorkOrderQueryResultActivity.class);
        intent.putExtra("queryUrl", getQueryUrl());
        getActivity().startActivity(intent);
    }

    private String getQueryUrl() {
        String queryUrl = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/info?";
        if (!EditTextUtils.isEmpty(etWorkOrderType)) {
            queryUrl += ("&type" + "=" + tndWorkOrderType.objectId);
        }
        if (!EditTextUtils.isEmpty(etDepartment)) {
            queryUrl += ("&requestDepartment" + "=" + tndDepartment.objectId);
        }
        if (!EditTextUtils.isEmpty(etEquipment)) {
            queryUrl += ("&equipment" + "=" + tndEquipment.objectId);
        }
        if (!EditTextUtils.isEmpty(etWorkOrderStatus)) {
            queryUrl += ("&status" + "=" + tndWorkOrderStatus.objectId);
        }
        if (!EditTextUtils.isEmpty(etEmergencyDegree)) {
            queryUrl += ("&emergencyDegree" + "=" + tndEmergencyDegree.objectId);
        }
        if (!EditTextUtils.isEmpty(etBeginRepairDate)) {
            queryUrl += ("&startDate" + "=" + EditTextUtils.getValue(etBeginRepairDate));
        }
        if (!EditTextUtils.isEmpty(etEndRepairDate)) {
            queryUrl += ("&endDate" + "=" + EditTextUtils.getValue(etEndRepairDate));
        }
        return queryUrl;
    }


}
