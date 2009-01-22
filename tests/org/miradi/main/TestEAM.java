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
package org.miradi.main;

import java.io.File;

public class TestEAM extends EAMTestCase
{
	public TestEAM(String name)
	{
		super(name);
	}

	public void testSubstitude()
	{
		String beforeSubstitute = "some text with %s";
		String substituteString = "some more text";
		String substitudedText = EAM.substitute(beforeSubstitute, substituteString);
		String expectedText = "some text with some more text";
		assertEquals("didnt substitude correctly?", expectedText, substitudedText); 
	}
	
	public void testIsOneFileInsideTheOther()
	{
		File homeDir = new File("C:\\Users\\Nima\\Documents\\Miradi");
		File file1 = new File("C:\\Users\\Nima\\Documents\\Miradi\\somefile.txt");
		assertTrueFileInsideOther(homeDir, file1);
		assertTrueFileInsideOther(file1, homeDir);
		
		assertTrueFileInsideOther(homeDir, homeDir);
		
		File file2 = new File("C:\\Users\\Nima\\Documents\\somefile.txt");
		assertFalseFileInsideOther(homeDir, file2);
		assertFalseFileInsideOther(file2, homeDir);
	}

	private void assertTrueFileInsideOther(File file1, File file2)
	{		
		assertTrue("file inside other folder", EAM.isOneFileInsideTheOther(file1, file2));
	}
	
	private void assertFalseFileInsideOther(File file1, File file2)
	{		
		assertFalse("file is not inside other folder", EAM.isOneFileInsideTheOther(file1, file2));
	}
}
