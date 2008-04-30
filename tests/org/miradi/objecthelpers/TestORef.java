package org.miradi.objecthelpers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;

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
}
