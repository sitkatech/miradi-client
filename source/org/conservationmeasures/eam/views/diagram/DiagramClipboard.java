/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import org.conservationmeasures.eam.project.Project;

public class DiagramClipboard extends Clipboard
{
	public DiagramClipboard()
	{
		super(DiagramClipboard.class.getName());
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	public synchronized int getPasteOffset() 
	{
		return pasteCount * Project.DEFAULT_GRID_SIZE;
	}

	private synchronized int incrementPasteCount() 
	{
		return pasteCount = pasteCount+1;
	}
	

	private synchronized int resetPasteCount() 
	{
		return pasteCount = 0;
	}
	
	public void setContents(Transferable contents, ClipboardOwner owner) 
	{
		resetPasteCount();
		clipboard.setContents(contents, owner);
	}
	
	public Transferable getContents(Object requestor)
	{
		incrementPasteCount() ;
		return clipboard.getContents(requestor);
	}
	
	private int pasteCount;
	private final Clipboard clipboard;
	final public static DiagramClipboard EAM_CLIPBOARD = new DiagramClipboard();
	
}
