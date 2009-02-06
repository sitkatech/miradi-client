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
package org.miradi.objecthelpers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;

public class TestORef extends TestCaseEnhanced
{
	public TestORef(String name)
	{
		super(name);
	}

	public void testStringConstructor() throws Exception
	{
		ORef valid = new ORef(27, new BaseId(15));
		assertEquals(valid, ORef.createFromString(valid.toString()));
		assertEquals(ORef.INVALID, ORef.createFromString(""));
		assertEquals(ORef.INVALID, ORef.createFromString("{\"a\":\"blah\"}"));
		
		PrintStream oldErr = EAM.getExceptionLoggingDestination();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		EAM.setExceptionLoggingDestination(buffer);
		EAM.setLogToString();
		try
		{
			assertEquals(ORef.INVALID, ORef.createFromString("abc"));
			String loggedException = new String(buffer.toByteArray(), "UTF-8");
			assertContains("ParseException", loggedException);
		}
		finally
		{
			EAM.setExceptionLoggingDestination(oldErr);
			EAM.setLogToConsole();
		}
	}
	
	public void testCompareTo()
	{
		ORef smaller = new ORef(4, new BaseId(10));
		ORef biggerByType = new ORef(5, new BaseId(10));
		ORef biggerById = new ORef(4, new BaseId(11));
		ORef same = new ORef(4, new BaseId(10));
		
		assertEquals("not bigger by type?", -1, smaller.compareTo(biggerByType));
		assertEquals("not bigger by id?", -1, smaller.compareTo(biggerById));
		assertEquals("not same?", 0, same.compareTo(same));
		assertEquals("not smaller by type?", 1, biggerByType.compareTo(smaller));
		assertEquals("not smaller by id?", 1, biggerById.compareTo(smaller));
	}
	
	public void testEnsureType()
	{
		ORef ref = new ORef(Cause.getObjectType(), new BaseId(4));
		try
		{
			ref.ensureType(Cause.getObjectType());
		}
		catch (Exception e)
		{
			fail("wrong type for ref?");
		}
		
		try
		{
			ref.ensureType(Target.getObjectType());
			fail("was suppose to fail due to wrong type?");
		}
		catch (Exception e)
		{		
		}
	}
	
	public void testCreateInvalidWithType()
	{
		ORef invalidRef = ORef.createInvalidWithType(Cause.getObjectType());
		assertEquals("wrong invalid type?", Cause.getObjectType(), invalidRef.getObjectType());
	}
}
