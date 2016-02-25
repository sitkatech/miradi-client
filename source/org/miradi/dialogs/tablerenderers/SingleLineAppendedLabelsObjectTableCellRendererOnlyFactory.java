/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.tablerenderers;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.HtmlUtilities;

import javax.swing.text.html.StyleSheet;


public class SingleLineAppendedLabelsObjectTableCellRendererOnlyFactory extends MultiLineObjectTableCellRendererOnlyFactory
{
	public SingleLineAppendedLabelsObjectTableCellRendererOnlyFactory(MainWindow mainWindowToUse, RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(mainWindowToUse, providerToUse, fontProviderToUse);
	}

	@Override
	protected TableCellHtmlRendererComponent createTableCellHtmlRendererComponent(MainWindow mainWindowToUse)
	{
		return new TableCellHtmlRendererComponentWithCustomStylesheet(mainWindowToUse);
	}

	public static class TableCellHtmlRendererComponentWithCustomStylesheet extends TableCellHtmlRendererComponent implements TableCellPreferredHeightProvider
	{
		public TableCellHtmlRendererComponentWithCustomStylesheet(MainWindow mainWindow)
		{
			super(mainWindow);
		}
		
		@Override
		protected void customizeStyleSheet(StyleSheet style)
		{
			final int fontSize = getMainWindow().getWizardFontSize();
			HtmlUtilities.addRuleFontSize(style, getFont().getSize(), fontSize);
			HtmlUtilities.addRuleFontFamily(style, getMainWindow().getDataPanelFontFamily());
			HtmlUtilities.addFontColor(style, EAM.EDITABLE_FOREGROUND_COLOR);
			HtmlUtilities.addCustomListItemStyle(style);
		}
	}
}
