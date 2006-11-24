/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

public class ModelEventNotifierFactorLinkAdded implements ModelEventNotifier 
{
	public void doNotify(DiagramModelListener listener, DiagramModelEvent event) 
	{
		listener.linkageAdded(event);
	}
}