/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import org.martus.util.MultiCalendar;
import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.main.EAM;

public class FileUtilities
{
	public static HashSet<String> getAllRecursiveChildrenPaths(File startDirectory)
	{
		return getAllRecursiveChildrenPaths(startDirectory, startDirectory);
	}
	
	private static HashSet<String> getAllRecursiveChildrenPaths(File baseDirectory, File topLevelDir)
	{
		File[] childrenFiles = topLevelDir.listFiles();
		if (childrenFiles == null)
			return new HashSet<String>();

		HashSet<String> allChildrenPaths = new HashSet<String>();
		for (int index = 0; index < childrenFiles.length; ++index)
		{
			File childFile = childrenFiles[index];
			if (childFile.isDirectory())
			{
				allChildrenPaths.addAll(getAllRecursiveChildrenPaths(baseDirectory, childFile));
			}
			else
			{
				String relativeChildPath = childFile.getAbsolutePath().substring(baseDirectory.getAbsolutePath().length());
				allChildrenPaths.add(relativeChildPath);
			}
		}
		
		return allChildrenPaths;
	}
	
	public static void copyStream(InputStream inputStream, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int got = -1;
		while( (got = inputStream.read(buffer)) > 0)
		{
			out.write(buffer, 0, got);
		}
	}

	public static void copyFile(File sourceFile, File destinationFile) throws Exception
	{
		FileInputStream fileInputStream = new FileInputStream(sourceFile);
		try
		{
			copyStreamToFile(fileInputStream, destinationFile);
		}
		finally
		{
			fileInputStream.close();
		}
	}
	
	public static void copyStreamToFile(InputStream inputStream, File destinationFile) throws IOException
	{
		FileOutputStream out = new FileOutputStream(destinationFile);
		try
		{
			copyStream(inputStream, out);
		}
		finally
		{
			out.close();
		}
	}

	public static File createTempDirectory(String nameHint) throws IOException
	{
		File tempDirectory = File.createTempFile("$$$" + nameHint, null);
		tempDirectory.deleteOnExit();
		tempDirectory.delete();
		tempDirectory.mkdir();
		return tempDirectory;
	}
	
	public static void deleteIfExistsWithRetries(final File fileToDelete)	throws Exception
	{
		if(fileDoesNotExist(fileToDelete))
			return;

		deleteExistingWithRetries(fileToDelete);
	}

	public static void deleteExistingWithRetries(final File fileToDelete) throws Exception
	{
		final int MAX_TRIES = 4;
		final int SLEEP_PER_TRY_MILLIS = 250;
		deleteExistingWithRetries(fileToDelete, MAX_TRIES, SLEEP_PER_TRY_MILLIS);
	}

	private static void deleteExistingWithRetries(final File fileToDelete, final int maxTries, final int sleepPerTryMillis) throws Exception
	{
		if (fileDoesNotExist(fileToDelete))
			throw new IOException("Must pass in a file that exists, file:" + fileToDelete.getAbsolutePath());

		for (int retryCount = 0; retryCount < maxTries; ++retryCount)
		{
			fileToDelete.delete();
			if (fileDoesNotExist(fileToDelete))
				return;

			Thread.currentThread().sleep(sleepPerTryMillis);
		}
		
		throw new IOException("Delete failed for file:" + fileToDelete.getAbsolutePath());
	}
	
	public static void renameIfExistsWithRetries(final File fromFile, final File toFile)	throws Exception
	{
		if(fileDoesNotExist(fromFile))
			return;

		renameExistingWithRetries(fromFile, toFile);
	}

	public static void renameExistingWithRetries(final File fromFile, final File toFile) throws Exception
	{
		final int MAX_TRIES = 4;
		final int SLEEP_PER_TRY_MILLIS = 250;
		renameExistingWithRetries(fromFile, toFile, MAX_TRIES, SLEEP_PER_TRY_MILLIS);
	}

	private static void renameExistingWithRetries(final File fromFile, final File toFile, final int maxTries, final int sleepPerTryMillis) throws Exception
	{
		if (fileDoesNotExist(fromFile))
			throw new IOException("Must pass in a file that exists, file:" + fromFile.getAbsolutePath());

		for (int retryCount = 0; retryCount < maxTries; ++retryCount)
		{
			if (fromFile.renameTo(toFile))
				return;

			Thread.currentThread().sleep(sleepPerTryMillis);
		}
		
		throw new IOException("Rename failed for file:" + fromFile.getAbsolutePath());
	}

	public static boolean fileDoesNotExist(final File fileToDelete)
	{
		return !fileToDelete.exists();
	}
	
	public static File getFileWithSuffix(File fromFile, String withSuffix)
	{
		return new File(fromFile.getAbsolutePath() + withSuffix);
	}

	public static File createTempFileCopyOf(InputStream mpzInputStream) throws IOException
	{
		File temporaryFile = File.createTempFile("$$$tempFileCopy", null);
		temporaryFile.deleteOnExit();
		copyStreamToFile(mpzInputStream, temporaryFile);
		
		return temporaryFile;
	}
	
	public static boolean compareDirectoriesBasedOnFileNames(final File dir1, final File dir2)
	{
		HashSet<String> childrenPathsFromDir1 = getAllRecursiveChildrenPaths(dir1);
		HashSet<String> childrenPathsFromDir2 = getAllRecursiveChildrenPaths(dir2);
		if (!childrenPathsFromDir1.containsAll(childrenPathsFromDir2))
			return false;
		
		if (!childrenPathsFromDir2.containsAll(childrenPathsFromDir1))
			return false;
		
		return true;
	}
	
	public static String join(String parentPath, String childPath) throws Exception
	{
		final String normalizedParentPath = removeTrailingForwardSlashes(parentPath);
		final String normalizedChildPath = removeLeadingForwardSlashes(childPath);
		final String joinedPath = normalizedParentPath + SEPARATOR + normalizedChildPath;
		return joinedPath;
	}

	private static String removeTrailingForwardSlashes(String path) throws Exception
	{
		if (path.endsWith(SEPARATOR))
			return StringUtilities.stripTrailingString(path, SEPARATOR);
		
		return path;
	}
	
	private static String removeLeadingForwardSlashes(String path) throws Exception
	{
		if (path.startsWith(SEPARATOR))
			return path.replaceFirst(SEPARATOR, "");
		
		return path;
	}
	
	public static String createFileNameWithExtension(String name, String extension)
	{
		if (!extension.startsWith("."))
			 extension = "." + extension;

		return name + extension;
	}
	
	public static void createMpfBackup(File projectFile, final String backupFolderName) throws Exception
	{
		long timeOfBackup = System.currentTimeMillis();
		File backup = new File(getBackupFolder(backupFolderName), "backup-" + projectFile.getName() + "-" + timeOfBackup + AbstractMpfFileFilter.EXTENSION);
		if (backup.exists())
			throw new Exception("Overriding older backup");
		
		FileUtilities.copyFile(projectFile, backup);
	}

	private static File getBackupFolder(String backupFolderName) throws Exception
	{
		File backupFolder = new File(EAM.getHomeDirectory(), backupFolderName);
		File backupSubFolder = new File (backupFolder, getSubfolderName());
		backupSubFolder.mkdirs();
		
		return backupSubFolder;
	}
	
	private static String getSubfolderName()
	{
		MultiCalendar calendar = new MultiCalendar();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String subForlderName = dateFormat.format(calendar.getTime());
		
		return subForlderName;
	}

	public static final String SEPARATOR = "/";
	public static final String REGULAR_EXPRESSION_BACKSLASH = "\\\\";
}
