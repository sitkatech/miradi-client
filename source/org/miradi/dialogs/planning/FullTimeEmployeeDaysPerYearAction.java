/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.DialogWithButtonBar;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeMultiTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningUpperMultiTable;
import org.miradi.layout.OneColumnPanel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.OptionalDouble;
import org.miradi.utils.Translation;

public class FullTimeEmployeeDaysPerYearAction extends AbstractAction
{
	public FullTimeEmployeeDaysPerYearAction(PlanningUpperMultiTable planningUpperMultiTable)
	{
		super(EAM.text("Fraction of FTE..."));
		
		mainWindow = planningUpperMultiTable.getMainWindow();
		multiTable = planningUpperMultiTable;
	}

	public void actionPerformed(ActionEvent event)
	{
		Vector<Component> buttons = new Vector();
		PanelButton insertButton = new PanelButton(EAM.text("Insert"));
		insertButton.addActionListener(new InsertButtonHandler());
		buttons.add(insertButton);
		PanelButton cancelButton = new PanelButton(EAM.text("Cancel"));
		buttons.add(cancelButton);
		
		dialog = new DialogWithButtonBar(getMainWindow(), EAM.text("Insert FTE Fraction"));
		dialog.setButtons(buttons);
		dialog.setModal(true);
		dialog.setSimpleCloseButton(cancelButton);
		
		OneColumnPanel panel = new OneColumnPanel();
		panel.add(createExplanationHtmlPanel(), BorderLayout.BEFORE_FIRST_LINE);
		errorField = new PanelTextField(50);
		errorField.setEditable(false);
		panel.add(errorField, BorderLayout.AFTER_LAST_LINE);
		dialog.add(panel, BorderLayout.BEFORE_FIRST_LINE);
		
		panel.add(createEditPanel(), BorderLayout.CENTER);
		dialog.getRootPane().setDefaultButton(insertButton);
		fractionField.selectAll();
		fractionField.requestFocusInWindow();
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);
	}

	private FlexibleWidthHtmlViewer createExplanationHtmlPanel()
	{
		try
		{
			String html = Translation.getHtmlContent("FteDaysPerYearDialogExplanation.html");
			return new FlexibleWidthHtmlViewer(getMainWindow(), html);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new FlexibleWidthHtmlViewer(getMainWindow(), EAM.text("Error"));
		}
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}

	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public JPanel createEditPanel()
	{
		TwoColumnPanel panel = new TwoColumnPanel();
		String fullTimeEmployeeDaysPerYear = getFullTimeEmployeeDaysPerYearAsString();
		PanelTextField daysPerYearField = new PanelTextField(fullTimeEmployeeDaysPerYear);
		daysPerYearField.setEditable(false);
		panel.add(new PanelTitleLabel(EAM.text("FTE Days Per Year:")));
		panel.add(daysPerYearField);

		fractionField = new PanelTextField(10);
		fractionField.setDocument(new NumericDocument());
		fractionField.setText(getPrePopulatedValue());

		panel.add(new PanelTitleLabel(EAM.text("Fraction:")));
		panel.add(fractionField);
		
		return panel;
	}

	private String getPrePopulatedValue()
	{
		PlanningViewAbstractTreeTableSyncedTableModel model = getCastedModel().getCastedModel(getSelectedColumn());
		int columnWithinMultiTableModel = getSelectedColumnWithinMultiTableModel();
		OptionalDouble value = model.getCellFraction(getSelectedRow(), columnWithinMultiTableModel);
		if (value.hasValue())
			return Double.toString(value.getValue());
		
		return "";
	}
	
	private int getSelectedColumn()
	{
		int selectedColumn = multiTable.getSelectedColumn();
		int modelColumn = multiTable.convertColumnIndexToModel(selectedColumn);
		
		return modelColumn;
	}
	
	private int getSelectedColumnWithinMultiTableModel()
	{
		return getCastedModel().findColumnWithinSubTable(getSelectedColumn());
	}
	
	private int getSelectedRow()
	{
		return multiTable.getSelectedRow();
	}

	private PlanningTreeMultiTableModel getCastedModel()
	{
		return multiTable.getCastedModel();
	}

	private String getFullTimeEmployeeDaysPerYearAsString()
	{
		return AssignmentDateUnitsTableModel.getRawFullTimeEmployeeDaysPerYear(getProject());
	}

	public class NumericDocument extends PlainDocument 
	{
	     public NumericDocument() 
	     {
	          super();
	     }
	   
	     public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException 
	     {
	    	 try
	    	 {
	    		final String LEADING_0_TO_ALLOW_DECIMAL_WITHOUT_0 = "0";
				String total = LEADING_0_TO_ALLOW_DECIMAL_WITHOUT_0 + fractionField.getText() + str; 
	    		 double parsed = Double.parseDouble(total);
	    		 if (0.0 <= parsed)
	    		 {
	    			 super.insertString(offset, str, attr);
	    			 clearErrorField();
	    			 return;
	    		 }
	    		 
	    		 displayError();
	    	 }
	    	 catch (NumberFormatException ignoreException)
	    	 {
	    		 displayError();
	    	 }
	     }

		private void clearErrorField()
		{
			if (errorField != null)
			{
				 errorField.setText("");
				 errorField.setBackground(dialog.getBackground());
			}
		}

		private void displayError()
		{
			errorField.setBackground(Color.RED);
			errorField.setText(EAM.text("Must be a positive number"));
		}
	}
	
	class InsertButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String doubleAsString = fractionField.getText();
			double parsedDouble = Double.parseDouble(doubleAsString);
			
			PlanningViewAbstractTreeTableSyncedTableModel model = getCastedModel().getCastedModel(getSelectedColumn());
			int columnWithinMultiTableModel = getSelectedColumnWithinMultiTableModel();
			model.updateFullTimeEmployeeDaysPerYearFraction(getSelectedRow(), columnWithinMultiTableModel, parsedDouble);
			
			dialog.dispose();
			dialog.setVisible(false);
		}
	}
	
	private PlanningUpperMultiTable multiTable;
	private MainWindow mainWindow;
	private PanelTextField fractionField;
	private PanelTextField errorField;
	private DialogWithButtonBar dialog;
}
