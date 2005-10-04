/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.interview.elements;

import java.util.Vector;

import org.martus.swing.UiList;

public class ListElementData extends ElementData 
{
	
	public ListElementData(String fieldNameToUse)
	{
		fieldName = fieldNameToUse;
		list = new Vector();
	}
	
	public boolean isEmpty()
	{
		return false;
	}
	
	public boolean hasData()
	{
		return true;
	}
	
	public String toString()
	{
		return "<<list>>";
	}

	public void appendLine(String text)
	{
		list.add(text);
	}
	
	public String getFieldName()
	{
		return fieldName;
	}
	
	public String getFieldData()
	{
		UiList uiList = getListComponent();
		String data = (String)uiList.getSelectedValue();
		if(data == null)
			data = "";
		return data;
	}

	public void setFieldData(String newData)
	{
		getListComponent().setSelectedValue(newData, true);
	}

	public void createComponent()
	{
		UiList field = new UiList(list);
		component = field;
	}
	
	public Vector getList()
	{
		return list;
	}
	
	private UiList getListComponent()
	{
		return (UiList)getComponent();
	}
	
	String fieldName;
	Vector list;
}
