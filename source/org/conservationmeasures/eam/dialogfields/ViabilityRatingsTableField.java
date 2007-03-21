/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.utils.SingleClickAutoSelectCellEditor;

public class ViabilityRatingsTableField extends ObjectStringMapTableField
{

	public ViabilityRatingsTableField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse);
		model = (DefaultTableModel)table.getModel();
		model.insertRow(1, new String[]{});
		model.insertRow(2, new String[]{});
		question = questionToUse;
		table.setDefaultRenderer(Object.class, new TableCellRenderer());
		table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
		table.setShowHorizontalLines(false);
		currentRowHeight = table.getRowHeight();
		setSingleCellEditor();
	}


	private void setSingleCellEditor()
	{
		int colCount = question.getChoices().length-1;
		for (int i = 0; i < colCount; i++)
		{
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setCellEditor(new SingleClickAutoSelectCellEditor(new JTextField()));
		}
	}
	
	
	private void setIconRow(ORef oref)
	{	
		EAMObject object = getProject().findObject(oref);
		if (object.getType() == ObjectType.GOAL)
		{
			detailSummary = object.getData(Goal.TAG_DESIRED_SUMMARY);
			detailStatusCode = object.getData(Goal.TAG_DESIRED_STATUS);
		}
		else if (object.getType() == ObjectType.INDICATOR)
		{
			measurementSummary = object.getData(Indicator.TAG_MEASUREMENT_SUMMARY);
			measurementStatusCode = object.getData(Indicator.TAG_MEASUREMENT_STATUS);
		}
	}
	
	public void showThreshold(boolean show)
	{
			if (show)
			{
				table.setRowHeight(0, currentRowHeight);
			}
			else
			{
				table.setRowHeight(0, 1);
			}
	}
	

	public void showStatus(boolean show)
	{
			if (show)
			{
				table.setRowHeight(1, currentRowHeight);
				table.setRowHeight(2, currentRowHeight);
			}
			else
			{
				table.setRowHeight(1, 1);
				table.setRowHeight(2, 1);
			}
	}
	
	public void setIconRowObject(ORef oref)
	{
		if (oref==null)
			clearIconRows();
		else
			setIconRow(oref);
		
		table.repaint();
	}
	
	private void clearIconRows()
	{
		measurementStatusCode = "";
		detailStatusCode = "";
		
		for (int i=0; i<4; ++i)
		{
			model.setValueAt("", 1, i);
			model.setValueAt("", 2, i);
		}
	}
	
	
	public void repaint()
	{
		table.repaint();
	}
	
	class TableCellRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable tableToUse, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			
			JComponent comp =  new JTextField((String)value);
			((JTextField)comp).setHorizontalAlignment(JTextField.CENTER);

			
			if ((row==1) && validCode(measurementStatusCode) && Integer.parseInt(measurementStatusCode)-1 == column)
			{
				comp =  new JLabel(measurementSummary, new IndicatorIcon(), JLabel.CENTER);
			}
			
			if ((row==2) && validCode(detailStatusCode)  && Integer.parseInt(detailStatusCode)-1 == column)
			{
				comp =  new JLabel(detailSummary, new GoalIcon(), JLabel.CENTER);
			}

			return getComponent(comp, column);
		}
		
		
		private JComponent getComponent(JComponent comp, int column)
		{
			if (validCode(measurementStatusCode) && Integer.parseInt(measurementStatusCode)-1 == column)
			{
				comp.setOpaque(true);
				comp.setBackground(question.getChoices()[Integer.parseInt(measurementStatusCode)].getColor());
			}
			updateEditableState(comp);
			return comp;
		}

		private boolean validCode(String code)
		{
			try
			{
				Integer.parseInt(code);
			}
			catch (Exception e)
			{
				return false;
			}
			return true;
		}
		

		public void updateEditableState(JComponent comp)
		{
			boolean editable = allowEdits() && isValidObject();
			comp.setEnabled(editable);
			if(!editable)
			{
				comp.setForeground(EAM.READONLY_FOREGROUND_COLOR);
				comp.setBackground(EAM.READONLY_BACKGROUND_COLOR);
			}
		}

	}
	
	class HeaderRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable tableToUse, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			setOpaque(false);
			JTextField field = new JTextField((String)value);
			field.setBackground(question.getChoices()[column+1].getColor());
			return field;
		}
		
	}
	
	

	
	String measurementStatusCode;
	String measurementSummary;
	String detailStatusCode;
	String detailSummary;
	ChoiceQuestion question;
	DefaultTableModel model;
	int currentRowHeight;
}
