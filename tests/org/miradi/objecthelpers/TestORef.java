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
}
