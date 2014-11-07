/**
 * 
 */
package com.liwei.rssread;

import java.util.List;
import java.util.Vector;

/**
 * @author lenovo
 *
 */
public class RSSFeed {

	private String _title = null;
    private String _pubdate = null;
    private String _imageurl=null;
    private int _itemcount = 0;
    private List<RSSItem> _itemlist;
    
    
    RSSFeed()
    {
        _itemlist = new Vector(0); 
    }
    int addItem(RSSItem item)
    {
        _itemlist.add(item);
        _itemcount++;
        return _itemcount;
    }
    RSSItem getItem(int location)
    {
        return _itemlist.get(location);
    }
    List getAllItems()
    {
        return _itemlist;
    }
    int getItemCount()
    {
        return _itemcount;
    }
    void setTitle(String title)
    {
        _title = title;
    }
    public String get_imageurl() {
		return _imageurl;
	}
	public void set_imageurl(String imageurl) {
		_imageurl = imageurl;
	}
	void setPubDate(String pubdate)
    {
        _pubdate = pubdate;
    }
    String getTitle()
    {
        return _title;
    }
    String getPubDate()
    {
        return _pubdate;
    }

}
