/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.TreeTableNode;

public interface ObjectPicker
{
	public EAMObject[] getSelectedObjects();
	public void clearSelection();
	//TODO: to be extracted to its own interface (TreeObjectPicker) later
	public TreeTableNode[] getSelectedTreeNodes();
}
