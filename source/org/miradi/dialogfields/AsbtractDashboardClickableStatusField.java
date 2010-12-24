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

package org.miradi.dialogfields;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.dashboard.DashboardProgressPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.OpenStandardsDynamicProgressStatuQuestion;
import org.miradi.utils.Translation;


abstract public class AsbtractDashboardClickableStatusField extends ObjectDataInputField
{
	public AsbtractDashboardClickableStatusField(Project projectToUse, ORef refToUse, String stringMapCodeToUse)
	{
		super(projectToUse, refToUse, Dashboard.PSEUDO_EFFECTIVE_STATUS_MAP);
		
		stringMapCode = stringMapCodeToUse;
		Dashboard dashboard = Dashboard.find(getProject(), refToUse);
		question = new OpenStandardsDynamicProgressStatuQuestion(dashboard, stringMapCodeToUse);
		iconComponent = new PanelTitleLabel();
		iconComponent.addMouseListener(new ClickHandler());
	}
	
	@Override
	public void setText(String stringCodeMapAsString)
	{
		try
		{
			StringChoiceMap map = new StringChoiceMap(stringCodeMapAsString);
			String code = map.get(stringMapCode);
			ChoiceItem progressChoiceItem = question.findChoiceByCode(code);
			updateLabel(progressChoiceItem, iconComponent);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}

	@Override
	public void updateEditableState()
	{
		iconComponent.setEnabled(true);
	}

	@Override
	public JComponent getComponent()
	{
		return iconComponent;
	}

	@Override
	public String getText()
	{
		return "";
	}
	
	private class ClickHandler extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent mouseEvent)
		{
			super.mouseClicked(mouseEvent);
			
			try
			{
				DisposablePanel editorPanel = new DashboardProgressPanel(getProject(), getORef(), stringMapCode);
				ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), Translation.fieldLabel(getObjectType(), getTag()));
				dialog.setMainPanel(editorPanel);
				dialog.becomeActive();
				Utilities.centerDlg(dialog);
				dialog.setVisible(true);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.unexpectedErrorDialog(e);
			}
		}
	}
	
	abstract protected void updateLabel(ChoiceItem progressChoiceItem, PanelTitleLabel componentToUpdate);

	private PanelTitleLabel iconComponent;
	private String stringMapCode;
	private ChoiceQuestion question;
}
