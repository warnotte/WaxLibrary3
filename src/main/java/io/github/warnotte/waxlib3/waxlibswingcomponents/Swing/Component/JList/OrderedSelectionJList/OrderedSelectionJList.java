package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.JList.OrderedSelectionJList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.*;

/**
 * Cette classe permet de retenir l'ordre de selection fait par l'utilisateur.
 * Contrairement a Jlist qui donne la selection dans l'ordre de la liste du dessus vers le dessous.
 * @param <E>
 */
public class OrderedSelectionJList<E> extends JList<E> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4241183741698054954L;
	private List<E> selectionOrder;

    public OrderedSelectionJList(ListModel<E> model) {
        super(model);
        this.selectionOrder = new ArrayList<>();
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Écouteur de sélection pour les changements programmatiques
        this.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
               // if (!e.getValueIsAdjusting()) {
            	updateSelectionOrder();
               //}
            }
        });
    }

    // Mise à jour de l'ordre de sélection
    private void updateSelectionOrder() {
        int[] selectedIndices = super.getSelectedIndices();
        if (selectedIndices.length == 0) {
            selectionOrder.clear();
            return;
        }

        List<E> currentSelection = new ArrayList<>();
        for (int index : selectedIndices) {
            currentSelection.add(getModel().getElementAt(index));
        }

        // Ajouter les nouveaux éléments sélectionnés à la fin
        for (E item : currentSelection) {
            if (!selectionOrder.contains(item)) {
                selectionOrder.add(item);
            }
        }

        // Supprimer les éléments désélectionnés
        Iterator<E> iterator = selectionOrder.iterator();
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (!currentSelection.contains(item)) {
                iterator.remove();
            }
        }
    }

    @Override
    public List<E> getSelectedValuesList() {
    	updateSelectionOrder();
        return new ArrayList<>(selectionOrder);
    }

    @Override
    public int[] getSelectedIndices() {
    	updateSelectionOrder();
        int[] indices = new int[selectionOrder.size()];
        for (int i = 0; i < selectionOrder.size(); i++) {
            E value = selectionOrder.get(i);
            indices[i] = getIndexForValue(value);
        }
        return indices;
    }

    @Override
    public int getSelectedIndex() {
    	updateSelectionOrder();
        if (selectionOrder.isEmpty()) {
            return -1;
        }
        return getIndexForValue(selectionOrder.get(0));
    }

    @Override
    public E getSelectedValue() {
    	updateSelectionOrder();
        if (selectionOrder.isEmpty()) {
            return null;
        }
        return selectionOrder.get(0);
    }

    private int getIndexForValue(E value) {
    	updateSelectionOrder();
        ListModel<E> model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (Objects.equals(model.getElementAt(i), value)) {
                return i;
            }
        }
        return -1;
    }
}
