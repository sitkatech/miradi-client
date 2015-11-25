/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.martus.util.UnicodeStringReader;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.ResourcesHandler;
import org.miradi.main.TestCaseWithProject;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.MpfToMpzConverter;
import org.miradi.utils.NullProgressMeter;

public class TestMpzToMpfConverter extends TestCaseWithProject
{
	public TestMpzToMpfConverter(String name)
	{
		super(name);
	}
	
	public void testGetExceptionsLog() throws Exception
	{
		verifyExceptionsLog(Deflater.DEFAULT_COMPRESSION);
		verifyExceptionsLog(Deflater.NO_COMPRESSION);
	}

	private void verifyExceptionsLog(final int compressionMethod) throws Exception
	{
		verifyExceptionsLog(20000, 40000, compressionMethod);
		verifyExceptionsLog(20000, 20000, compressionMethod);
		verifyExceptionsLog(10000, 10000, compressionMethod);
	}

	private void verifyExceptionsLog(final int expectedExceptionsLength, final int exceptionsLenthToCreate, final int compressionMethod) throws Exception
	{
		final File file = File.createTempFile("$$$tempExceptionsLogZipFile", null);
		final FileOutputStream fileOutputStream = new FileOutputStream(file);
		String sampleException = createSampleExceptionsLogUpToLenth(exceptionsLenthToCreate);
		writeZipStream(fileOutputStream, sampleException.toString(), compressionMethod);
		MiradiZipFile miradiZipFile = new MiradiZipFile(file);
		try
		{
			ZipEntry exceptionsLogEntry = miradiZipFile.getEntry("Exceptions.log");
			String exceptionsFromZip = MpzToMpfConverter.getExceptionsLog(miradiZipFile, exceptionsLogEntry);
			assertEquals("incorrect unzipped exceptions lenth?", expectedExceptionsLength, exceptionsFromZip.getBytes("UTF-8").length);
		}
		finally
		{
			miradiZipFile.close();
			fileOutputStream.flush();
			fileOutputStream.close();
			FileUtilities.deleteIfExistsWithRetries(file);
		}
	}

	private String createSampleExceptionsLogUpToLenth(final int maxExceptionsLenth) throws Exception
	{
		StackTraceElement[] element = new RuntimeException().getStackTrace();
		StringBuffer sampleException = new StringBuffer();
		for (int stackCount = 0; stackCount < element.length; ++stackCount)
		{
			sampleException.append("\t");
			sampleException.append(element[stackCount]);
			sampleException.append("\n"); 
		}
		
		StringBuffer sampleExceptionUpToMaxLength = new StringBuffer();
		while (sampleExceptionUpToMaxLength.length() < maxExceptionsLenth)
		{
			sampleExceptionUpToMaxLength.append(sampleException);
		}
		
		return sampleExceptionUpToMaxLength.substring(sampleExceptionUpToMaxLength.length() - maxExceptionsLenth);
	}
		
	
	private void writeZipStream(final FileOutputStream fileOutputStream, String sampleValue, final int compressionMethod) throws Exception
	{
		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
		zipOutputStream.setLevel(compressionMethod);
		try
		{
			MpfToMpzConverter.writeZipEntry(zipOutputStream, "Exceptions.log", sampleValue);
		}
		finally
		{
			zipOutputStream.flush();
			zipOutputStream.close();
		}
	}
	
	public void testExtractVersion() throws Exception
	{
		verifyExtractVersion(6);
		verifyExtractVersion(61);
		verifyExtractVersion(62);
	}

	private void verifyExtractVersion(int expectedVersion) throws Exception
	{
		String sampleMpzResourcePath = "/Sample-v" + expectedVersion + ".mpz";
		byte[] mpzBytes = readSampleMpz(sampleMpzResourcePath);
		File mpz = writeToTemporaryFile(mpzBytes);
		try
		{
			MiradiZipFile zipFile = new MiradiZipFile(mpz);
			int version = MpzToMpfConverter.extractVersion(zipFile);
			assertEquals(expectedVersion, version);
		}
		finally
		{
			mpz.delete();
		}
	}
	
	public void testMigrateMpz() throws Exception
	{
		byte[] mpzBytes = readSampleMpz("/MarineExample-2.1.0-v35.mpz");
		File mpz = writeToTemporaryFile(mpzBytes);
		try
		{
			File migratedFile = MpzToMpfConverter.migrate(mpz, new NullProgressMeter());
			MiradiZipFile migratedZipFile = new MiradiZipFile(migratedFile);
			int endingVersion = MpzToMpfConverter.extractVersion(migratedZipFile);
			assertEquals(61, endingVersion);
			migratedZipFile.close();
			migratedFile.delete();
		}
		finally
		{
			mpz.delete();
		}
		
	}
	
