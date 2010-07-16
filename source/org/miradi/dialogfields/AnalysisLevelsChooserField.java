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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class AnalysisLevelsChooserField extends ObjectDataInputField
{ 
	public AnalysisLevelsChooserField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse);
		
		component = new AnalysisLevelsEditorComponent(projectToUse);
		component.addActionListener(new ComboBoxChangeHandler());
	}

	@Override
	public JComponent getComponent()
	{
		return component;
	}

	@Override
	public String getText()
	{
		return getComponentText();
	}

	private String getComponentText()
	{
		try
		{
			return component.getText();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
			return "";
		}
	}

	@Override
	public void setText(String codes)
	{
		enableSkipSave();
		try
		{
			component.setText(codes);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
		finally
		{
			disableSkipSave();
		}
	}
	
	private boolean shouldSave()
	{
		return !skipSave;
	}
	
	private void enableSkipSave()
	{
		skipSave = true;
	}
	
	private void disableSkipSave()
	{
		skipSave = false;
	}
	
	class ComboBoxChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if (shouldSave())
				forceSave();
		}
	}
	
	private boolean skipSave;
	private AnalysisLevelsEditorComponent component;
}
