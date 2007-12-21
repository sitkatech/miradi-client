/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTextField;
import org.conservationmeasures.eam.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;
import org.conservationmeasures.eam.utils.ColumnTagProvider;
import org.conservationmeasures.eam.utils.SingleClickAutoSelectCellEditor;
import org.conservationmeasures.eam.utils.StringMapData;
import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;
import org.martus.swing.UiScrollPane;

public class ViabilityRatingsTableField extends ObjectDataInputField
{

	public ViabilityRatingsTableField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse.getTag());
		question = questionToUse;
		model = new MyTableModel(question);
		table = new MyTable(model);
		
		UiScrollPane requiredToMakeTableHeaderVisible = new UiScrollPane(table);
		requiredToMakeTableHeaderVisible.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		requiredToMakeTableHeaderVisible.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		component = requiredToMakeTableHeaderVisible;

	}
	
	
	public JComponent getComponent()
	{
		return component;
	}


	public String getText()
	{
		try
		{
			return model.getText();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	public void setText(String dataString)
	{
		try
		{
			table.editingCanceled(null);
			table.clearSelection();
			model.setText(dataString);
			table.repaint();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void updateEditableState()
	{
		boolean editable = allowEdits() && isValidObject();
		table.setEnabled(editable);
		Color fg = EAM.EDITABLE_FOREGROUND_COLOR;
		Color bg = EAM.EDITABLE_BACKGROUND_COLOR;
		if(!editable)
		{
			fg = EAM.READONLY_FOREGROUND_COLOR;
			bg = EAM.READONLY_BACKGROUND_COLOR;
		}
		table.setForeground(fg);
		table.setBackground(bg);
	}

	public void saveSelection()
	{
		setNeedsSave();
		saveIfNeeded();
	}
	
	public void dataHasChanged()
	{
		table.repaint();
	}
	
	
	class MyTableModel extends AbstractTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
	{
		public MyTableModel(ChoiceQuestion questionToUse)
		{
			question = questionToUse;
			trendQuestion = new TrendQuestion("");
			data = new StringMapData();
		}
		
		public int getColumnCount()
		{
			return columnCodes.length;
		}

		public int getRowCount()
		{
			return 3;
		}

		public void setValueAt(Object aValue, int rowIndex, int columnIndex)
		{
			if(!isThresholdRow(rowIndex))
			{
				EAM.logWarning("Attempted to setValueAt for row " + rowIndex);
				return;
			}
			
			String code = getChoiceItem(columnIndex).getCode();
			data.add(code, (String)aValue);
			saveSelection();
		}
		
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			
			if(isThresholdRow(rowIndex))
				return getThresholdValue(columnIndex);
			if(rowIndex == 1)
				return getCurrentStatus(columnIndex);
			if(rowIndex == 2)
				return getFutureStatus(columnIndex);
			
			EAM.logError("Unknown row: " + rowIndex);
			return null;
		}

		private Object getThresholdValue(int columnIndex)
		{
			String code = getChoiceItem(columnIndex).getCode();
			return data.get(code);
		}

		private Object getCurrentStatus(int columnIndex)
		{
			Indicator indicator = getIndicator();
			ORef latestMeasurementRef = indicator.getLatestMeasurementRef();
			if(latestMeasurementRef.isInvalid())
				return null;
			Measurement measurement = (Measurement)getProject().findObject(latestMeasurementRef);
			String statusCode = measurement.getData(Measurement.TAG_STATUS);
			if(!statusCode.equals(getChoiceItem(columnIndex).getCode()))
				return null;
			String summary = measurement.getData(Measurement.TAG_SUMMARY);
			String trendCode = measurement.getData(Measurement.TAG_TREND);
			Icon trendIcon = trendQuestion.findChoiceByCode(trendCode).getIcon();
			
			// FIXME: After renderer is fixed, return ChoiceItem
			//return new ChoiceItem(summary, summary, trendIcon);
			return new JLabel(summary, trendIcon, SwingConstants.LEADING);
		}

		private Indicator getIndicator()
		{
			return (Indicator) getProject().findObject(getORef());
		}

		private Object getFutureStatus(int columnIndex)
		{
			Indicator indicator = getIndicator();
			if(indicator == null)
				return null;
			String futureStatusCode = indicator.getData(Indicator.TAG_FUTURE_STATUS_RATING);
			if(!futureStatusCode.equals(getChoiceItem(columnIndex).getCode()))
				return null;
			String futureValue = indicator.getData(Indicator.TAG_FUTURE_STATUS_SUMMARY);

			// FIXME: After renderer is fixed, return ChoiceItem
//			return new ChoiceItem(futureValue, futureValue, new GoalIcon());
			return new JLabel(futureValue, new GoalIcon(), SwingConstants.LEADING);

		}

		public String getColumnTag(int column)
		{
			return getChoiceItem(column).getCode();
		}

		public String getColumnName(int column)
		{
			return getChoiceItem(column).getLabel();
		}
		
		public void setText(String newData) throws Exception
		{
			data.set(newData);
		}
		
		public String getText()
		{
			return data.get();
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return isThresholdRow(rowIndex);
		}
		
		public BaseObject getBaseObjectForRowColumn(int row, int column)
		{
			return getIndicator();
		}

		public boolean isThresholdRow(int row)
		{
			return (row == 0);
		}

		private ChoiceItem getChoiceItem(int column)
		{
			return question.findChoiceByCode(columnCodes[column]);
		}
		
		private String[] columnCodes =  {
				StatusQuestion.POOR,
				StatusQuestion.FAIR,
				StatusQuestion.GOOD,
				StatusQuestion.VERY_GOOD,
		};

		private ChoiceQuestion question;
		private TrendQuestion trendQuestion;
		private StringMapData data;
	}
		
	class MyTable extends TableWithColumnWidthSaver
	{
		public MyTable(MyTableModel model)
		{
			super(model);
			setSingleCellEditor();
			Dimension preferredSize = getPreferredSize();
			preferredSize.width = 600;
			setPreferredScrollableViewportSize(preferredSize);
			measurementRenderer = new JLabelRenderer();
			getTableHeader().setDefaultRenderer(new HeaderRenderer());

		}
		
		public String getUniqueTableIdentifier()
		{
			return UNIQUE_IDENTIFIER;
		}

		public Component prepareEditor(TableCellEditor editor, int row, int column)
		{
			Component c = super.prepareEditor(editor, row, column);
			if (c instanceof JTextComponent)
			{
				if (row==0)
				{
					((JTextField)c).selectAll();
				}
			}

			return c;
		}

		public TableCellRenderer getCellRenderer(int row, int column)
		{
			if(model.isThresholdRow(row))
				return super.getCellRenderer(row, column);
			return measurementRenderer;
		}

		public boolean isCellEditable(int row, int column)
		{
			return (row==0);
		}
		
		private void setSingleCellEditor()
		{
			int colCount = question.getChoices().length-1;
			for (int i = 0; i < colCount; i++)
			{
				TableColumn column = getColumnModel().getColumn(i);
				column.setCellEditor(new SingleClickAutoSelectCellEditor(new PanelTextField()));
			}
		}
		
		private JLabelRenderer measurementRenderer;
	
		public static final String UNIQUE_IDENTIFIER = "IndicatorRatingsTable";

	}
	
	// FIXME: This should really be very similar to MeasurementValueRenderer,
	// and the model should return ChoiceItems instead of JLabels
	class JLabelRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable tableToUse, Object value,
					boolean isSelected, boolean hasFocus, int row, int column)
		{
			JComponent label = (JComponent)value;
			if(label == null)
				return null;
			
			return label;
		}
		
	}
	
	class HeaderRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable tableToUse, Object value,
				boolean isSelected, boolean hasFocus, int row, int tableColumn)
		{
			setOpaque(false);
			JTextField field = new PanelTextField((String)value);
			field.setFont(field.getFont().deriveFont(Font.BOLD));
			field.setHorizontalAlignment(JTextField.CENTER);
			int modelColumn = tableToUse.convertColumnIndexToModel(tableColumn);
			field.setBackground(model.getChoiceItem(modelColumn).getColor());
			return field;
		}
	}
	
	private ChoiceQuestion question;
	private MyTable table;
	private MyTableModel model;
	private JComponent component;
}
