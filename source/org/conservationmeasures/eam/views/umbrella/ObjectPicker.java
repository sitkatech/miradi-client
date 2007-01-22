/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.objects.EAMObject;

public interface ObjectPicker
{
	public EAMObject[] getSelectedObjects();
	public void clearSelection();
	public void addListSelectionListener(ListSelectionListener listener);
}
