/*
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.base;

import org.martus.util.StreamCopier;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBoxWithMaxAsPreferredSize;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.Miradi;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.LanguagePackFileChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Vector;

public class LanguagesPanel extends DisposablePanelWithDescription
{
    public LanguagesPanel(MainWindow mainWindowToUse)
    {
        mainWindow = mainWindowToUse;
        previousLanguageCode = mainWindowToUse.getAppPreferences().getLanguageCode();

        setLayout(new OneColumnGridLayout());
        setBackground(AppPreferences.getDataPanelBackgroundColor());
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        add(new PanelTitleLabel(EAM.text("Select language from choices below, ")));
        add(new PanelTitleLabel(EAM.text("or install a new language pack.")));
        add(new FillerLabel());
        add(new PanelTitleLabel(EAM.text("Changes will be applied on restarting Miradi.")));
        add(new FillerLabel());

        ChoiceItem[] availableLanguageChoices = getAvailableLanguageChoices();
        languageDropdown = new ChoiceItemComboBoxWithMaxAsPreferredSize(availableLanguageChoices);
        selectLanguage(previousLanguageCode);
        languageDropdown.addActionListener(new LanguagesPanel.LanguageSelectionListener());
        add(languageDropdown);
    }

    MainWindow getMainWindow()
    {
        return mainWindow;
    }

    private void selectLanguage(String codeToSelect)
    {
        languageDropdown.setSelectedCode(codeToSelect);
        if(languageDropdown.getSelectedIndex() < 0)
            languageDropdown.setSelectedIndex(0);
    }

    private String getSelectedLanguageCode()
    {
        int selected = languageDropdown.getSelectedIndex();
        if(selected >= 0)
            return languageDropdown.getChoiceItemAt(selected).getCode();

        return null;
    }

    private ChoiceItem[] getAvailableLanguageChoices()
    {
        Vector<ChoiceItem> availableLanguageChoices = new Vector<ChoiceItem>();
        try
        {
            availableLanguageChoices.addAll(Miradi.getAvailableLanguageChoices());
            Collections.sort(availableLanguageChoices);
        }
        catch(Exception e)
        {
            EAM.logException(e);
        }
        availableLanguageChoices.insertElementAt(new ChoiceItem("en", "English"), 0);
        availableLanguageChoices.addElement(new ChoiceItem(OTHER_LANGUAGE_CODE, "Other..."));
        return availableLanguageChoices.toArray(new ChoiceItem[0]);
    }

    private class LanguageSelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String languageCode = getSelectedLanguageCode();
            if(languageCode == null)
                return;

            if(languageCode.equals(OTHER_LANGUAGE_CODE))
            {
                tryToInstallOtherLanguages();
                languageDropdown.removeAllItems();
                ChoiceItem[] choices = getAvailableLanguageChoices();
                for(ChoiceItem choice : choices)
                    languageDropdown.addItem(choice);
            }

            getMainWindow().getAppPreferences().setLanguageCode(getSelectedLanguageCode());
            getMainWindow().safelySavePreferences();
        }

        private void tryToInstallOtherLanguages()
        {
            String local = EAM.text("Button|Install...");
            String cancel = EAM.text("Button|Cancel");
            String[] buttonLabels = {
                    local,
                    cancel
            };
            String sampleJarName = "MiradiContent-" + Miradi.LANGUAGE_PACK_VERSION + "-es.jar";
            String text = "<html>" + EAM.text(
                    "Additional languages are supported by installing Miradi Language Packs,<br> " +
                            "which are files with a name like: <em>%s</em><br>" +
                            "<br>" +
                            "If you already have a Language Pack file, you can install it into Miradi now.<br>" +
                            "Otherwise, you can download one from www.miradi.org. <br>" +
                            "");
            text = EAM.substituteSingleString(text, sampleJarName);
            int result = EAM.confirmDialog(EAM.text("Install Language Pack"), text, buttonLabels);

            if(buttonLabels[result].equals(local))
                installLocalLanguagePack();
        }

        private void installLocalLanguagePack()
        {
            LanguagePackFileChooser fileChooser = new LanguagePackFileChooser(getMainWindow());
            File chosen = fileChooser.displayChooser();
            if(chosen == null)
                return;

            File destination = new File(EAM.getHomeDirectory(), chosen.getName());
            if(destination.equals(chosen))
            {
                EAM.notifyDialog(EAM.text("That language pack is already installed"));
                return;
            }

            if(destination.exists())
            {
                String replace = EAM.text("Button|Replace");
                String cancel = EAM.text("Button|Cancel");
                String text = EAM.text("That language pack already exists. Replace it?");
                String[] buttons = new String[] {replace, cancel};
                int result = EAM.confirmDialog(EAM.text("Replace Language Pack"), text, buttons);
                if(!buttons[result].equals(replace))
                    return;
            }

            installLanguagePack(chosen, destination);
        }

        private void installLanguagePack(File chosen, File destination)
        {
            try
            {
                FileInputStream in = new FileInputStream(chosen);
                FileOutputStream out = new FileOutputStream(destination);
                new StreamCopier().copyStream(in, out);
                out.close();
                in.close();
                EAM.notifyDialog(EAM.text("Installed successfully"));
            }
            catch(Exception e)
            {
                EAM.logException(e);
                EAM.errorDialog(EAM.text("Unexpected error prevented installing that language pack"));
            }
        }
    }

    @Override
    public String getPanelDescription()
    {
        return EAM.text("Languages");
    }

    private static final String OTHER_LANGUAGE_CODE = "xxx";

    private MainWindow mainWindow;
    private ChoiceItemComboBoxWithMaxAsPreferredSize languageDropdown;
    private String previousLanguageCode;
}
