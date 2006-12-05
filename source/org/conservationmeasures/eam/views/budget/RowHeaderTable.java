/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiTable;

public class RowHeaderTable extends UiTable
{
	public RowHeaderTable(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	private void rebuild()
	{
		DefaultTableModel model = (DefaultTableModel)getModel();
	    model.addColumn(COLUMN_HEADER_TITLE, new String[]{""});

		projectResources = project.getAllProjectResources();
		String[] resourceNames = new String[projectResources.length];
		resourceCombo = new UiComboBox();
		for (int i = 0; i < projectResources.length; i++)
			resourceNames[i] = projectResources[i].getData(ProjectResource.TAG_NAME);
		
		 int vColIndex = 0;
		 TableColumn col = getColumnModel().getColumn(vColIndex);
		 col.setCellEditor(new ComboBoxEditor(resourceNames));
		 col.setCellRenderer(new ComboBoxRenderer(resourceNames));
	}
	
	Project project;
	UiComboBox resourceCombo;
	ProjectResource[] projectResources;
	RowHeaderTableModel rowHeaderModel;
	static final String COLUMN_HEADER_TITLE = EAM.text("Resource Names");
}


class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
    public ComboBoxRenderer(String[] items) {
        super(items);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setSelectedItem(value);
        return this;
    }
}

class ComboBoxEditor extends DefaultCellEditor {
    public ComboBoxEditor(String[] items) {
        super(new JComboBox(items));
    }
}

