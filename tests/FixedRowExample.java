/* (swing1.1.1beta1) */
//package jp.gr.java_conf.tame.swing.examples;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

/**
 * @version 1.0 03/05/99
 */
public class FixedRowExample extends JFrame {
  Object[][] data;
  Object[] column;
  JTable fixedTable,table;
  private int FIXED_NUM = 2;

  public FixedRowExample() {
    super( "Fixed Row Example" );
    
    data =  new Object[][]{
        {      "a","","","","",""},
        {      "","b","","","",""},
        {      "","","c","","",""},
        {      "","","","d","",""},
        {      "","","","","e",""},
        {      "","","","","","f"},
        {"fixed1","","","","","","",""},
        {"fixed2","","","","","","",""}};
    column = new Object[]{"A","B","C","D","E","F"};
        
    AbstractTableModel    model = new AbstractTableModel() {
      public int getColumnCount() { return column.length; }
      public int getRowCount() { return data.length - FIXED_NUM; }
      public String getColumnName(int col) {
       return (String)column[col]; 
      }
      public Object getValueAt(int row, int col) { 
        return data[row][col]; 
      }
      public void setValueAt(Object obj, int row, int col) { 
        data[row][col] = obj; 
      }
      public boolean CellEditable(int row, int col) { 
        return true; 
      }
    };
    
    AbstractTableModel fixedModel = new AbstractTableModel() {      
      public int getColumnCount() { return column.length; }
      public int getRowCount() { return FIXED_NUM; }
      public Object getValueAt(int row, int col) { 
        return data[row + (data.length - FIXED_NUM)][col]; 
      }
    };
    
    table = new JTable( model );
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    fixedTable = new JTable( fixedModel );
    fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    JScrollPane scroll      = new JScrollPane( table );
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    
    JScrollPane fixedScroll = new JScrollPane( fixedTable ) {
      public void setColumnHeaderView(Component view) {} // work around
    };                                                   //
                                             // fixedScroll.setColumnHeader(null); 
    
    fixedScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    JScrollBar bar = fixedScroll.getVerticalScrollBar();
    JScrollBar dummyBar = new JScrollBar() {
      public void paint(Graphics g) {}
    };
    dummyBar.setPreferredSize(bar.getPreferredSize());
    fixedScroll.setVerticalScrollBar(dummyBar);
    
    final JScrollBar bar1 = scroll.getHorizontalScrollBar();
    JScrollBar bar2 = fixedScroll.getHorizontalScrollBar();
    bar2.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        bar1.setValue(e.getValue());
      }
    });
    
    
    scroll.setPreferredSize(new Dimension(400, 100));
    fixedScroll.setPreferredSize(new Dimension(400, 52));  // Hmm...
    getContentPane().add(     scroll, BorderLayout.CENTER);
    getContentPane().add(fixedScroll, BorderLayout.SOUTH);    
  }

  public static void main(String[] args) {
    FixedRowExample frame = new FixedRowExample();
    frame.addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent e ) {
	System.exit(0);
      }
    });
    frame.pack();
    frame.setVisible(true);
  }
}

