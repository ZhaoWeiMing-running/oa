package com.shy.oa.dao;

import com.shy.oa.entity.DealRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理记录就是日志
 * 没有删除和修改
 */


@Repository("dealRecordDao")
public interface DealRecordDao {
    void insert(DealRecord dealRecord);

    //针对某个报销单，查询处理记录
    List<DealRecord> selectByClaimVoucher(int cvid);
}
