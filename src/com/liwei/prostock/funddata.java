package com.liwei.prostock;

import java.io.Serializable;

public class funddata implements Serializable{
	public String getFundid() {
		return fundid;
	}
	public void setFundid(String fundid) {
		this.fundid = fundid;
	}
	public String getFundname() {
		return fundname;
	}
	public void setFundname(String fundname) {
		this.fundname = fundname;
	}
	public String getFundzxjz() {
		return fundzxjz;
	}
	public void setFundzxjz(String fundzxjz) {
		this.fundzxjz = fundzxjz;
	}
	public String getFundljjz() {
		return fundljjz;
	}
	public void setFundljjz(String fundljjz) {
		this.fundljjz = fundljjz;
	}
	public String getFundzrjz() {
		return fundzrjz;
	}
	public void setFundzrjz(String fundzrjz) {
		this.fundzrjz = fundzrjz;
	}
	public String getFundzf() {
		return fundzf;
	}
	public void setFundzf(String fundzf) {
		this.fundzf = fundzf;
	}

	private String fundid;
	private String fundname;
	private String fundzxjz;
	private String  fundljjz;
	private String fundzrjz;
	private String fundzf;
	public String getFundrq() {
		return fundrq;
	}
	public void setFundrq(String fundrq) {
		this.fundrq = fundrq;
	}

	private String fundrq;
}
