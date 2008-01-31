/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

abstract public class ObjectListManagementPanel extends ObjectManagementPanel
{
	public ObjectListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ObjectCollectionPanel tablePanelToUse, AbstractObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, tablePanelToUse, propertiesPanelToUse);
	}
	
	public ObjectListManagementPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse, ObjectCollectionPanel tablePanelToUse, AbstractObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		this(null, splitPositionSaverToUse, tablePanelToUse, propertiesPanelToUse);
	}
	
}
