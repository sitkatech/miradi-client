/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import org.conservationmeasures.eam.project.Project;

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
