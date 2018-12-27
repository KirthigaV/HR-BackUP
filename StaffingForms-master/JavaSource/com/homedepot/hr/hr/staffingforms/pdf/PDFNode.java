/*
 * Created on Sep 16, 2008
* This code is copyright of Home Depot Inc. Any unauthorized use is prohibited
* This program is proprietary to The Home Depot and * is not to be reproduced, 
* used, or disclosed without permission of:
* The Home Depot
* 2455 Paces Ferry Road, N.W.
* Atlanta, GA 30339-4024
* 
* author Chris Stann
*/
package com.homedepot.hr.hr.staffingforms.pdf;



/**
 * @author CSY2MFG
 *
 * PDFNode represents an XML node object
 * 
 */
public class PDFNode
{
	private String _name;
	private StringBuffer _content = new StringBuffer();
	private StringBuffer _attributes = new StringBuffer();
    
	public PDFNode(String name)
	{
		_name = name;
	}
    
	public String getPDFNode()
	{
		if (isEmpty())
			_content.append("<empty/>");
		return "<" + _name + _attributes.toString() + ">" 
				+ _content.toString() 
				+ "</" + _name + ">";
	}
    
	public void addContent(PDFNode rn)
	{
		_content.append(rn.getPDFNode());
	}
    
	public void addContent(String name, String c)
	{ 
		//Handle null names
        if (name == null)return;
        //Handle null values
        if(c != null) {
              c = c.replace("&", "&amp;");
              c = c.replace("<", "&lt;");
              c = c.replace(">", "&gt;");
              c = c.replace("\"", "&quot;");
        }else {
              c = "";
        }
        _content.append("<" + name + ">" + c + "</" + name + ">");

	}


	public void addAttribute(String name, String val)
	{
        //Handle null names
        if (name == null) return;
        //Handle null values
        if(val != null) {
              val = val.replace("\"", "&quot;");
        }else {
              val = "";
        }
        _attributes.append(" " + name + "=\"" + val + "\"");

	}
    
	public void reset()
	{
		_content = null;
		_content = new StringBuffer();
		_attributes = null;
		_attributes = new StringBuffer();
	}
    
	public boolean isEmpty()
	{
		return _content.length() == 0;
	}
    
	public String getName()
	{
		return _name;
	}
    
    
	public String toString()
	{
		return getPDFNode();
	}
}
