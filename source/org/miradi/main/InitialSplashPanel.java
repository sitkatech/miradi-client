/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.util.StreamCopier;
import org.miradi.dialogs.base.DialogWithEscapeToClose;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBoxWithMaxAsPreferredSize;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.LanguagePackFileChooser;
import org.miradi.views.umbrella.AboutDoer;
import org.miradi.views.umbrella.HelpAboutPanel;

public class InitialSplashPanel extends HelpAboutPanel
{
	public InitialSplashPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, AboutDoer.buildMainSection() + AboutDoer.buildEndSection());
		previousLanguageCode = mainWindowToUse.getAppPreferences().getLanguageCode();
	}
	
	public String getSelectedLanguageCode()
	{
		int selected = languageDropdown.getSelectedIndex();
		if(selected >= 0)
			return languageDropdown.getChoiceItemAt(selected).getCode();

		return null;
	}

	@Override
	protected JComponent createButtonBar(DialogWithEscapeToClose dlg)
	{
		UiButton close = new UiButton(new CloseAction(dlg, EAM.text("Button|Continue")));

		languageDropdown = new ChoiceItemComboBoxWithMaxAsPreferredSize(getAvailableLanguageChoices());
		selectLanguage(previousLanguageCode);
		languageDropdown.addActionListener(new LanguageSelectionListener());

		Box buttonBar = Box.createHorizontalBox();
		buttonBar.setBorder(new EmptyBorder(5,5,5,5));
		buttonBar.setBackground(AppPreferences.getWizardTitleBackground());
		buttonBar.add(new UiLabel(EAM.text("Language: ")));
		buttonBar.add(languageDropdown);
		buttonBar.add(Box.createHorizontalGlue());
		buttonBar.add(close);

		setCloseButton(close);
		return buttonBar;
	}

	private void selectLanguage(String codeToSelect)
	{
		languageDropdown.setSelectedCode(codeToSelect);
		if(languageDropdown.getSelectedIndex() < 0)
			languageDropdown.setSelectedIndex(0);
	}

	private ChoiceItem[] getAvailableLanguageChoices()
	{
		try
		{
			Vector<ChoiceItem> availableLanguageChoices = new Vector(Miradi.getAvailableLanguageChoices());
			Collections.sort(availableLanguageChoices);
			availableLanguageChoices.insertElementAt(new ChoiceItem("en", "English"), 0);
			availableLanguageChoices.addElement(new ChoiceItem(OTHER_LANGUAGE_CODE, "Other..."));
			return availableLanguageChoices.toArray(new ChoiceItem[0]);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new ChoiceItem[0];
		}
	}
	
	class LanguageSelectionListener implements ActionListener
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
		}

		private void tryToInstallOtherLanguages()
		{
			String local = EAM.text("Button|Install...");
			String cancel = EAM.text("Button|Cancel");
			String[] buttonLabels = {
					local, 
					cancel
					};
			String sampleJarName = "MiradiContent-" + Miradi.MAIN_VERSION + "-es.jar";
			String text = "<html>" + EAM.text(
					"Additional languages are supported by installing Miradi Language Packs,<br> " +
					"which are files with a name like: <em>%s</em><br>" +
					"<br>" +
					"If you already have a Language Pack file, you can install it into Miradi now.<br>" +
					"Otherwise, you can download one from www.miradi.org. <br>" +
					"");
			text = EAM.substitute(text, sampleJarName);
			int result = EAM.confirmDialog(EAM.text("Install Language Pack"), text, buttonLabels);
			
			if(buttonLabels[result].equals(local))
				installLocalLanguagePack();
			else
				return;
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
	
	private static final String OTHER_LANGUAGE_CODE = "xxx";

	private ChoiceItemComboBoxWithMaxAsPreferredSize languageDropdown;
	private String previousLanguageCode;
}
