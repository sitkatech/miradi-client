/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
	
	public void testComment() throws IOException
	{
		Vector rowVector =  loadDelimitedData(new StringReader("# some commented line"));
		assertEquals("did not ignore commented line?", 0, rowVector.size());
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
		Vector rowVector = new DelimitedFileLoader().getDelimitedContents(reader);
		return rowVector;
	}
	

	private void verifyItemData(Vector rowVector, int row, int column, String expected)
	{
		String data = (String)((Vector)rowVector.elementAt(row)).get(column);
		assertEquals(expected, data);
	}
	
}
