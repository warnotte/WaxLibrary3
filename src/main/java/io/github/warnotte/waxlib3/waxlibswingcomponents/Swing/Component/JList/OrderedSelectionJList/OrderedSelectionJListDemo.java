package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.JList.OrderedSelectionJList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class OrderedSelectionJListDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Démo OrderedSelectionJList");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Élément 1");
        model.addElement("Élément 2");
        model.addElement("Élément 3");
        model.addElement("Élément 4");
        model.addElement("Élément 5");

        OrderedSelectionJList<String> orderedList = new OrderedSelectionJList<>(model);
        JScrollPane scrollPane = new JScrollPane(orderedList);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton showSelectionButton = new JButton("Afficher Sélection");
        frame.add(showSelectionButton, BorderLayout.SOUTH);

        showSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Premier élément sélectionné : " + orderedList.getSelectedValue());
                System.out.println("Indice du premier élément sélectionné : " + orderedList.getSelectedIndex());
                System.out.println("Éléments sélectionnés : " + orderedList.getSelectedValuesList());
                System.out.println("Indices sélectionnés : " + Arrays.toString(orderedList.getSelectedIndices()));
                System.out.println("Indice de la première sélection : " + orderedList.getMinSelectionIndex());
                System.out.println("Indice de la dernière sélection : " + orderedList.getMaxSelectionIndex());
            }
        });

        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
