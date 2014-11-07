package com.liwei.message;

import java.util.Date;

public class messagedata {
	/**
	 * 
	 */
	int messageid;
	String stockid="";
	int stocktj=0;
	int stockdx=0;
	float stockvalue=0;
	boolean stockbool=false;
	Date stocktime;
	public int getMessageid() {
		return messageid;
	}
	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}
	public String getStockid() {
		return stockid;
	}
	public void setStockid(String stockid) {
		this.stockid = stockid;
	}
	public int getStocktj() {
		return stocktj;
	}
	public void setStocktj(int stocktj) {
		this.stocktj = stocktj;
	}
	public int getStockdx() {
		return stockdx;
	}
	public void setStockdx(int stockdx) {
		this.stockdx = stockdx;
	}
	public float getStockvalue() {
		return stockvalue;
	}
	public void setStockvalue(float stockvalue) {
		this.stockvalue = stockvalue;
	}
	public boolean isStockbool() {
		return stockbool;
	}
	public void setStockbool(boolean stockbool) {
		this.stockbool = stockbool;
	}
	public Date getStocktime() {
		return stocktime;
	}
	public void setStocktime(Date stocktime) {
		this.stocktime = stocktime;
	}
	
	
	
}
