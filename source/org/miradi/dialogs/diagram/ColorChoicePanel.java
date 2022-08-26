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
    public ColorChoicePanel(Project projectToUse, String editorDialogTitleToUse, ChoiceQuestion questionToUse, Color initialColorToUse, ActionListener colorChoiceChangeHandlerToUse)
    {
        super(new BorderLayout());

        project = projectToUse;
        editorDialogTitle = editorDialogTitleToUse;
        colorChoiceQuestion = questionToUse;
        initialColor = initialColorToUse;
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
    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            Color initialColorToUse = initialColor;
            if (!StringUtilities.isNullOrEmpty(selectedColor))
                initialColorToUse = Color.decode(selectedColor);

            DataInputPanel editorPanel = new DataInputPanel(project);
            editorPanel.setLayout(new BorderLayout());

            colorEditor = new ColorEditorComponent(initialColorToUse, this);
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
            selectedColor = FactorHtmlViewer.convertColorToHTMLColor(newColor);
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
    private Color initialColor;
    ActionListener colorChoiceChangeHandler;
    private String selectedColor;
    private boolean needsSave;
}
