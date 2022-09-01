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

import org.martus.swing.Utilities;
import org.miradi.diagram.renderers.FactorHtmlViewer;
import org.miradi.dialogfields.DataField;
import org.miradi.dialogs.base.DataInputPanel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.ColorEditorComponent;
import org.miradi.utils.StringUtilities;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorChoicePanel extends MiradiPanel implements ActionListener, ChangeListener
{
    public ColorChoicePanel(Project projectToUse, String editorDialogTitleToUse, ChoiceQuestion questionToUse, Color defaultColorToUse, ActionListener colorChoiceChangeHandlerToUse)
    {
        super(new BorderLayout());

        project = projectToUse;
        editorDialogTitle = editorDialogTitleToUse;
        colorChoiceQuestion = questionToUse;
        defaultColor = defaultColorToUse;
        selectedColor = FactorHtmlViewer.convertColorToHTMLColor(defaultColorToUse);
        colorChoiceChangeHandler = colorChoiceChangeHandlerToUse;

        setBackground(EAM.READONLY_BACKGROUND_COLOR);
        selectButton = new PanelButton("Select...");
        selectButton.setOpaque(true);
        selectButton.setBackground(defaultColor);
        selectButton.setForeground(defaultColor);
        selectButton.setBorder(null);
        selectButton.addActionListener(this);
		OneColumnPanel buttonPanel = new OneColumnPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
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

        Color color = FactorHtmlViewer.convertHTMLColorToColor(selectedColor, defaultColor);
        selectButton.setBackground(color);
        selectButton.setForeground(color);

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
    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            Color color = defaultColor;
            if (!StringUtilities.isNullOrEmpty(selectedColor))
                color = Color.decode(selectedColor);

            DataInputPanel editorPanel = new DataInputPanel(project);
            editorPanel.setLayout(new BorderLayout());

            colorEditor = new ColorEditorComponent(color, this);
            editorPanel.add(colorEditor);

            ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), editorDialogTitle);
            dialog.setMainPanel(editorPanel);
            dialog.becomeActive();
            Utilities.centerDlg(dialog);
            dialog.setVisible(true);
        }
        catch (Exception e)
        {
            EAM.alertUserOfNonFatalException(e);
        }
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        Color newColor = colorEditor.getColor();
        saveColor(newColor, changeEvent.getSource());
    }

    private void saveColor(Color newColor, Object actionSource)
    {
        if (newColor != null)
        {
            String color = FactorHtmlViewer.convertColorToHTMLColor(newColor);
            setText(color);
            setNeedsSave();

            if (colorChoiceChangeHandler != null)
                colorChoiceChangeHandler.actionPerformed(new ActionEvent(actionSource, ActionEvent.ACTION_PERFORMED, "ColorChoicePanel"));
        }
    }

    private Project project;
    private String editorDialogTitle;
    private ChoiceQuestion colorChoiceQuestion;
    private PanelButton selectButton;
    private ColorEditorComponent colorEditor;
    private Color defaultColor;
    ActionListener colorChoiceChangeHandler;
    private String selectedColor;
    private boolean needsSave;
}
