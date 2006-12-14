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
	public DiagramClipboard(String name)
	{
		super(name);
	}
	
	public synchronized int getPasteOffset() 
	{
		return count * Project.DEFAULT_GRID_SIZE;
	}

	private synchronized int incrementCount() 
	{
		return count = count+1;
	}
	

	private synchronized int resetCount() 
	{
		return count = 0;
	}
	
	public void setContents(Transferable contents, ClipboardOwner owner) 
	{
		resetCount();
		clipboard.setContents(contents, owner);
	}
	
	public Transferable getContents(Object requestor)
	{
		incrementCount() ;
		return clipboard.getContents(requestor);
	}
	
	private int count = 0;
	final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	final public static DiagramClipboard EMA_CLIPBOARD = new DiagramClipboard("DUMMY");
	
}
