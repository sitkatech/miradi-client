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

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.miradi.dialogs.base.EAMDialog;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBoxWithMaxAsPreferredSize;
import org.miradi.questions.ChoiceItem;
import org.miradi.views.umbrella.AboutDoer;
import org.miradi.views.umbrella.HelpAboutPanel;

public class InitialSplashPanel extends HelpAboutPanel
{

	public InitialSplashPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, AboutDoer.buildMainSection() + AboutDoer.buildEndSection());
	}

	@Override
	protected JComponent createButtonBar(EAMDialog dlg)
	{
		UiButton close = new UiButton(new CloseAction(dlg, EAM.text("Button|Continue")));
		ChoiceItemComboBoxWithMaxAsPreferredSize languageDropdown = new ChoiceItemComboBoxWithMaxAsPreferredSize(getAvailableLanguageChoices());

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

	private ChoiceItem[] getAvailableLanguageChoices()
	{
		try
		{
			return Miradi.getAvailableLanguageCodes().toArray(new ChoiceItem[0]);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new ChoiceItem[0];
		}
	}
}
