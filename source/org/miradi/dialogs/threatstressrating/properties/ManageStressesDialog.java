/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.properties;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.stress.StressListManagementPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Target;

public class ManageStressesDialog extends ModalDialogWithClose
{
	public ManageStressesDialog(MainWindow parent, Target target, StressListManagementPanel stressListManagementPanel)
	{
		super(parent, stressListManagementPanel, "");
		setTitle(EAM.text("Manage Stresses for ") + target.combineShortLabelAndLabel());

	}
	
	protected JComponent possiblyWrapInScrollPane(JPanel mainPanel)
	{
		return mainPanel;
	}
}
