/*
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.threatrating.upperPanel;

import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.dialogs.tablerenderers.FontForObjectProvider;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.main.MainWindow;

import javax.swing.*;
import java.awt.*;

public class ThreatIconColumnTable extends TableWhoseScrollPaneAlwaysExactlyFits
{
    public ThreatIconColumnTable(MainWindow mainWindowToUse, AbstractThreatPerRowTableModel tableModel)
    {
        super(mainWindowToUse, tableModel, tableModel.getUniqueTableModelIdentifier());

        iconCellRendererFactory = new BorderlessChoiceItemCellRendererFactory(tableModel, new DefaultFontProvider(getMainWindow()));
        getColumnModel().getColumn(ThreatIconColumnTableModel.THREAT_ICON_COLUMN_INDEX).setCellRenderer(iconCellRendererFactory);
        setColumnWidth(ThreatIconColumnTableModel.THREAT_ICON_COLUMN_INDEX, new DirectThreatIcon().getIconWidth() * 2);
    }

    @Override
    public boolean shouldSaveColumnSequence()
    {
        return false;
    }

    private BorderlessChoiceItemCellRendererFactory iconCellRendererFactory;
}

class BorderlessChoiceItemCellRendererFactory extends ChoiceItemTableCellRendererFactory
{
    public BorderlessChoiceItemCellRendererFactory(
            RowColumnBaseObjectProvider providerToUse,
            FontForObjectProvider fontProviderToUse)
    {
        super(providerToUse, fontProviderToUse);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int tableColumn)
    {
        final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, tableColumn);
        ((JComponent)component).setBorder(BorderFactory.createEmptyBorder());
        return component;
    }
}
