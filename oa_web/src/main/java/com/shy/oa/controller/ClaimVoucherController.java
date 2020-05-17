package com.shy.oa.controller;

import com.shy.oa.biz.ClaimVoucherBiz;
import com.shy.oa.dto.ClaimVoucherInfo;
import com.shy.oa.entity.DealRecord;
import com.shy.oa.entity.Employee;
import com.shy.oa.global.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/claim_voucher")
public class ClaimVoucherController {

    @Autowired
    private ClaimVoucherBiz claimVoucherBiz;

    /**
     * 业务逻辑：去填写--》填写完后进行保存--》填完后跳转到详情页面
     * @param map
     * @return
     */


    //页面上填写报销单，是需要所有的费用类型，所以需要用一个数据进行传递，既然要传递了，用map
    @RequestMapping("/to_add")
    public String toAdd(Map<String,Object> map){
        map.put("items", Contant.getItems());
        map.put("info",new ClaimVoucherInfo());
        return "claim_voucher_add";
    }

    //添加功能
    @RequestMapping("/add")
    public String add(HttpSession session, ClaimVoucherInfo info){
        Employee employee = (Employee) session.getAttribute("employee");
        //把报销单的创建者编号设置为当前登录的创建者编号
        info.getClaimVoucher().setCreateSn(employee.getSn());
        //需要拿出两个条目，一个是报销单，一个是报销单的条目
        claimVoucherBiz.save(info.getClaimVoucher(),info.getItems());

        //跳转,需要携带id，这样跳转后才能显示哪个人的编号
        return "redirect:deal";
    }

    //报销单详情
    @RequestMapping("/detail")
    public String detail(int id,Map<String,Object> map){
        //查看哪个报销单信息，我才传递到页面
        map.put("claimVoucher",claimVoucherBiz.get(id));
        map.put("items",claimVoucherBiz.getItems(id));
        map.put("records",claimVoucherBiz.getRecords(id));
        return "claim_voucher_detail";
    }



    //跳转个人报销单
    @RequestMapping("/self")
    public String self(HttpSession session,Map<String,Object> map){
        Employee employee = (Employee) session.getAttribute("employee");
        map.put("list",claimVoucherBiz.getForSelf(employee.getSn()));
        return "claim_voucher_self";
    }


    @RequestMapping("/deal")
    public String deal(HttpSession session,Map<String,Object> map){
        Employee employee = (Employee) session.getAttribute("employee");
        map.put("list",claimVoucherBiz.getForDeal(employee.getSn()));
        return "claim_voucher_deal";
    }

    @RequestMapping("/to_update")
    public String toUpdate(int id,Map<String,Object> map){
        map.put("items",Contant.getItems());
        ClaimVoucherInfo info=new ClaimVoucherInfo();
        //基本信息
        info.setClaimVoucher(claimVoucherBiz.get(id));
        //条目信息
        info.setItems(claimVoucherBiz.getItems(id));
        map.put("info",info);
        return "claim_voucher_update";
    }

    @RequestMapping("/update")
    public String update(HttpSession session, ClaimVoucherInfo info){
        Employee employee = (Employee) session.getAttribute("employee");
        //把报销单的创建者编号设置为当前登录的创建者编号
        info.getClaimVoucher().setCreateSn(employee.getSn());
        //需要拿出两个条目，一个是报销单，一个是报销单的条目
        claimVoucherBiz.save(info.getClaimVoucher(),info.getItems());
        //跳转到待处理区
        return "redirect:deal";
    }


    @RequestMapping("/submit")
    public String submit(int id){
        claimVoucherBiz.submit(id);
        return "redirect:deal";
    }


    @RequestMapping("/to_check")
    public String toCheck(int id,Map<String,Object> map){
        //查看哪个报销单信息，我才传递到页面
        map.put("claimVoucher",claimVoucherBiz.get(id));
        map.put("items",claimVoucherBiz.getItems(id));
        map.put("records",claimVoucherBiz.getRecords(id));

        //去这个页面的时候，都是对这个对象设置，
        DealRecord dealRecord=new DealRecord();
        dealRecord.setClaimVoucherId(id);
        map.put("record",dealRecord);
        return "claim_voucher_check";
    }

    @RequestMapping("/check")
    public String check(HttpSession session, DealRecord dealRecord){
        Employee employee = (Employee) session.getAttribute("employee");
       dealRecord.setDealSn(employee.getSn());
       claimVoucherBiz.deal(dealRecord);
        //跳转到待处理区
        return "redirect:deal";
    }


}
