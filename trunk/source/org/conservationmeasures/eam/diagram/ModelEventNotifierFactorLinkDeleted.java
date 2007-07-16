/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

public class ModelEventNotifierFactorLinkDeleted implements ModelEventNotifier 
{
	public void doNotify(DiagramModelListener listener, DiagramModelEvent event) 
	{
		listener.linkDeleted(event);
	}
}