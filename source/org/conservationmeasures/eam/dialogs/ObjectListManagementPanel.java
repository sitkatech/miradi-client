/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.utils.SplitterPositionSaver;

abstract public class ObjectListManagementPanel extends ObjectManagementPanel
{
	public ObjectListManagementPanel(SplitterPositionSaver splitPositionSaverToUse, String splitterNameToUse, ObjectListTablePanel tablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(splitPositionSaverToUse, splitterNameToUse, tablePanelToUse, propertiesPanelToUse);
	}
	
}
