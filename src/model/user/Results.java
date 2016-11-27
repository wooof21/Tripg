package model.user;

import java.io.Serializable;

public class Results implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 4L;
    public String username;
    public String CNphoalphabet;
    public String sex;
    public String id;
    public String company_id;
    public String dept_id;
    public String role_id;
    public String passport_type;
    public String passport_no;
    public String birthday;
    public String cellphone;
    public String email;
    public String card_type;
    public String allpoint;
    public String consumpoint;
    public String approved_status;
    public String firstname;
    public String lastname;
    public String company_name;
    public String dept_name;
    public String role_name;
    public Costcenter costcenter;
    public ApprovalUser approval_user;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getCNphoalphabet() {
        return CNphoalphabet;
    }
    public void setCNphoalphabet(String cNphoalphabet) {
        CNphoalphabet = cNphoalphabet;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCompany_id() {
        return company_id;
    }
    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
    public String getDept_id() {
        return dept_id;
    }
    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }
    public String getRole_id() {
        return role_id;
    }
    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
    public String getPassport_type() {
        return passport_type;
    }
    public void setPassport_type(String passport_type) {
        this.passport_type = passport_type;
    }
    public String getPassport_no() {
        return passport_no;
    }
    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getCellphone() {
        return cellphone;
    }
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCard_type() {
        return card_type;
    }
    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }
    public String getAllpoint() {
        return allpoint;
    }
    public void setAllpoint(String allpoint) {
        this.allpoint = allpoint;
    }
    public String getConsumpoint() {
        return consumpoint;
    }
    public void setConsumpoint(String consumpoint) {
        this.consumpoint = consumpoint;
    }
    public String getApproved_status() {
        return approved_status;
    }
    public void setApproved_status(String approved_status) {
        this.approved_status = approved_status;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getCompany_name() {
        return company_name;
    }
    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
    public String getDept_name() {
        return dept_name;
    }
    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }
    public String getRole_name() {
        return role_name;
    }
    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
    public Costcenter getCostcenter() {
        return costcenter;
    }
    public void setCostcenter(Costcenter costcenter) {
        this.costcenter = costcenter;
    }
    public ApprovalUser getApproval_user() {
        return approval_user;
    }
    public void setApproval_user(ApprovalUser approval_user) {
        this.approval_user = approval_user;
    }
    
}
