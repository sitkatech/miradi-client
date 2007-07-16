package org.conservationmeasures.eam.objecthelpers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.martus.util.TestCaseEnhanced;

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
