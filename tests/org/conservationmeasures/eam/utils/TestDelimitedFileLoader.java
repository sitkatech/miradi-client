/*
 * Copyright 2006, The Conservation Measures Partnership & Beneficent
 * Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDelimitedFileLoader extends EAMTestCase
{

	public TestDelimitedFileLoader(String name)
	{
		super(name);
	}

	public void testNoData() throws IOException
	{
		Vector myData = loadDelimitedData(new StringReader(""));
		assertEquals(true, myData.size()==0);
	}

	public void testOneElement() throws IOException
	{
		Vector myData =  loadDelimitedData(new StringReader("xxxx"));
		assertEquals(true, ((Vector)myData.elementAt(0)).size()==1);
	}
	
	public void testTwoElement() throws IOException
	{
		Vector myData =  loadDelimitedData(new StringReader("xxxx \t yyyy"));
		assertEquals(2, ((Vector)myData.elementAt(0)).size());
	}
	
	public void testTwoElementOneQouted() throws IOException
	{
		Vector myData =  loadDelimitedData(new StringReader("xxxx \t     \"xxxx\""));
		assertEquals(2, ((Vector)myData.elementAt(0)).size());
	}
	
	public void testTwoElementOneQoutedEmpty() throws IOException
	{
		Vector myData =  loadDelimitedData(new StringReader("xxxx \t     \"\""));
		assertEquals(2, ((Vector)myData.elementAt(0)).size());
	}
	
	public void testElementContentWithTrim() throws IOException
	{
		Vector myData =  loadDelimitedData(new StringReader("xxxx \t     \"abcd 123\"   "));
		testItem(myData, 0, 1, "abcd 123");
	}
	
	public void testElementTwoRows() throws IOException
	{
		Vector myData =  loadDelimitedData(new StringReader("124 \t     \"abcd 234\"    \n 345 \t     \"efgc 678\"  "));
		assertEquals(2, myData.size());
	}
	
	public void testElementTwoContent() throws IOException
	{
		Vector myData =  loadDelimitedData(new StringReader("124 \t     \"abcd 234\"    \n 345 \t     \"efgc 678\"  "));
		testItem(myData, 0, 1, "abcd 234");
		testItem(myData, 1, 1, "efgc 678");
	}
	
	private Vector loadDelimitedData(StringReader data) throws IOException
	{
		BufferedReader reader = new BufferedReader( data );
		Vector myData = DelimitedFileLoader.getDelimitedContents(reader);
		return myData;
	}
	

	private void testItem(Vector myData, int temp0, int temp1, String tempData)
	{
		String data = (String)((Vector)myData.elementAt(temp0)).get(temp1);
		assertEquals(tempData, data);
	}
	
}
