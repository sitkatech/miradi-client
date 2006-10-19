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

public class TestProcessDelimitedFile extends EAMTestCase
{

	public TestProcessDelimitedFile(String name)
	{
		super(name);
	}

	public void testGetCVSFile() throws Exception
	{
		testAssertNoData("");
		testAssertOneElement("xxxx");
		testAssertTwoElement("xxxx \t yyyy");
		testAssertTwoElementOneQouted("xxxx \t     \"xxxx\"");
		testAssertTwoElementOneQoutedEmpty("xxxx \t     \"\"");
		testAssertThreatFileInMain("ThreatTaxonomies.txt");
	}


	private void testAssertNoData(String test) throws IOException
	{
		BufferedReader reader = new BufferedReader( new StringReader(test) );
		Vector myData = DelimitedFileLoader.getDelimitedContents(reader);
		assertEquals(true, myData.size()==0);
	}
	
	private void testAssertOneElement(String test) throws IOException
	{
		BufferedReader reader = new BufferedReader( new StringReader(test) );
		Vector myData = DelimitedFileLoader.getDelimitedContents(reader);
		assertEquals(true, ((Vector)myData.elementAt(0)).size()==1);
	}
	
	private void testAssertTwoElement(String test) throws IOException
	{
		BufferedReader reader = new BufferedReader( new StringReader(test) );
		Vector myData = DelimitedFileLoader.getDelimitedContents(reader);
		assertEquals(2, ((Vector)myData.elementAt(0)).size());
	}
	
	private void testAssertTwoElementOneQouted(String test) throws IOException
	{
		BufferedReader reader = new BufferedReader( new StringReader(test) );
		Vector myData = DelimitedFileLoader.getDelimitedContents(reader);
		assertEquals(2, ((Vector)myData.elementAt(0)).size());
	}
	
	private void testAssertTwoElementOneQoutedEmpty(String test) throws IOException
	{
		BufferedReader reader = new BufferedReader( new StringReader(test) );
		Vector myData = DelimitedFileLoader.getDelimitedContents(reader);
		assertEquals(2, ((Vector)myData.elementAt(0)).size());
	}
	
	private void testAssertThreatFileInMain(String file) throws Exception
	{
		Vector myData = DelimitedFileLoader.getDelimitedContents(file);
		assertEquals(41, myData.size());
	}
	
	
}
