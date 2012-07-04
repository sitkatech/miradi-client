/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs;

import javax.swing.BorderFactory;

import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.fieldComponents.PanelCheckBox;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.notify.NeverAgainCheckBoxHandler;
import org.miradi.dialogs.notify.NotifyDialogTemplate;
import org.miradi.dialogs.notify.NotifyDialogTemplateFactory;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FillerLabel;

public class NeverShowAgainPanel extends DisposablePanelWithDescription
{
	public NeverShowAgainPanel(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		
		setLayout(new OneColumnGridLayout());
		setBackground(getPreferences().getDataPanelBackgroundColor());
		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		add(new PanelTitleLabel(EAM.text("If a notification dialog in this list is checked, ")));
		add(new PanelTitleLabel(EAM.text("it will not be shown. Uncheck it to have Miradi ")));
		add(new PanelTitleLabel(EAM.text("start displaying it again.")));
		add(new FillerLabel());
		
		// TODO: This should get all the templates from the pool, 
		// and loop through them creating checkboxes
		NotifyDialogTemplate template = NotifyDialogTemplateFactory.pastedSharedFactors();
		addCheckBox(template);
	}

	private void addCheckBox(NotifyDialogTemplate template)
	{
		String dialogCode = template.getDialogCode();
		PanelCheckBox checkbox1 = new PanelCheckBox(template.getTitle());
		checkbox1.setBackground(AppPreferences.getDataPanelBackgroundColor());
		checkbox1.setSelected(getPreferences().shouldNeverShowNotifyDialogAgain(dialogCode));
		checkbox1.addActionListener(new NeverAgainCheckBoxHandler(mainWindow, dialogCode));
		add(checkbox1);
	}
	
	AppPreferences getPreferences()
	{
		return mainWindow.getAppPreferences();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Notification Dialogs");
	}

	private MainWindow mainWindow;
}
