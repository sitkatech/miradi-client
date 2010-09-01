/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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
import java.util.prefs.Preferences;

import javax.swing.filechooser.FileSystemView;

public class FixMiradi
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("This utility will fix the 'missing preferences' problem on Mac and Linux computers.");
		System.out.println("It will create a Java preferences file and set the default Miradi home directory.");
		System.out.println("It is safe to run whether there was a problem or not.");
		System.out.println("If you are not sure you want to do do this, close the terminal window now.");
		System.out.print("To continue, press Enter now...");
		System.in.read();
		File defaultHomeDirectory = getDefaultHomeDirectory();
		System.out.println("Default home directory: " + defaultHomeDirectory);
		Preferences prefsNode = Preferences.userNodeForPackage(FixMiradi.class);
		if(prefsNode == null)
			System.out.println("Missing preferences");
		String storedDirectory = prefsNode.get("MiradiDataDirectory", "");
		System.out.println("Stored Miradi directory: " + storedDirectory);
		if(storedDirectory == null || storedDirectory.length() == 0)
		{
			prefsNode.put("MiradiDataDirectory", defaultHomeDirectory.getAbsolutePath());
			System.out.println("Updated preferences");
		}
		else
		{
			System.out.println("Preferences were already set, so no action was taken.");
		}

	}

	private static File getDefaultHomeDirectory()
	{
		return new File(FileSystemView.getFileSystemView().getDefaultDirectory(), "Miradi");
	}
	
}
