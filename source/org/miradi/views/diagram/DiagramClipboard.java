/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import org.miradi.main.EAM;
import org.miradi.project.Project;

public class DiagramClipboard extends Clipboard
{
	public DiagramClipboard(Project projectToUse)
	{
		super(DiagramClipboard.class.getName());
		project = projectToUse;

		if (project.isInCommandLineMode())
			clipboard = null;
		else
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	public synchronized int getPasteOffset() 
	{
		return pasteCount * project.getOffsetSize();
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
	
	@Override
	public synchronized void setContents(Transferable contents, ClipboardOwner owner) 
	{
		if (project.isInCommandLineMode())
			throw new UnsupportedOperationException(EAM.text("Cannot use clipboard in command line mode"));

		resetPasteCount();
		clipboard.setContents(contents, owner);
	}
	
	@Override
	public synchronized Transferable getContents(Object requestor)
	{
		if (project.isInCommandLineMode())
			throw new UnsupportedOperationException(EAM.text("Cannot use clipboard in command line mode"));

		return clipboard.getContents(requestor);
	}
	
	public synchronized Transferable getContents()
	{
		return getContents(REQUESTOR_WHICH_JAVA_IGNORES);
	}
	
	public boolean isClipboardAvailable()
	{
		try
		{
			if (project.isInCommandLineMode())
				return false;

			getContents();
			
			return true;
		}
		catch (IllegalStateException exceptionIfClipboardInUse)
		{
			EAM.logException(exceptionIfClipboardInUse);
			return false;
		}
	}
	
	private Project project;
	private int pasteCount;
	private final Clipboard clipboard;
	private static final Object REQUESTOR_WHICH_JAVA_IGNORES = null;
}
