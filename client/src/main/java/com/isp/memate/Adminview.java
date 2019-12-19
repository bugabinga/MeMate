/**
 * © 2019 isp-insoft GmbH
 */
package com.isp.memate;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

/**
 * In der Adminview kann man das Guthaben des Spaarschweins setzen/sehen.
 * Außerdem kann man die gesamte Historie sehen und die Anzahl der Getränke festlegen.
 * 
 * @author nwe
 * @since 09.12.2019
 *
 */
public class Adminview extends JPanel
{
  private static final Adminview instance              = new Adminview();
  private final JButton          setAdminBalanceButton = new JButton( "Guthaben setzen" );
  private final JTextField       balanceField          = new JTextField();
  private final JLabel           piggyBankLabel        = new JLabel();
  private JPanel                 drinkAmountPanel      = new JPanel( new FlowLayout() );

  /**
   * @return static instance of Adminview
   */
  public static Adminview getInstance()
  {
    return instance;
  }

  /**
   * 
   */
  public Adminview()
  {
    setLayout( new GridBagLayout() );
    balanceField.setPreferredSize( new Dimension( 100, 20 ) );
    piggyBankLabel.setPreferredSize( new Dimension( 1100, 100 ) );
    piggyBankLabel.setFont( piggyBankLabel.getFont().deriveFont( 18f ) );
    piggyBankLabel.setHorizontalAlignment( JLabel.CENTER );

    GridBagConstraints piggybankLabelConstraints = new GridBagConstraints();
    piggybankLabelConstraints.gridx = 0;
    piggybankLabelConstraints.gridy = 0;
    piggybankLabelConstraints.gridwidth = 2;
    add( piggyBankLabel, piggybankLabelConstraints );
    GridBagConstraints balanceFieldConstraints = new GridBagConstraints();
    balanceFieldConstraints.gridx = 0;
    balanceFieldConstraints.gridy = 1;
    balanceFieldConstraints.weightx = 1;
    balanceFieldConstraints.anchor = GridBagConstraints.LINE_END;
    add( balanceField, balanceFieldConstraints );
    GridBagConstraints setAdminBalanceButtonConstraints = new GridBagConstraints();
    setAdminBalanceButtonConstraints.gridx = 1;
    setAdminBalanceButtonConstraints.gridy = 1;
    setAdminBalanceButtonConstraints.weightx = 1;
    setAdminBalanceButtonConstraints.anchor = GridBagConstraints.LINE_START;
    add( setAdminBalanceButton, setAdminBalanceButtonConstraints );
    setBackground( UIManager.getColor( "TabbedPane.highlight" ) );
    addAllDrinks();

    setAdminBalanceButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed( ActionEvent e )
      {
        String balanceAsString = balanceField.getText();
        Float balance = null;
        if ( isNumber( balanceAsString ) )
        {
          balance = Float.valueOf( balanceAsString );
          ServerCommunication.getInstance().setAdminBalance( Float.valueOf( balance ) );
          piggyBankLabel.setText( String.format( "Im Sparschwein befinden sich %.2f€", balance ) );
        }
        else
        {
          JOptionPane.showMessageDialog( Mainframe.getInstance(), "Bitte geben Sie einen gültigen Wert an." );
        }
      }

      private boolean isNumber( String balanceAsString )
      {
        try
        {
          Float.parseFloat( balanceAsString );
        }
        catch ( Exception exception )
        {
          return false;
        }
        return true;
      }
    } );
    ServerCommunication.getInstance().tellServerToSendPiggybankBalance();
  }


  /**
   * Fügt für jedes Getränk einen Spinner und ein Speicher-Button an
   */
  private void addAllDrinks()
  {
    int index = 2;
    for ( String drink : ServerCommunication.getInstance().getDrinkNames() )
    {
      drinkAmountPanel = new JPanel( new GridBagLayout() );
      JLabel drinkNameLabel = new JLabel( drink );
      GridBagConstraints drinkNameLabelConstraints = new GridBagConstraints();
      drinkNameLabelConstraints.gridy = 0;
      drinkNameLabelConstraints.gridx = 0;
      drinkNameLabelConstraints.anchor = GridBagConstraints.LINE_END;
      drinkNameLabelConstraints.weightx = 1;
      drinkAmountPanel.add( drinkNameLabel, drinkNameLabelConstraints );
      SpinnerModel amountSpinnerModel = new SpinnerNumberModel( 0, 0, 50, 1 );
      JSpinner amountSpinner = new JSpinner( amountSpinnerModel );
      amountSpinner.setValue( ServerCommunication.getInstance().getAmount( drink ) );
      GridBagConstraints amountSpinnerConstraints = new GridBagConstraints();
      amountSpinnerConstraints.gridx = 1;
      amountSpinnerConstraints.gridy = 0;
      amountSpinnerConstraints.weightx = 0;
      amountSpinnerConstraints.anchor = GridBagConstraints.LINE_END;
      amountSpinnerConstraints.insets = new Insets( 0, 5, 0, 0 );
      drinkAmountPanel.add( amountSpinner, amountSpinnerConstraints );
      JButton setAmountButton = new JButton( "Anzahl setzen" );
      GridBagConstraints setAmountButtonConstraints = new GridBagConstraints();
      setAmountButtonConstraints.gridx = 2;
      setAmountButtonConstraints.gridy = 0;
      setAmountButtonConstraints.weightx = 0;
      setAmountButtonConstraints.anchor = GridBagConstraints.LINE_END;
      setAmountButtonConstraints.insets = new Insets( 0, 5, 0, 462 );
      drinkAmountPanel.add( setAmountButton, setAmountButtonConstraints );
      setAmountButton.addActionListener( new ActionListener()
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          String amount = String.valueOf( amountSpinner.getValue() );
          if ( isInteger( amount ) )
          {
            ServerCommunication.getInstance().setDrinkAmount( drink, Integer.valueOf( amount ) );
          }
        }

        private boolean isInteger( String testValue )
        {
          try
          {
            Integer.parseInt( testValue );
          }
          catch ( Exception exception )
          {
            return false;
          }
          return true;
        }
      } );
      GridBagConstraints drinkAmountPanelConstraints = new GridBagConstraints();
      drinkAmountPanelConstraints.gridx = 0;
      drinkAmountPanelConstraints.gridwidth = 2;
      drinkAmountPanelConstraints.gridy = index;
      drinkAmountPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
      add( drinkAmountPanel, drinkAmountPanelConstraints );
      drinkAmountPanel.setBackground( UIManager.getColor( "TabbedPane.highlight" ) );
      index++;
    }
  }

  @SuppressWarnings( "javadoc" )
  public void updateDrinkAmounts()
  {
    remove( drinkAmountPanel );
    repaint();
    revalidate();
    addAllDrinks();
    repaint();
    revalidate();
  }

  /**
   * Updated das Label für das Spaarschweinguthaben.
   * 
   * @param balance Guthaben
   */
  public void updatePiggybankBalanceLabel( Float balance )
  {
    piggyBankLabel.setText( String.format( "Im Sparschwein befinden sich %.2f€", balance ) );
  }
}
