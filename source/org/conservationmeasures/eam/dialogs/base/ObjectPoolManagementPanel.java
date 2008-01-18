/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import javax.swing.Icon;

import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

abstract public class ObjectPoolManagementPanel extends ObjectManagementPanel
{
	public ObjectPoolManagementPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse, ObjectCollectionPanel tablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(splitPositionSaverToUse, tablePanelToUse, propertiesPanelToUse);
	}
	
	abstract public Icon getIcon();
	
}
