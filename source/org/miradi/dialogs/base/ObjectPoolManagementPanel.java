/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import javax.swing.Icon;

import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

abstract public class ObjectPoolManagementPanel extends ObjectManagementPanel
{
	public ObjectPoolManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ObjectCollectionPanel tablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, tablePanelToUse, propertiesPanelToUse);
	}
	
	abstract public Icon getIcon();	
}
