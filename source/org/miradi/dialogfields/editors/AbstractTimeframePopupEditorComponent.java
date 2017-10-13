/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.AbstractPopupEditorComponent;
import org.miradi.utils.CodeList;
import org.miradi.utils.HtmlUtilities;

public abstract class AbstractTimeframePopupEditorComponent extends AbstractPopupEditorComponent
{
	public AbstractTimeframePopupEditorComponent(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	abstract protected AbstractTimeframeEditorComponent createWhenEditorComponent(Project projectToUse, BaseObject baseObjectForRow) throws Exception;

	abstract protected String getDialogTitle();

	@Override
	protected void invokePopupEditor()
	{
		try
		{
			String title = getDialogTitle();
			ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), title);
			whenEditorPanel = createWhenEditorComponent(getMainWindow().getProject(), baseObjectForRow);
			dialog.setMainPanel(whenEditorPanel);
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
	
	@Override
	public String getText()
	{
		try
		{
			if (whenEditorPanel != null)
				return whenEditorPanel.getStartEndCodes().toString();
			
			return null;
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
			return new CodeList().toString();
		}
	}
	
	protected String getBaseObjectForRowLabel()
	{
		String combinedValue = baseObjectForRow.combineShortLabelAndLabel();
		return HtmlUtilities.convertHtmlToPlainText(combinedValue);
	}
	
	public void setBaseObjectForRowLabel(BaseObject baseObjectForRowToUse)
	{
		baseObjectForRow = baseObjectForRowToUse;
	}
	
	private AbstractTimeframeEditorComponent whenEditorPanel;
	private BaseObject baseObjectForRow;
}
