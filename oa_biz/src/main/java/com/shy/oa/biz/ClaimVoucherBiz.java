package com.shy.oa.biz;

import com.shy.oa.entity.ClaimVoucher;
import com.shy.oa.entity.ClaimVoucherItem;
import com.shy.oa.entity.DealRecord;

import java.util.List;

public interface ClaimVoucherBiz {
    //插入报销单和报销单条目
    void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items);

    //要获取详情信息
    ClaimVoucher get(int id);

    //根据报销单编号查询报销单详情
    List<ClaimVoucherItem> getItems(int cvid);

    //根据报销单编号查询处理记录
    List<DealRecord> getRecords(int cvid);


    //个人报销单
    List<ClaimVoucher> getForSelf(String sn);

    //获取待处理报销单
    List<ClaimVoucher> getForDeal(String sn);

    //修改报销单
    void update(ClaimVoucher claimVoucher,List<ClaimVoucherItem> items);

    //提交
    void submit(int id);

    //审核
    void deal(DealRecord dealRecord);

}
