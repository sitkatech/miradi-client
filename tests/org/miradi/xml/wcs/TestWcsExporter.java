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

package org.miradi.xml.wcs;

import org.miradi.main.TestCaseWithProject;

public class TestWcsExporter extends TestCaseWithProject
{
	public TestWcsExporter(String name)
	{
		super(name);
	}

 //FIXME urgent - wcs exporter test commented out 
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		getProject().populateEverything(); 
	}
	
	
	public void testValidate() throws Exception
	{
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//		UnicodeWriter writer = new UnicodeWriter(bytes);
//		new WcsXmlExporter(getProject()).exportProject(writer);
//		writer.close();
//		String xml = new String(bytes.toByteArray(), "UTF-8");
//		
//		System.out.println("TMP OUTPUT = " + xml);
//		InputStreamWithSeek inputStream = new StringInputStreamWithSeek(xml);
//		if (!new WcsMiradiXmlValidator().isValid(inputStream))
//		{
//			throw new ValidationException(EAM.text("File to import does not validate."));
//		}	
	}
}
