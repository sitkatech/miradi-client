/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.accountingcode;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AccountingCode;
import org.miradi.project.Project;

public class AccountingCodePropertiesPanel extends ObjectDataInputPanel
{
	public AccountingCodePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.ACCOUNTING_CODE, idToEdit);
		
		addField(createStringField(AccountingCode.TAG_CODE));
		addField(createStringField(AccountingCode.TAG_LABEL));
		addField(createMultilineField(AccountingCode.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Accounting Code Properties");
	}
}
