/*
 * Copyright 2006, The Conservation Measures Partnership & Beneficent
 * Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.miradi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import org.miradi.main.EAMTestCase;
import org.miradi.utils.DelimitedFileLoader;

public class TestDelimitedFileLoader extends EAMTestCase
{

	public TestDelimitedFileLoader(String name)
	{
		super(name);
	}

	public void testNoData() throws IOException
	{
		Vector rowVector = loadDelimitedData(new StringReader(""));
		assertEquals(0, rowVector.size());
	}

	public void testOneElement() throws IOException
	{
		Vector rowVector =  loadDelimitedData(new StringReader("xxxx"));
		assertEquals(1, ((Vector)rowVector.elementAt(0)).size());
	}
	
	public void testTwoElement() throws IOException
	{
		Vector rowVector =  loadDelimitedData(new StringReader("xxxx \t yyyy"));
		assertEquals(2, ((Vector)rowVector.elementAt(0)).size());
	}
	
	public void testTwoElementOneQouted() throws IOException
	{
		Vector rowVector =  loadDelimitedData(new StringReader("xxxx \t     \"xxxx\""));
		assertEquals(2, ((Vector)rowVector.elementAt(0)).size());
	}
	
	public void testTwoElementOneQoutedEmpty() throws IOException
	{
		Vector rowVector =  loadDelimitedData(new StringReader("xxxx \t     \"\""));
		assertEquals(2, ((Vector)rowVector.elementAt(0)).size());
	}
	
	public void testElementContentWithTrim() throws IOException
	{
		Vector rowVector =  loadDelimitedData(new StringReader("xxxx \t     \"abcd 123\"   "));
		verifyItemData(rowVector, 0, 1, "abcd 123");
		rowVector =  loadDelimitedData(new StringReader("   xxxx   \t     \"abcd 123\"   "));
		verifyItemData(rowVector, 0, 0, "xxxx");
	}
	
	public void testElementTwoRows() throws IOException
	{
		Vector rowVector =  loadDelimitedData(new StringReader("124 \t     \"abcd 234\"    \n 345 \t     \"efgc 678\"  "));
		assertEquals(2, rowVector.size());
		assertEquals(2, ((Vector)rowVector.get(0)).size());
		assertEquals(2, ((Vector)rowVector.get(1)).size());
	}
	
	public void testElementTwoContent() throws IOException
	{
		Vector rowVector =  loadDelimitedData(new StringReader("124 \t     \"abcd 234\"    \n 345 \t     \"efgc 678\"  "));
		verifyItemData(rowVector, 0, 1, "abcd 234");
		verifyItemData(rowVector, 1, 1, "efgc 678");
	}
	
	private Vector loadDelimitedData(StringReader data) throws IOException
	{
		BufferedReader reader = new BufferedReader( data );
		Vector rowVector = DelimitedFileLoader.getDelimitedContents(reader);
		return rowVector;
	}
	

	private void verifyItemData(Vector rowVector, int row, int column, String expected)
	{
		String data = (String)((Vector)rowVector.elementAt(row)).get(column);
		assertEquals(expected, data);
	}
	
}
