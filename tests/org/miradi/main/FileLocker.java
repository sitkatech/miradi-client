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

package org.miradi.main;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLocker
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("FileLocker <filetolock>");
		String filename = args[0];
		File file = new File(filename);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel channel = raf.getChannel();
		FileLock lock = channel.tryLock();
		if(lock == null)
			throw new RuntimeException("Unable to create the lock");
		System.out.println("File " + file + " is locked at " + lock.position() + " for " + lock.size() + " bytes");
		System.out.print("Press Enter to exit.");
		System.in.read();
		lock.release();
		raf.close();
		System.out.println("Unlocked, closed, and exiting");
	}
}
