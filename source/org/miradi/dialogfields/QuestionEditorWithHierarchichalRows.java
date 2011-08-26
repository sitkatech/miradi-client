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

package org.miradi.dialogfields;

import java.awt.Font;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.base.DataPanelSingleRowSelectionHandler;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.SingleRowSelectionHandler;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceItemWithLongDescriptionProvider;
import org.miradi.questions.ChoiceQuestion;

public class QuestionEditorWithHierarchichalRows extends QuestionBasedEditorComponent
{
	public QuestionEditorWithHierarchichalRows(MainWindow mainWindowToUse, ChoiceQuestion questionToUse)
	{
		super(questionToUse, SINGLE_COLUMN);
		
		mainWindow = mainWindowToUse;
		mainWindow = mainWindowToUse;
		rowSelectionHandler = getSafeRowSelectionHandler();
		setBackground(AppPreferences.getDataPanelBackgroundColor());
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		getSafeRowSelectionHandler().becomeActive();
	}

	@Override
	public void becomeInactive()
	{
		getSafeRowSelectionHandler().becomeInactive();
		
		super.becomeInactive();
	}
	
	public SingleRowSelectionHandler getSafeRowSelectionHandler()
	{
		if (rowSelectionHandler == null)
			rowSelectionHandler = new DataPanelSingleRowSelectionHandler();
		
		return rowSelectionHandler;
	}

	public void addSelectionListener(ListSelectionListener rightSideDescriptionPanel)
	{
		getSafeRowSelectionHandler().addSelectionListener(rightSideDescriptionPanel);
	}
	
	public void removeSelectionListener(ListSelectionListener rightSideDescriptionPanel)
	{
		getSafeRowSelectionHandler().removeSelectionListener(rightSideDescriptionPanel);
	}
		
	@Override
	protected void addComponentToRowPanel(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, ChoiceItem choiceItem)
	{
		mainRowsPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		try
		{
			addRowComponents(mainRowsPanel, leftColumnComponent, choiceItem);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	@Override
	protected int calculateColumnCount()
	{
		int columnPerRowCount = getColumnCount();
		if (getQuestion().hasAdditionalText())
			++columnPerRowCount;
		
		return columnPerRowCount;
	}

	protected void addRowComponents(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, ChoiceItem choiceItem) throws Exception
	{
		ChoiceItemWithLongDescriptionProvider castedChoiceItem = (ChoiceItemWithLongDescriptionProvider) choiceItem;
		addHeaderSelectableRow(mainRowsPanel, leftColumnComponent, castedChoiceItem);
		addSubHeaderComponents(mainRowsPanel, leftColumnComponent, choiceItem.getChildren());
	}

	private void addSubHeaderComponents(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, Vector<ChoiceItem> children) throws Exception
	{
		for(ChoiceItem childChoiceItem : children)
		{
			ChoiceItemWithLongDescriptionProvider castedChoiceItem = (ChoiceItemWithLongDescriptionProvider) childChoiceItem;
			addSubHeaderSelectableComponents(mainRowsPanel, leftColumnComponent, castedChoiceItem);
			addLeafComponents(mainRowsPanel, leftColumnComponent, castedChoiceItem.getChildren());
		}
	}

	private void addLeafComponents(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, Vector<ChoiceItem> children) throws Exception
	{
		for(ChoiceItem childChoiceItem : children)
		{
			addLeafSelectableComponents(mainRowsPanel, leftColumnComponent, (ChoiceItemWithLongDescriptionProvider) childChoiceItem);
		}		
	}
	
	private void addLeafSelectableComponents(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, ChoiceItemWithLongDescriptionProvider choiceItem) throws Exception
	{
	 	final int LEAF_INDENT_COUNT = 2;
		
		addSelectableRow(mainRowsPanel, leftColumnComponent, choiceItem, LEAF_INDENT_COUNT, getLeafTitleFont());
	}

	private void addSubHeaderSelectableComponents(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, ChoiceItemWithLongDescriptionProvider choiceItem) throws Exception
	{
		final int SUBHEADER_INDENT_COUNT = 1;
		
		addSelectableRow(mainRowsPanel, leftColumnComponent, choiceItem, SUBHEADER_INDENT_COUNT, createSubHeaderFont());
	}

	private void addHeaderSelectableRow(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, ChoiceItemWithLongDescriptionProvider choiceItem) throws Exception
	{
		final int HEADER_INDENT_COUNT = 0;
		
		addSelectableRow(mainRowsPanel, leftColumnComponent, choiceItem, HEADER_INDENT_COUNT, createHeaderTitleFont());
	}

	protected void addSelectableRow(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, ChoiceItemWithLongDescriptionProvider choiceItem, final int indentCount, Font font)
	{
		JComponent leftComponent = createLeftColumnComponent(choiceItem, leftColumnComponent);
		leftComponent.setFont(font);
		
		PanelTitleLabel rightComponent = new PanelTitleLabel(choiceItem.getAdditionalText());
		rightComponent.setFont(font);
		
		Box box = createHorizontalBoxWithIndents(indentCount);
		box.add(leftComponent);
		mainRowsPanel.add(box);
		mainRowsPanel.add(rightComponent);
		
		Vector<JComponent> selectableComponents = new Vector<JComponent>();
		selectableComponents.add(leftColumnComponent);
		selectableComponents.add(rightComponent);
		getSafeRowSelectionHandler().addSelectableRow(selectableComponents, choiceItem.getLongDescriptionProvider());
	}
	
	protected Box createHorizontalBoxWithIndents(int indentCount)
	{
		Box box = Box.createHorizontalBox();
		for (int index = 0; index < indentCount; ++index)
		{
			box.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		}
		
		return box;
	}
	
	private Font createHeaderTitleFont()
	{
		Font font = getRawFont();
		font = font.deriveFont(Font.BOLD);
		font = font.deriveFont((float)(font.getSize() * 1.5));
		
		return font;
	}
	
	private Font createSubHeaderFont()
	{
		Font font = getRawFont();
		font = font.deriveFont(Font.BOLD);
		
		return font;
	}
	
	private Font getLeafTitleFont()
	{
		return getRawFont();
	}

	protected Font getRawFont()
	{
		return new PanelTitleLabel().getFont();
	}
	
	protected String getMainDescriptionFileName()
	{
		return "dashboard/1.html";
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
	private SingleRowSelectionHandler rowSelectionHandler;
	
	protected static final int INDENT_PER_LEVEL = 25;
}
