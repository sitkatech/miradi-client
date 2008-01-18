/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

public class ModelEventNotifierFactorLinkAdded implements ModelEventNotifier 
{
	public void doNotify(DiagramModelListener listener, DiagramModelEvent event) 
	{
		listener.linkAdded(event);
	}
}