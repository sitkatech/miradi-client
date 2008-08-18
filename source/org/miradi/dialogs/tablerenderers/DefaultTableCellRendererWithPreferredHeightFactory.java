package org.miradi.dialogs.tablerenderers;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DefaultTableCellRendererWithPreferredHeightFactory extends
		BasicTableCellRendererFactory implements TableCellPreferredHeightProvider
{
	public DefaultTableCellRendererWithPreferredHeightFactory()
	{
		rendererComponent = new DefaultTableCellRenderer();
	}
	
	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected,
			boolean hasFocus, int row, int tableColumn, Object value)
	{
		return (JComponent)rendererComponent.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
	}

	public int getPreferredHeight(Object value)
	{
		// FIXME: Do the real calculation here
		return rendererComponent.getFont().getSize();
	}

	private DefaultTableCellRenderer rendererComponent;
}