	public void testConvertMpzToMpf() throws Exception
	{
		byte[] mpzBytes = readSampleMpz("/Sample-v61.mpz");
		File mpz = writeToTemporaryFile(mpzBytes);
		try
		{
			NullProgressMeter progressIndicator = new NullProgressMeter();
			String convertedProjectString = MpzToMpfConverter.convert(mpz, progressIndicator);
			MigrationManager migrationManager = new MigrationManager();
			RawProject rawProjectToMigrate = RawProjectLoader.loadProject(convertedProjectString);
			migrationManager.migrate(rawProjectToMigrate, Project.getMiradiVersionRange());
			convertedProjectString = RawProjectSaver.saveProject(rawProjectToMigrate);
			// NOTE: For easier debugging
			//System.out.println(convertedProjectString);
			
			ProjectForTesting project2 = createProjectFromDotMiradi(convertedProjectString);
			assertEquals(946, project2.getNormalIdAssigner().getHighestAssignedId());
			
			SimpleThreatRatingFramework simpleThreatRatingFramework = project2.getSimpleThreatRatingFramework();
			Collection<ThreatRatingBundle> bundles = simpleThreatRatingFramework.getAllBundles();
			assertEquals("Didn't convert simple threat ratings?", 31, bundles.size());
			ThreatRatingBundle bundle = simpleThreatRatingFramework.getBundle(new FactorId(41), new FactorId(39));
			assertEquals("Didn't convert actual threat ratings?", new BaseId(6), bundle.getValueId(new BaseId(3)));

			Indicator indicator = Indicator.find(project2, new ORef(IndicatorSchema.getObjectType(), new BaseId(155)));
			CodeToUserStringMap thresholdMap = indicator.getCodeToUserStringMapData(Indicator.TAG_THRESHOLDS_MAP);
			assertEquals("71 - 90%", thresholdMap.getUserString("3"));
			assertNotContains("Didn't XML-encode?", "<", thresholdMap.getUserString("1"));

			//FIXME urgent: Need to spot-check various other items
			// text field with newlines
			// numeric field
			// date field
			// choice field
			// reflist
			// ...
			
			assertContains("non-realistic example", project2.getQuarantineFileContents());
			
			final int expectedSizeAfterTruncationOfSampleException = 20000;
			final String exceptionLog = project2.getExceptionLog();
			assertTrue("Exception log did not get truncated?", exceptionLog.length() <= expectedSizeAfterTruncationOfSampleException);
		}
		finally
		{
			mpz.delete();
		}
	}
	
	public void testSafeConvertUtf8BytesToString() throws Exception
	{
		String test = new String(new char[] {0x20AC, 'T', 'e'});
		byte[] bytes = test.getBytes("UTF-8");
		byte[] withIncompleteAtStart = new byte[bytes.length - 1];
		System.arraycopy(bytes, 1, withIncompleteAtStart, 0, withIncompleteAtStart.length);
		String safe = MpzToMpfConverter.safeConvertUtf8BytesToString(withIncompleteAtStart);
		assertEquals("Te", safe);
	}

	public static byte[] readSampleMpz(String mpzResourcePath) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		URL url = ResourcesHandler.getResourceURL(mpzResourcePath);
		InputStream in = url.openStream();
		try
		{
			byte[] buffer = new byte[1024];
			int got = -1;
			while( (got = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, got);
			}
			out.close();
		}
		finally
		{
			in.close();
		}
		return out.toByteArray();
	}

	private ProjectForTesting createProjectFromDotMiradi(String projectAsStringFromConverter) throws Exception
	{
		ProjectForTesting project2 = ProjectForTesting.createProjectWithDefaultObjects(getName());
		project2.clear();
		UnicodeStringReader reader = new UnicodeStringReader(projectAsStringFromConverter);
		ProjectLoader.loadProject(reader, project2);
		return project2;
	}

	public static File writeToTemporaryFile(byte[] byteArray) throws Exception
	{
		File tempMpzFile = File.createTempFile("$$$TestMpzToMiradiConverter.mpz", null);
		tempMpzFile.deleteOnExit();
		try
		{
			FileOutputStream fileOut = new FileOutputStream(tempMpzFile);
			fileOut.write(byteArray);
			fileOut.flush();
			fileOut.close();
			return tempMpzFile;
		}
		catch(Exception e)
		{
			tempMpzFile.delete();
			throw(e);
		}
	}

}
