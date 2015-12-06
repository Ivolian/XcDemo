package com.unicorn.csp.xcdemo.fragment.shared;

import android.content.Intent;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.TreeChooseActivity;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnFocusChange;


public class WorkOrderQueryFragment extends ButterKnifeFragment {


    // ================================== getLayoutResId ==================================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_work_order_query;
    }


    // ================================== workOrderType tree ==================================

    public static final int WORK_ORDER_TYPE_CODE = 2333_1;

    public static final String WORK_ORDER_TYPE_TAG = "workOrderType";

    @Bind(R.id.et_work_order_type)
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

    @Bind(R.id.et_department)
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

    @Bind(R.id.et_equipment)
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

    @Bind(R.id.et_work_order_status)
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

    @Bind(R.id.et_emergency_degree)
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


    // ================================== repair date ==================================

    @Bind(R.id.et_repair_date)
    MaterialEditText etRepairDate;

    @OnClick(R.id.et_repair_date)
    public void repairDateOnClick() {
        showDatePicker();
    }

    @OnFocusChange(R.id.et_repair_date)
    public void repairDateOnFocus(boolean focused) {
        if (focused) {
            showDatePicker();
        }
    }

    int mYear = 0, mMonthOfYear = 0, mDayOfMonth = 0, mYearEnd = 0, mMonthOfYearEnd = 0, mDayOfMonthEnd = 0;

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
                        String beginDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        String endDateString = yearEnd + "-" + (monthOfYearEnd + 1) + "-" + dayOfMonthEnd;
                        String dateString = beginDateString + " 至 " + endDateString;
                        etRepairDate.setText(dateString);

                        mYear = year;
                        mMonthOfYear = monthOfYear;
                        mDayOfMonth = dayOfMonth;
                        mYearEnd = yearEnd;
                        mMonthOfYearEnd = monthOfYearEnd;
                        mDayOfMonthEnd = dayOfMonthEnd;
                    }
                },
                mYear == 0 ? now.get(Calendar.YEAR) : mYear,
                mMonthOfYear == 0 ? now.get(Calendar.MONTH) : mMonthOfYear,
                mDayOfMonth == 0 ? now.get(Calendar.DAY_OF_MONTH) : mDayOfMonth
        );
        datePickerDialog.show(getActivity().getFragmentManager(), "repairDate");
    }

}
