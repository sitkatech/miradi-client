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
import java.util.HashSet;

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
	
	public static void deleteIfExists(File file) throws IOException
	{
		if(!file.exists())
			return;
		
		if(!file.delete())
			throw new IOException("Delete failed: " + file.getAbsolutePath());
	}
	
	public static void renameIfExists(File fromFile, File toFile) throws IOException
	{
		if(!fromFile.exists())
			return;
		
		rename(fromFile, toFile);
	}

	public static void rename(File fromFile, File toFile) throws IOException
	{
		if(!fromFile.renameTo(toFile))
			throw new IOException("Rename failed: " + fromFile.getAbsolutePath() + "->" + toFile.getAbsolutePath());
	}

	public static File getFileWithSuffix(File currentFile, String suffix)
	{
		return new File(currentFile.getAbsolutePath() + suffix);
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
		final String normalizedChildPath = removeTrailingForwardSlashes(childPath);
		final String joinedPath = normalizedParentPath + SEPARATOR + normalizedChildPath;
		return joinedPath;
	}

	private static String removeTrailingForwardSlashes(String path) throws Exception
	{
		if (path.endsWith(SEPARATOR))
			return StringUtilities.stripTrailingString(path, SEPARATOR);
		
		return path;
	}
	
	public static String getSystemSeparator()
	{
		return System.getProperty("file.separator");
	}
	
	private static final String SEPARATOR = "/";
}
