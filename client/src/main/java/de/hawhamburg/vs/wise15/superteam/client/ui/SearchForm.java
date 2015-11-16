package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class SearchForm {
    private JPanel panel;
    private JList list1;
    private JList list2;
    private JButton backButton;
    private JTextField textField1;
    private JButton enterGameButton;


    public SearchForm(Client client) {


        ListSelectionModel selectionModel = list1.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            String selection = (String) list1.getSelectedValue();

            DefaultListModel<String> playerListModel = new DefaultListModel<>();

            switch (selection) {
                case "Spiel 1":
                    playerListModel.addElement("Spieler 1.1");
                    playerListModel.addElement("Spieler 1.2");
                    playerListModel.addElement("Spieler 1.3");
                    break;

                case "Spiel 2":
                    playerListModel.addElement("Spieler 2.1");
                    playerListModel.addElement("Spieler 2.2");
                    playerListModel.addElement("Spieler 2.3");
                    break;
            }

            list2.setModel(playerListModel);
        });
        backButton.addActionListener(e -> client.openStartForm());
        enterGameButton.addActionListener(e -> client.openLobbyForm());
    }


    public JPanel getPanel() {

        return panel;
    }
}
