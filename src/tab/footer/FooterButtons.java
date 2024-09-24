package src.tab.footer;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;

import src.components.parts.CustomButton;
import src.utils.CommonColor;

public class FooterButtons {

  private final GridBagConstraints gbc = new GridBagConstraints();
  private CommonColor commonColor = new CommonColor();

  private CustomButton saveButton = new CustomButton();
  private CustomButton reloadButton = new CustomButton();
  private CustomButton deleteButton = new CustomButton();
  private CustomButton editButton = new CustomButton();

  public FooterButtons() {
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(10, 0, 0, 0);
  }

  public CustomButton generateSaveButton(ActionListener callback) {
    saveButton.setVisible(false);
    saveButton.addButton("Save", gbc.gridx, gbc.gridy, gbc.gridwidth, callback, commonColor.commonMUIBlue());
    return saveButton;
  }

  public CustomButton generateReloadButoon(ActionListener callback) {
    reloadButton.addButton("Reload", gbc.gridx, gbc.gridy, gbc.gridwidth, callback, commonColor.commonMUIBlue());
    return reloadButton;
  }

  public CustomButton generateDeleteButoon(ActionListener callback) {
    deleteButton.setVisible(false);
    deleteButton.addButton("Delete", gbc.gridx, gbc.gridy, gbc.gridwidth, callback, commonColor.commonMUIRed());
    return deleteButton;
  }

  public CustomButton generateEditButoon(ActionListener callback) {
    editButton.addButton("Edit", gbc.gridx, gbc.gridy, gbc.gridwidth, callback, commonColor.commonMUIBlue());
    return editButton;
  }
}
