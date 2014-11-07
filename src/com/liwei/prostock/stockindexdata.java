package com.liwei.prostock;

public class stockindexdata {
	private String zsmc;//指数名称
	private String zsds;//点位
	private String zsjg;//涨跌点数
	private String zszdl;//涨跌幅
	private String zscjl;//成交量
	private String zscje;//成交额
	private String zssj;//时间
	public String getZssj() {
		return zssj;
	}
	public void setZssj(String zssj) {
		this.zssj = zssj;
	}
	public String getZsmc() {
		return zsmc;
	}
	public void setZsmc(String zsmc) {
		this.zsmc = zsmc;
	}
	public String getZsds() {
		return zsds;
	}
	public void setZsds(String zsds) {
		this.zsds = zsds;
	}
	public String getZsjg() {
		return zsjg;
	}
	public void setZsjg(String zsjg) {
		this.zsjg = zsjg;
	}
	public String getZszdl() {
		return zszdl;
	}
	public void setZszdl(String zszdl) {
		this.zszdl = zszdl;
	}
	public String getZscjl() {
		return zscjl;
	}
	public void setZscjl(String zscjl) {
		this.zscjl = zscjl;
	}
	public String getZscje() {
		return zscje;
	}
	public void setZscje(String zscje) {
		this.zscje = zscje;
	}
	//displayData(SZ("sh000001")); //上证指数
	//displayData(SZ("sz399001"));   //深证成指
	//displayData(OTHER("int_dji"));//道琼斯
	//displayData(OTHER("int_nasdaq")); //纳斯达克
	//displayData(OTHER("int_hangseng")); //恒生
	//displayData(OTHER("int_nikkei")); 日经指数
	//displayData(OTHER("b_TWSE")); 台湾加权
	//displayData(OTHER("b_FSSTI")); //新加坡
	
}
