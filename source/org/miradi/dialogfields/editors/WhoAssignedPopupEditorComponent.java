/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogfields.editors;

import org.martus.swing.Utilities;
import org.miradi.dialogfields.FieldSaver;
import org.miradi.dialogfields.WhoAssignedCodeListEditorComponent;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ProjectResourceQuestion;
import org.miradi.utils.AbstractPopupEditorComponent;
import org.miradi.utils.HtmlUtilities;

import javax.swing.*;
import java.awt.*;

public class WhoAssignedPopupEditorComponent extends AbstractPopupEditorComponent
{
	public WhoAssignedPopupEditorComponent(MainWindow mainWindowToUse, ORefSet resourceRefsFilterToUse)
	{
		super(mainWindowToUse);
		resourceRefsFilter = resourceRefsFilterToUse;
	}

	private WhoAssignedCodeListEditorComponent createEditorComponent(BaseObject baseObject, ProjectResourceQuestion question, ORefSet resourceRefsFilter) throws Exception
	{
		return new WhoAssignedCodeListEditorComponent(baseObject, question, resourceRefsFilter);
	}

	protected String getDialogTitle()
	{
		String objectFullNameAsPlainText = getBaseObjectForRowLabel();
		String dialogTitle = EAM.text("Who Work Assignments");
		if (!objectFullNameAsPlainText.isEmpty())
			dialogTitle += " - " + objectFullNameAsPlainText;

		return dialogTitle;
	}

	@Override
	protected void invokePopupEditor()
	{
		try
		{
			String title = getDialogTitle();
			ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), title);

			PanelTitleLabel panelTitleLabel = new PanelTitleLabel(getDialogHelpText());
			panelTitleLabel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
			dialog.add(panelTitleLabel, BorderLayout.BEFORE_FIRST_LINE);

			final ProjectResourceQuestion question = new ProjectResourceQuestion(getMainWindow().getProject());
			whoAssignedEditorPanel = createEditorComponent(baseObjectForRow, question, resourceRefsFilter);

			dialog.setScrollableMainPanel(whoAssignedEditorPanel);
			dialog.getWrappedPanel().setBackground(AppPreferences.getDataPanelBackgroundColor());
			dialog.pack();

			Utilities.centerFrame(dialog);
			dialog.setVisible(true);
			FieldSaver.savePendingEdits();
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	private String getDialogHelpText()
	{
		return EAM.text("<html>" +
				"Select people from the list. This creates work plan Assignments.");
	}

	@Override
	public String getText()
	{
		// edits are handled via editor panel
		return null;
	}
	
	private String getBaseObjectForRowLabel()
	{
		String combinedValue = baseObjectForRow.combineShortLabelAndLabel();
		return HtmlUtilities.convertHtmlToPlainText(combinedValue);
	}
	
	public void setBaseObjectForRowLabel(BaseObject baseObjectForRowToUse)
	{
		baseObjectForRow = baseObjectForRowToUse;
	}
	
	private WhoAssignedCodeListEditorComponent whoAssignedEditorPanel;
	private BaseObject baseObjectForRow;
	private ORefSet resourceRefsFilter;
}
