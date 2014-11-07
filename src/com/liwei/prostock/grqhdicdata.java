package com.liwei.prostock;

class Jys{
	public int getJysid() {
		return jysid;
	}
	public void setJysid(int jysid) {
		this.jysid = jysid;
	}
	public String getJysname() {
		return jysname;
	}
	public void setJysname(String jysname) {
		this.jysname = jysname;
	}
	int jysid;
	String jysname;
}
class Pz{
	public int getPzid() {
		return pzid;
	}
	public void setPzid(int pzid) {
		this.pzid = pzid;
	}
	public int getJysid() {
		return jysid;
	}
	public void setJysid(int jysid) {
		this.jysid = jysid;
	}
	public String getPzname() {
		return pzname;
	}
	public void setPzname(String pzname) {
		this.pzname = pzname;
	}
	int pzid;
	int jysid;
	String pzname;
}
class Jtqh{
	public int getJtqhid() {
		return jtqhid;
	}
	public void setJtqhid(int jtqhid) {
		this.jtqhid = jtqhid;
	}
	public int getPzid() {
		return pzid;
	}
	public void setPzid(int pzid) {
		this.pzid = pzid;
	}
	public String getJtqhdm() {
		return jtqhdm;
	}
	public void setJtqhdm(String jtqhdm) {
		this.jtqhdm = jtqhdm;
	}
	public String getJtqhname() {
		return jtqhname;
	}
	public void setJtqhname(String jtqhname) {
		this.jtqhname = jtqhname;
	}
	int jtqhid;
	int pzid;
	String jtqhdm;
	String jtqhname;
}
