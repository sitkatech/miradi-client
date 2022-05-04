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
package org.miradi.dialogs;

import com.jhlabs.awt.GridLayoutPlus;
import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;
import org.miradi.dialogs.base.DialogWithButtonBar;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.MiradiUIButton;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.ProjectNameRestrictedTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import static org.miradi.icons.IconManager.getWarningIcon;
import static org.miradi.main.MiradiStrings.getCancelButtonText;
import static org.miradi.main.MiradiStrings.getOverwriteLabel;

public class ModalProjectOverwriteDialog extends DialogWithButtonBar implements ActionListener, DocumentListener
{
    public static String showDialog(MainWindow mainWindowToUse, String titleToUse, String proposedProjectNameToUse)
    {
        ModalProjectOverwriteDialog dialog = new ModalProjectOverwriteDialog(mainWindowToUse, titleToUse, proposedProjectNameToUse);
        dialog.setVisible(true);
        return dialog.getProposedProjectName();
    }

    private ModalProjectOverwriteDialog(MainWindow mainWindowToUse, String titleToUse, String proposedProjectNameToUse)
    {
        super(mainWindowToUse);

        proposedProjectName = proposedProjectNameToUse;

        setTitle(titleToUse);
        setModal(true);

        MiradiPanel panel = new MiradiPanel(new GridLayoutPlus(0, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        projectNameField = new ProjectNameRestrictedTextField(proposedProjectNameToUse);
        projectNameField.getDocument().addDocumentListener(this);
        panel.add(new PanelTitleLabel(EAM.text("Project name: ")));
        panel.add(projectNameField);

        String message = EAM.text("A project or file by this name already exists - enter a new name or choose Overwrite to replace the existing file.");
        warningMessageField = new PanelTitleLabel(message, getWarningIcon());
        warningMessageField.setVerticalAlignment(SwingConstants.TOP);
        panel.add(new FillerLabel());
        panel.add(warningMessageField);

        panel.add(new FillerLabel());
        panel.add(new FillerLabel());

        overwriteButton = new PanelButton(getOverwriteLabel());
        saveAsButton = new PanelButton(EAM.text("Button|Save"));
        cancelButton = new PanelButton(getCancelButtonText());
        Vector<Component> buttons = new Vector<Component>();
        buttons.add(overwriteButton);
        buttons.add(saveAsButton);
        buttons.add(cancelButton);

        overwriteButton.addActionListener(this);
        saveAsButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setButtonEnabledStates();

        getContentPane().add(new MiradiScrollPane(panel));
        setButtons(buttons);

        pack();
        Utilities.centerDlg(this);
    }

    @Override
    public void insertUpdate(DocumentEvent event)
    {
        setButtonEnabledStates();
    }

    @Override
    public void removeUpdate(DocumentEvent event)
    {
        setButtonEnabledStates();
    }

    @Override
    public void changedUpdate(DocumentEvent event)
    {
        setButtonEnabledStates();
    }

    private void setButtonEnabledStates()
    {
        boolean isProjectNameFieldNotEmpty = (projectNameField.getText().length() > 0);

        overwriteButton.setEnabled(isProjectNameFieldNotEmpty);
        saveAsButton.setEnabled(isProjectNameFieldNotEmpty);

        if (isProjectNameFieldNotEmpty)
        {
            if (projectExists(projectNameField.getText()))
            {
                warningMessageField.setVisible(true);
                overwriteButton.setEnabled(true);
                saveAsButton.setEnabled(false);
            }
            else
            {
                warningMessageField.setVisible(false);
                overwriteButton.setEnabled(false);
                saveAsButton.setEnabled(true);
            }
        }
    }

    private boolean projectExists(String proposedProjectName)
    {
        try
        {
            String proposedProjectFileName = AbstractMpfFileFilter.createNameWithExtension(proposedProjectName);
            File proposedProjectFile = new File(EAM.getHomeDirectory(), proposedProjectFileName);
            return MainWindow.projectExists(proposedProjectFile);
        }
        catch (Exception ex)
        {
            EAM.logException(ex);
        }

        return false;
    }

    private String getProposedProjectName()
    {
        return proposedProjectName;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        if (event.getSource() == saveAsButton)
            proposedProjectName = projectNameField.getText();

        if (event.getSource() == cancelButton)
            proposedProjectName = null;

        setVisible(false);
        dispose();
    }

    private String proposedProjectName;
    private UiLabel warningMessageField;
    private ProjectNameRestrictedTextField projectNameField;
    private MiradiUIButton overwriteButton;
    private MiradiUIButton saveAsButton;
    private MiradiUIButton cancelButton;

}
