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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardLocker
{
	public static void main(String[] args) throws Exception
	{
		String somelargeString = "";
		somelargeString += "123456789";

		System.out.println("About to start locking the clipboard");
		System.out.println("running...");
		for (int index = 0; index < 100000000; ++index)
		{
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection transferable = new StringSelection(somelargeString);
			clipboard.setContents(transferable, transferable);
			/*Transferable content =*/ clipboard.getContents(null);
			Thread.sleep(5);
			if(index % 200 == 0)
//				System.out.println();
				System.out.print(".");
			System.out.flush();
		}
		System.out.println("end");
	}

}
