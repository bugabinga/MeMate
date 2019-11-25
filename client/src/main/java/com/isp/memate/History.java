/**
 * © 2019 isp-insoft GmbH
 */
package com.isp.memate;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * In der Historie soll der Nutzer alle bisherigen Buchungen (auch von Kollegen) sehen, egal ob etwas
 * gekauft oder das Konto aufgeladen wurde. Die Buchungen sollen in Aktion, Konsument,
 * Transaktionsmenge, neuer Kontostand und Datum angezeigt werden.
 * 
 * @author nwe
 * @since 15.10.2019
 */
public class History extends JPanel
{
  /**
   * Erzeugt einen Table mit Beispieldaten und fügt diesen dem HistoryPanel hinzu.
   */
  public History()
  {
    super( new BorderLayout() );
    final String[] columnNames = { "Aktion", "Konsument", "Transakstionsmenge", "Neuer Kontostand", "Datum" };
    final JTable historyTable = new JTable( ServerCommunication.getInstance().getHistoryData(), columnNames );
    final DefaultTableModel tableModel = new DefaultTableModel( ServerCommunication.getInstance().getHistoryData(), columnNames )
    {
      @Override
      public boolean isCellEditable( int row, int column )
      {
        return false;
      }
    };
    historyTable.setModel( tableModel );
    setBackground( UIManager.getColor( "TabbedPane.highlight" ) );

    final JScrollPane scrollPane = new JScrollPane();
    scrollPane.setBackground( UIManager.getColor( "TabbedPane.highlight" ) );
    scrollPane.setBorder( BorderFactory.createEmptyBorder() );
    scrollPane.setViewportView( historyTable );
    add( scrollPane );
  }
}
