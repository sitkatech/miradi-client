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

package org.miradi.dialogs.diagram;

import org.miradi.diagram.renderers.FactorHtmlViewer;
import org.miradi.dialogfields.DataField;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.ColorEditorComponent;
import org.miradi.utils.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorChoicePanel extends MiradiPanel implements ActionListener
{
    public ColorChoicePanel(String editorDialogTitleToUse, ChoiceQuestion questionToUse, Color defaultColorToUse, ActionListener colorChoiceChangeHandlerToUse)
    {
        super(new BorderLayout());

        editorDialogTitle = editorDialogTitleToUse;
        colorChoiceQuestion = questionToUse;
        defaultColor = defaultColorToUse;
        colorChoiceChangeHandler = colorChoiceChangeHandlerToUse;

        setBackground(EAM.READONLY_BACKGROUND_COLOR);
        selectButton = new PanelButton("Show Color Chooser...");
        selectButton.addActionListener(this);
		OneColumnPanel buttonPanel = new OneColumnPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPanel.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		buttonPanel.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		buttonPanel.add(selectButton);
		setBorder(DataField.createLineBorderWithMargin());
		add(buttonPanel, BorderLayout.AFTER_LINE_ENDS);

        clearNeedsSaving();
    }

    public void dispose()
    {
		if (colorEditor != null)
		{
            colorEditor.dispose();
            colorEditor = null;
		}

        colorChoiceChangeHandler = null;
    }

    public void setText(String text)
	{
        selectedColor = text;
        clearNeedsSaving();
	}

	public String getText()
	{
        return selectedColor;
	}

    @Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		selectButton.setEnabled(enabled);
	}

	private void setNeedsSave()
	{
		needsSave = true;
	}

	public void clearNeedsSaving()
	{
		needsSave = false;
	}

	public boolean needsToBeSaved()
	{
		return needsSave;
	}

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Color initialColor = defaultColor;
        if (!StringUtilities.isNullOrEmpty(selectedColor))
        {
            initialColor = Color.decode(selectedColor);
        }

        Color newColor = ColorEditorComponent.showDialog(this, editorDialogTitle, initialColor);
        if (newColor != null)
        {
            selectedColor = FactorHtmlViewer.convertColorToHTMLColor(newColor);
            setNeedsSave();
            if (colorChoiceChangeHandler != null)
            {
                colorChoiceChangeHandler.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "ColorChoicePanel"));
            }
        }
    }

    private String editorDialogTitle;
    private ChoiceQuestion colorChoiceQuestion;
    private PanelButton selectButton;
    private ColorEditorComponent colorEditor;
    private Color defaultColor;
    ActionListener colorChoiceChangeHandler;
    private String selectedColor;
    private boolean needsSave;
}
