/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland  
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
