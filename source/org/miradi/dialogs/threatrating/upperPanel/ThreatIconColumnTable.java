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
