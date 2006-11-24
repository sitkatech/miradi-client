/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import java.util.EventListener;

public interface DiagramModelListener extends EventListener 
{
	public void factorAdded(DiagramModelEvent event);
	public void factorChanged(DiagramModelEvent event);
	public void factorDeleted(DiagramModelEvent event);
	public void factorMoved(DiagramModelEvent event);
	public void linkAdded(DiagramModelEvent event);
	public void linkDeleted(DiagramModelEvent event);
}
