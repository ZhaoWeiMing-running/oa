package com.shy.oa.dao;


import com.shy.oa.entity.ClaimVoucherItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("claimVoucherItemDao")
public interface ClaimVoucherItemDao {
    void insert(ClaimVoucherItem claimVoucherItem);
    void update(ClaimVoucherItem claimVoucherItem);
    void delete(int id);

    //根据报销单编号，查询报销单详情
    List<ClaimVoucherItem> selectByClaimVoucher(int cvid);
}
