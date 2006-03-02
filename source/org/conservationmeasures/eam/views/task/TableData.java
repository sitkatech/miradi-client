/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.task;

import java.io.IOException;
import java.util.Vector;

import org.martus.util.UnicodeReader;


public class TableData
{
	public TableData()
	{
		rows = new Vector();
	}
	
	public int rowCount()
	{
		return rows.size();
	}
	
	public int columnCount()
	{
		return header.length;
	}
	
	public String getCellData(int row, int column)
	{
		return ((String[])rows.get(row))[column];
	}
	
	public String getOptionalCellData(int row, int column)
	{
		String[] rowData = (String[])rows.get(row);
		if(column >= rowData.length)
			return "";
		return rowData[column];
	}
	
	public int getCellInteger(int row, int column)
	{
		return Integer.parseInt(getCellData(row, column));
	}
	
	public String getHeader(int col)
	{
		return header[col];
	}
	
	public void loadData(UnicodeReader reader) throws IOException
	{
		rows = new Vector();
		while(true)
		{
			String rowData = reader.readLine();
			if(rowData == null)
				break;
			if(rowData.length() == 0 || rowData.startsWith("#"))
				continue;
			String[] elements = rowData.split(",");
			if(header == null)
				header = elements;
			else
				rows.add(elements);
		}
	}
	
	String[] header;
	Vector rows;
}
