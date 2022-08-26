/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogfields;

import org.miradi.dialogs.diagram.ColorChoicePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorChoiceField extends ObjectDataInputField implements ActionListener {
    public ColorChoiceField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, Color defaultColorToUse)
    {
        super(projectToUse, refToUse, tagToUse);

        colorChoiceQuestion = questionToUse;
        defaultColor = defaultColorToUse;

        final String dialogTitle = Translation.fieldLabel(getObjectType(), getTag());
        colorChoicePanel = new ColorChoicePanel(dialogTitle, colorChoiceQuestion, defaultColor, this);
        colorChoicePanel.addFocusListener(this);
    }

	@Override
	public void dispose()
	{
		super.dispose();

		if (colorChoicePanel != null)
		{
            colorChoicePanel.dispose();
            colorChoicePanel.removeFocusListener(this);
            colorChoicePanel = null;
		}
	}

    @Override
    public JComponent getComponent()
    {
        return colorChoicePanel;
    }

	@Override
	public boolean needsToBeSaved()
	{
		if(colorChoicePanel == null)
		{
			EAM.logWarning("ColorChoiceField.needsToBeSaved called after dispose");
			EAM.logStackTrace();
			return false;
		}
		return colorChoicePanel.needsToBeSaved();
	}

	@Override
	public void clearNeedsSave()
	{
        colorChoicePanel.clearNeedsSaving();
	}

    @Override
    public String getText()
    {
        return colorChoicePanel.getText();
    }

    @Override
    public void setText(String newValue)
    {
        colorChoicePanel.setText(newValue);
    }

    @Override
    protected boolean shouldBeEditable()
    {
        return isValidObject();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        saveIfNeeded();
    }

    private ChoiceQuestion colorChoiceQuestion;
    private Color defaultColor;
    private ColorChoicePanel colorChoicePanel;
}
