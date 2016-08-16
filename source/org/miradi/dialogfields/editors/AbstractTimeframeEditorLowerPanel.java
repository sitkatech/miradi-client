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

package org.miradi.dialogfields.editors;

import org.miradi.dialogfields.FieldSaver;
import org.miradi.dialogfields.TimeframeEditorField;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;

import java.awt.*;

abstract public class AbstractTimeframeEditorLowerPanel extends DisposablePanel
{
	public AbstractTimeframeEditorLowerPanel(ProjectCalendar projectCalendarToUse, StartEndDateUnitProvider startEndDateUnitProviderToUse)
	{
		cardLayout = new CardLayout();
		setLayout(cardLayout);

		projectCalendar = projectCalendarToUse;
		startEndDateUnitProvider = startEndDateUnitProviderToUse;

		noneCard = new NoneCard();
		projectTotalCard = new ProjectTotalDateUnitStartAndEndCard();
		yearCard = new CalendarYearDateUnitStartAndEndCard(projectCalendarToUse, startEndDateUnitProviderToUse);
		quarterCard = new QuarterDateUnitStartAndEndCard(projectCalendarToUse, startEndDateUnitProviderToUse);
		monthCard = new MonthDateUnitStartAndEndCard(projectCalendarToUse, startEndDateUnitProviderToUse);
		dayCard = createDayDateUnitStartAndEndCard(startEndDateUnitProviderToUse);
		
		add(noneCard, noneCard.getPanelDescription());
		add(projectTotalCard, projectTotalCard.getPanelDescription());
		add(yearCard, yearCard.getPanelDescription());
		add(quarterCard, quarterCard.getPanelDescription());
		add(monthCard, monthCard.getPanelDescription());
		add(dayCard, dayCard.getPanelDescription());

		backgroundColor = getBackground();

		currentCard = noneCard;
	}

	@Override
	public void dispose()
	{
		FieldSaver.savePendingEdits();
		disposePanel(dayCard);
		dayCard = null;

		super.dispose();
	}

	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		backgroundColor = bg;
		if (currentCard != null)
			currentCard.setBackground(backgroundColor);
	}

	public void addActionListener(TimeframeEditorField.TimeframeEditorChangeHandler editorFieldChangeHandlerToUse)
	{
		if (yearCard != null)
			yearCard.addActionListener(editorFieldChangeHandlerToUse);
		if (quarterCard != null)
			quarterCard.addActionListener(editorFieldChangeHandlerToUse);
		if (monthCard != null)
			monthCard.addActionListener(editorFieldChangeHandlerToUse);
		if (dayCard != null)
			dayCard.addActionListener(editorFieldChangeHandlerToUse);
	}

	public void setStartEndDateUnitProvider(ProjectCalendar projectCalendarToUse, StartEndDateUnitProvider startEndDateUnitProviderToUse)
	{
		projectCalendar = projectCalendarToUse;
		startEndDateUnitProvider = startEndDateUnitProviderToUse;
		yearCard.setStartEndDateUnitProvider(projectCalendar, startEndDateUnitProvider);
		quarterCard.setStartEndDateUnitProvider(projectCalendar, startEndDateUnitProvider);
		monthCard.setStartEndDateUnitProvider(projectCalendar, startEndDateUnitProvider);
		dayCard.setStartEndDateUnitProvider(projectCalendar, startEndDateUnitProvider);
	}

	public DateUnit getStartDateUnit()
	{
		return currentCard.getStartDate();
	}
	
	public DateUnit getEndDateUnit()
	{
		return currentCard.getEndDate();
	}
	
	public void showCard(String cardName)
	{
		currentCard = findPanel(cardName);
		currentCard.setBackground(backgroundColor);
		cardLayout.show(this, currentCard.getPanelDescription());
	}

	abstract protected AbstractDayDateUnitStartAndEndCard createDayDateUnitStartAndEndCard(StartEndDateUnitProvider startEndDateUnitProviderToUse);

	private DateUnitStartAndEndCard findPanel(String cardName)
	{
		if (cardName.equals(noneCard.getPanelDescription()))
			return noneCard;
		
		if (cardName.equals(projectTotalCard.getPanelDescription()))
			return projectTotalCard;
		
		if (cardName.equals(yearCard.getPanelDescription()))
			return yearCard;
		
		if (cardName.equals(quarterCard.getPanelDescription()))
			return quarterCard;
		
		if (cardName.equals(monthCard.getPanelDescription()))
			return monthCard;
		
		if (cardName.equals(dayCard.getPanelDescription()))
			return dayCard;
		
		throw new RuntimeException(cardName + " card could not be found");
	}

	private ProjectCalendar projectCalendar;
	private StartEndDateUnitProvider startEndDateUnitProvider;
	private Color backgroundColor;
	private CardLayout cardLayout;
	private DateUnitStartAndEndCard currentCard;
	
	private NoneCard noneCard;
	private ProjectTotalDateUnitStartAndEndCard projectTotalCard;
	private CalendarYearDateUnitStartAndEndCard yearCard;
	private QuarterDateUnitStartAndEndCard quarterCard;
	private MonthDateUnitStartAndEndCard monthCard;
	private AbstractDayDateUnitStartAndEndCard dayCard;
}
