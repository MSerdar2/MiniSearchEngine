package minisearchengine.gui.adapter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface DocumentChangeAdapter extends DocumentListener {

    void onDocumentChanged(DocumentEvent event);

    @Override
    default void insertUpdate(DocumentEvent e) {
        onDocumentChanged(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        onDocumentChanged(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        onDocumentChanged(e);
    }

}
