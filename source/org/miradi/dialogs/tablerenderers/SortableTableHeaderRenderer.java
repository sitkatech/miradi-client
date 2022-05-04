/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.questions.SortDirectionQuestion;
import org.miradi.utils.MultiTableRowSortController;
import org.miradi.utils.SortableRowTable;
import org.miradi.utils.SortableTableModel;
import sun.swing.DefaultLookup;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import java.awt.*;

public class SortableTableHeaderRenderer extends DefaultTableCellHeaderRenderer
{
    public SortableTableHeaderRenderer(SortableRowTable sortableRowTableToUse)
    {
        sortableRowTable = sortableRowTableToUse;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel label = (JLabel) table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        try
        {
            MultiTableRowSortController rowSortController = sortableRowTable.getRowSortController();
            SortableTableModel model = (SortableTableModel) sortableRowTable.getModel();

            int columnToSortBy = rowSortController.findColumnToSortBy(model);
            if (columnToSortBy == column && model.shouldSortRows())
            {
                String sortDirectionCode = rowSortController.getColumnSortDirection(model);
                Icon sortIcon = getSortIcon(table, label, sortDirectionCode);
                label.setIcon(sortIcon);
            }
        }
        catch (Exception e)
        {
            EAM.logException(e);
            EAM.alertUserOfNonFatalException(e);
        }

        return label;
    }

    private Icon getSortIcon(JTable table, JLabel label, String sortDirectionCode)
    {
        String iconName = sortDirectionCode.equalsIgnoreCase(SortDirectionQuestion.DEFAULT_SORT_ORDER_CODE) ? "Table.ascendingSortIcon": "Table.descendingSortIcon";
        return DefaultLookup.getIcon(label, table.getTableHeader().getUI(), iconName);
    }

    private SortableRowTable sortableRowTable;
}
