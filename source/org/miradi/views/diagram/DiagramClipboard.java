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
package org.miradi.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import org.miradi.project.Project;

public class DiagramClipboard extends Clipboard
{
	public DiagramClipboard(Project projectToUse)
	{
		super(DiagramClipboard.class.getName());
		project = projectToUse;
		
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	public synchronized int getPasteOffset() 
	{
		return pasteCount * project.getGridSize();
	}

	public synchronized void incrementPasteCount() 
	{
		++pasteCount;
	}
	
	public synchronized void decrementPasteCount()
	{
		--pasteCount;
	}

	private synchronized void resetPasteCount() 
	{
		pasteCount = 0;
	}
	
	public void setContents(Transferable contents, ClipboardOwner owner) 
	{
		resetPasteCount();
		clipboard.setContents(contents, owner);
	}
	
	public Transferable getContents(Object requestor)
	{
		return clipboard.getContents(requestor);
	}
	
	private Project project;
	private int pasteCount;
	private final Clipboard clipboard;
}
