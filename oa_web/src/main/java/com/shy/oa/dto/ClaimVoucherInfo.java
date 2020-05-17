package com.shy.oa.dto;

import com.shy.oa.entity.ClaimVoucher;
import com.shy.oa.entity.ClaimVoucherItem;

import java.util.List;

public class ClaimVoucherInfo {
    //报销单属性
    private ClaimVoucher claimVoucher;

    //item集合
    private List<ClaimVoucherItem> items;


    public ClaimVoucher getClaimVoucher() {
        return claimVoucher;
    }

    public void setClaimVoucher(ClaimVoucher claimVoucher) {
        this.claimVoucher = claimVoucher;
    }

    public List<ClaimVoucherItem> getItems() {
        return items;
    }

    public void setItems(List<ClaimVoucherItem> items) {
        this.items = items;
    }
}
