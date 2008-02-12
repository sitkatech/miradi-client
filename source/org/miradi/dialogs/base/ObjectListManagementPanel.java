/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

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
