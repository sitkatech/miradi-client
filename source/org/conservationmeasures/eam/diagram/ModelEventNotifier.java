/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

public interface ModelEventNotifier 
{
	public void fileAction(DiagramModelListener listener, DiagramModelEvent event);
}
