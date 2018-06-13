package com.binger.common.enums;


public enum BillTypeEnum {
    ORDER_BILL("order", "SO2018", 14, 0, 99999999),
    ORDER_DETAIL_BILL("orderD", "SOD2018", 15, 0, 99999999),
    RETURN_ORDER_BILL("return", "RO2018", 14, 0, 99999999),
    RETURN_DETAIL_BILL("returnD", "ROD2018", 15, 0, 99999999),
    PRODUCT_PLAN_BILL("productPlan", "PL2018", 14, 0, 99999999),
    PRODUCT_PLAN_DETAIL_BILL("productPlanD", "PLD2018", 15, 0, 99999999),
    PRODUCT_ORDER_BILL("productOrder", "PO2018", 14, 0, 99999999),
    PRODUCT_ORDER_DETAIL_BILL("productOrderD", "POD2018", 15, 0, 99999999),
    STOCK_IN_BILL("stockIn", "SI2018", 14, 0, 99999999),
    STOCK_IN_DETAIL_BILL("stockInD", "SID2018", 15, 0, 99999999),
    STOCK_OUT_BILL("stockOut", "SOB2018", 14, 0, 99999999),
    STOCK_OUT_DETAIL_BILL("stockOutD", "SOBD2018", 15, 0, 99999999);
    private String billType;
    private String billPrefix;
    private int length;
    private int begin;
    private int max;

    BillTypeEnum(String billType, String billPrefix, int length, int begin, int max) {
        this.billType = billType;
        this.billPrefix = billPrefix;
        this.length = length;
        this.begin = begin;
        this.max = max;
    }

    public String getBillType() {
        return billType;
    }

    public String getBillPrefix() {
        return billPrefix;
    }

    public int getLength() {
        return length;
    }

    public int getBegin() {
        return begin;
    }

    public int getMax() {
        return max;
    }

    public static BillTypeEnum getBillTypeByType(String billType) {
        for (BillTypeEnum typeEnum : BillTypeEnum.values()) {
            if (typeEnum.getBillType().equals(billType)) {
                return typeEnum;
            }
        }
        return null;
    }
}
