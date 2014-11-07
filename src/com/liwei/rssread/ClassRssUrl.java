/**
 * 
 */
package com.liwei.rssread;

/**
 * @author lenovo
 *
 */
public class ClassRssUrl {

	
	/**
	 * @return the _id
	 */
	
	private int _id;
	private String rssname;
	private String rssurl;
	private byte[] rssimage;
	
	public int get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(int _id) {
		this._id = _id;
	}
	/**
	 * @return the rssname
	 */
	public String getRssname() {
		return rssname;
	}
	/**
	 * @param rssname the rssname to set
	 */
	public void setRssname(String rssname) {
		this.rssname = rssname;
	}
	/**
	 * @return the rssurl
	 */
	public String getRssurl() {
		return rssurl;
	}
	/**
	 * @param rssurl the rssurl to set
	 */
	public void setRssurl(String rssurl) {
		this.rssurl = rssurl;
	}
	/**
	 * @return the rssimage
	 */
	public byte[] getRssimage() {
		return rssimage;
	}
	/**
	 * @param rssimage the rssimage to set
	 */
	public void setRssimage(byte[] rssimage) {
		this.rssimage = rssimage;
	}
	
	
}
