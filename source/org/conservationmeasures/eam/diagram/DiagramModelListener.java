/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import java.util.EventListener;

public interface DiagramModelListener extends EventListener 
{
	public void nodeAdded(DiagramModelEvent event);
	public void nodeChanged(DiagramModelEvent event);
	public void nodeDeleted(DiagramModelEvent event);
}
