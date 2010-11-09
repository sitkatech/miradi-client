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

package org.miradi.dialogs.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.Box;

import org.miradi.dialogfields.QuestionEditorWithHierarchichalRows;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.summary.SummaryView;

abstract public class LeftSidePanelWithSelectableRows extends QuestionEditorWithHierarchichalRows
{
	public LeftSidePanelWithSelectableRows(MainWindow mainWindowToUse)
	{
		this(mainWindowToUse, new EmptyQuestion());
	}
	
	public LeftSidePanelWithSelectableRows(MainWindow mainWindowToUse, ChoiceQuestion questionToUse)
	{
		super(mainWindowToUse, questionToUse);
		
		setLayout(new TwoColumnGridLayout());
		setBackground(AppPreferences.getDataPanelBackgroundColor());
	}
	
		protected void createSubHeaderRow(String leftColumnTranslatedText, String rightPanelHtmlFileName, String wizardStepName) throws Exception
	{
		String rightColumnTranslatedText = "";
		createSubHeaderRow(leftColumnTranslatedText, rightColumnTranslatedText, rightPanelHtmlFileName, wizardStepName);
	}

	protected void createSubHeaderRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String rightPanelHtmlFileName, String wizardStepName) throws Exception
	{
		Box firstColumnBox = createBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		createSubHeaderRow(leftColumnTranslatedText, rightColumnTranslatedText, rightPanelHtmlFileName, firstColumnBox, wizardStepName);
	}
	
	protected SelectableRow createDataRow( String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, String wizardStepName) throws Exception
	{
		Box firstColumnBox = createBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		
		return createRow(leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, firstColumnBox, wizardStepName);
	}
	
	protected SelectableRow createHeaderRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, String wizardStepName) throws Exception
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		Font font = leftLabel.getFont();
		font = font.deriveFont(Font.BOLD);
		font = font.deriveFont((float)(font.getSize() * 1.5));
		leftLabel.setFont(font);

		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		Box firstColumnBox = createBox();
		SelectableRow headerRow = createRow(descriptionFileName, firstColumnBox, wizardStepName, leftLabel, rightLabel);
		notifyListeners(headerRow);
		
		return headerRow;
	}
	
	private SelectableRow createSubHeaderRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, Box firstColumnBox, String wizardStepName) throws Exception
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		Font font = leftLabel.getFont();
		font = font.deriveFont(Font.BOLD);
		leftLabel.setFont(font);
		
		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		return createRow(descriptionFileName, firstColumnBox, wizardStepName, leftLabel, rightLabel);
	}
	
	private SelectableRow createRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, Box firstColumnBox, String wizardStepName) throws Exception
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		return createRow(descriptionFileName, firstColumnBox, wizardStepName, leftLabel, rightLabel);
	}

	private SelectableRow createRow(String descriptionFileName, Box firstColumnBox, String wizardStepName, PanelTitleLabel leftLabel, PanelTitleLabel rightLabel) throws Exception
	{
		firstColumnBox.add(leftLabel, BorderLayout.BEFORE_FIRST_LINE);

		Box secondColumnBox = createBox();

		secondColumnBox.add(rightLabel);

		SelectableRow selectableRow = new SelectableRow(firstColumnBox, secondColumnBox, new HtmlResourceRowDescriptionProvider(descriptionFileName, wizardStepName));
		selectableRow.addMouseListener(new ClickHandler(selectableRow));
		selectableRows.add(selectableRow);

		add(firstColumnBox);
		add(secondColumnBox);

		return selectableRow;
	}

	protected Box createBox()
	{
		Box box = Box.createHorizontalBox();
		
		return box;
	}
	
	protected String getDashboardData(String tag)
	{
		return getDashboard().getData(tag);
	}
	
	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}
	
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	protected String getDiagramOverviewStepName()
	{
		return getMainWindow().getWizardManager().getOverviewStepName(DiagramView.getViewName());
	}
	
	protected String getSummaryOverviewStepName()
	{
		return getMainWindow().getWizardManager().getOverviewStepName(SummaryView.getViewName());
	}
	

	private static class EmptyQuestion extends ChoiceQuestion
	{
		@Override
		public ChoiceItem[] getChoices()
		{
			return new ChoiceItem[0];
		}
	}
		
	abstract protected String getMainDescriptionFileName();
}
