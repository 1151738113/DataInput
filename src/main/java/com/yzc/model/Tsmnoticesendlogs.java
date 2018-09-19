package com.yzc.model;

import java.util.Date;

/**
 * Created by wei.wang on 2018/8/21 0021.
 */

//@Entity
//@Table(name = "Tsmnoticesendlogs")
public class Tsmnoticesendlogs {

//    @Id
    private String noticesendlogsid;

    private Integer businesstype;

    private String modulecode;

    private String modulename;

    private String memberlevel;

    private Integer sendtype;

    private String receiver;

    private String receivername;

    private String messagetitle;

    private String messagecontent;

    private String readurl;

    private String skipurl;

    private String companyid;

    private String companyname;

    private String receivecompanyid;

    private String receivecompanyname;

    private Date sendtime;

    private Integer isdel;

    private Integer isread;

    public String getNoticesendlogsid() {
        return noticesendlogsid;
    }

    public void setNoticesendlogsid(String noticesendlogsid) {
        this.noticesendlogsid = noticesendlogsid;
    }


    public String getModulecode() {
        return modulecode;
    }

    public void setModulecode(String modulecode) {
        this.modulecode = modulecode;
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public String getMemberlevel() {
        return memberlevel;
    }

    public void setMemberlevel(String memberlevel) {
        this.memberlevel = memberlevel;
    }

    public Integer getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(Integer businesstype) {
        this.businesstype = businesstype;
    }

    public Integer getSendtype() {
        return sendtype;
    }

    public void setSendtype(Integer sendtype) {
        this.sendtype = sendtype;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public String getMessagetitle() {
        return messagetitle;
    }

    public void setMessagetitle(String messagetitle) {
        this.messagetitle = messagetitle;
    }

    public String getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent;
    }

    public String getReadurl() {
        return readurl;
    }

    public void setReadurl(String readurl) {
        this.readurl = readurl;
    }

    public String getSkipurl() {
        return skipurl;
    }

    public void setSkipurl(String skipurl) {
        this.skipurl = skipurl;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getReceivecompanyid() {
        return receivecompanyid;
    }

    public void setReceivecompanyid(String receivecompanyid) {
        this.receivecompanyid = receivecompanyid;
    }

    public String getReceivecompanyname() {
        return receivecompanyname;
    }

    public void setReceivecompanyname(String receivecompanyname) {
        this.receivecompanyname = receivecompanyname;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

}
