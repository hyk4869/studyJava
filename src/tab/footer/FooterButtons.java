package src.tab.footer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;

import src.components.parts.CustomButton;

public class FooterButtons {

  private final GridBagConstraints gbc = new GridBagConstraints();

  private CustomButton customFooterButton;

  public FooterButtons() {
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(10, 0, 0, 0);
  }

  public CustomButton generateFooterButton(String title, ActionListener callback, boolean isVisible, Color color) {
    this.customFooterButton = new CustomButton();
    customFooterButton.setVisible(isVisible);
    customFooterButton.addButton(title, gbc.gridx, gbc.gridy, gbc.gridwidth, callback, color);
    return customFooterButton;
  }

  public void getButton() {
    System.out.println(this.customFooterButton.getText());
  }
}
