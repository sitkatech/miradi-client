/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.xml.conpro.importer;

import org.miradi.main.TestCaseWithProject;

public class TestConProXmlImporter extends TestCaseWithProject
{
	public TestConProXmlImporter(String name)
	{
		super(name);
	}
	
	public void testImportConProProject() throws Exception
	{
//		getProject().populateEverything();
//		File beforeXmlOutFile = createTempFileFromName("conproBeforeImport.xml");
//		
//		new ConproXmlExporter(getProject()).export(beforeXmlOutFile);
//		String beforeImportAsString = convertFileContentToString(beforeXmlOutFile);
//		Project projectCreatedFromImport = new ConProXmlImporter().populateProjectFromFile(beforeXmlOutFile);
//		
//		File afterXmlOutFile = createTempFileFromName("conproAfterImport.xml");
//		new ConproXmlExporter(projectCreatedFromImport).export(afterXmlOutFile);
//		String afterImportAsString = convertFileContentToString(afterXmlOutFile);
//		
//		assertEquals("incorrect project values after import?", beforeImportAsString, afterImportAsString);
	}
	
//	private String convertFileContentToString(File fileToConvert) throws Exception
//	{
//	    StringBuffer stringBuffer = new StringBuffer();
//	    BufferedReader in = new BufferedReader(new FileReader(fileToConvert));
//		try 
//		{
//	        String str;
//	        while ((str = in.readLine()) != null) 
//	        {
//	        	stringBuffer.append(str);
//	        }
//	    } 
//		finally
//		{
//			in.close();
//		}
//		
//		return stringBuffer.toString();
//	}
}
