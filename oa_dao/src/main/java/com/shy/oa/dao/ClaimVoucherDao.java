package com.shy.oa.dao;

import com.shy.oa.entity.ClaimVoucher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("claimVoucherDao")
public interface ClaimVoucherDao {
    void insert(ClaimVoucher claimVoucher);
    void update(ClaimVoucher claimVoucher);
    void delete(int id);
    ClaimVoucher select(int id);

    //根据创建者编号，查询某个创建者的报销单
    List<ClaimVoucher> selectByCreateSn(String csn);
    //根据处理人编号，查询某个处理者的报销单
    List<ClaimVoucher> selectByNextDealSn(String ndsn);
}
