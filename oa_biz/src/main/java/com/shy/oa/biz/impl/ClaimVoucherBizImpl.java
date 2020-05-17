package com.shy.oa.biz.impl;

import com.shy.oa.biz.ClaimVoucherBiz;
import com.shy.oa.dao.ClaimVoucherDao;
import com.shy.oa.dao.ClaimVoucherItemDao;
import com.shy.oa.dao.DealRecordDao;
import com.shy.oa.dao.EmployeeDao;
import com.shy.oa.entity.ClaimVoucher;
import com.shy.oa.entity.ClaimVoucherItem;
import com.shy.oa.entity.DealRecord;
import com.shy.oa.entity.Employee;
import com.shy.oa.global.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {

    @Autowired
    private ClaimVoucherDao claimVoucherDao;

    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;

    @Autowired
    private DealRecordDao dealRecordDao;

    @Autowired
    private EmployeeDao employeeDao;

    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setCreateTime(new Date());
        //设置待处理人，肯定是创建的人
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        //设置状态
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);
        //插入
        claimVoucherDao.insert(claimVoucher);

        //要获取报销单详情，只需要报销单的id
        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            claimVoucherItemDao.insert(item);
        }

    }

    public ClaimVoucher get(int id) {
        return claimVoucherDao.select(id);
    }

    public List<ClaimVoucherItem> getItems(int cvid) {
        return claimVoucherItemDao.selectByClaimVoucher(cvid);
    }

    public List<DealRecord> getRecords(int cvid) {
        return dealRecordDao.selectByClaimVoucher(cvid);
    }

    //获取个人报销单
    public List<ClaimVoucher> getForSelf(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    //获取待处理人
    public List<ClaimVoucher> getForDeal(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    public void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        /**
         * 业务逻辑：更新基本信息--》
         *          更新条目信息--》删除我准备不要的那些信息???--》修改已经有的这些条目--》插入之前不存在的新条目
         */

        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);
        claimVoucherDao.update(claimVoucher);

        //其实有两个条目的集合，一个是原有保存在数据库中的集合，一个是传递来的集合，所以要去掉原来的

        //查出老的条目
        List<ClaimVoucherItem> olds=claimVoucherItemDao.selectByClaimVoucher(claimVoucher.getId());
        //
        for (ClaimVoucherItem old : olds) {
            //判断当前获取的old是否不存在 items这个集合里面，所以要等迭代完这个集合才能知道
            boolean isHave=false;
            for (ClaimVoucherItem item : items) {
                if (item.getId()==old.getId()){
                    isHave=true;
                    break;
                }
            }
            //判断isHave是否为false,如果是false代表着，我迭代了还是没找到，
            // 在新的这个集合里面都没有找到这个条目，那代表这个条目是应该被删掉的
            if (!isHave){
                claimVoucherItemDao.delete(old.getId());
            }
        }
        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            if (item.getId()!=null & item.getId()>0){
                claimVoucherItemDao.update(item);
            }else {
                claimVoucherItemDao.insert(item);
            }
        }
    }

    public void submit(int id) {
        //先拿到报销单
        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        //创建人信息
        Employee employee = employeeDao.select(claimVoucher.getCreateSn());

        //状态，已提交就是待审核的状态
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_SUBMIT);
        //获得处理人，因为是集合，所以要get(0),根据编号来的所以要getSn
        claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(),Contant.POST_FM).get(0).getSn());
        claimVoucherDao.update(claimVoucher);

        //操作记录表
        DealRecord dealRecord=new DealRecord();
        dealRecord.setDealWay(Contant.DEAL_SUBMIT);
        dealRecord.setDealSn(employee.getSn());
        dealRecord.setClaimVoucherId(id);
        dealRecord.setDealResult(Contant.CLAIMVOUCHER_SUBMIT);
        dealRecord.setDealTime(new Date());
        dealRecord.setComment("无");
        dealRecordDao.insert(dealRecord);
    }

    //审核
    public void deal(DealRecord dealRecord) {
        //对报销单进行处理，要处理很多情况
        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());

        //获取处理人
        Employee employee = employeeDao.select(dealRecord.getDealSn());

        //没必要重复写处理时间，所以在判断之前写
        dealRecord.setDealTime(new Date());


        //审核通过的情况
        if (dealRecord.getDealWay().equals(Contant.DEAL_PASS)){
            //有两种情况，一种是金额小于5000，一种是审核人为总经理
            //不需要复审的
            if (claimVoucher.getTotalAmount()<Contant.LIMIT_CHECK || employee.getPost().equals(Contant.POST_GM)){
                //状态设为已审核
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                //设置审核人为财务
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null,Contant.POST_CASHIER).get(0).getSn());

                dealRecord.setDealResult(Contant.CLAIMVOUCHER_APPROVED);
            }else {
                //状态设为待复审
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_RECHECK);
                //设置审核人为财务
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null,Contant.POST_GM).get(0).getSn());

                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            }
        } else if (dealRecord.getDealWay().equals(Contant.DEAL_BACK)) {
            //状态设为打回
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_BACK);
            //处理人为创建者
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());

            dealRecord.setDealResult(Contant.CLAIMVOUCHER_BACK);
        }else if (dealRecord.getDealWay().equals(Contant.DEAL_REJECT)){
            //状态设为拒绝
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_TERMINATED);
            //处理人为创建者
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealResult(Contant.CLAIMVOUCHER_TERMINATED);
        }else if (dealRecord.getDealWay().equals(Contant.DEAL_PAID)){
            //状态设为已打款
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_PAID);
            //处理人为创建者
            claimVoucher.setNextDealSn(null);


            dealRecord.setDealResult(Contant.CLAIMVOUCHER_PAID);
        }




        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);




    }


}
