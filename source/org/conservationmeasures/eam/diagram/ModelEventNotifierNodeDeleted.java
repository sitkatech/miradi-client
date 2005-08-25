/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

public class ModelEventNotifierNodeDeleted implements ModelEventNotifier 
{
	public void fileAction(DiagramModelListener listener, DiagramModelEvent event) 
	{
		listener.nodeDeleted(event);
	}
}