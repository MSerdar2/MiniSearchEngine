package minisearchengine.gui.scene;

import javax.swing.*;

// Abstraction over JFrame, to extends its functionalities
public abstract class ProgramScene extends JFrame {

    public ProgramScene() {
        super();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Chances the title of the window. Yet, keeps the main name prepended
     *
     * @param title Title to be set
     */
    protected void changeTitle(String title) {
        this.setTitle("MiniSearchEngine - " + title);
    }

    /**
     * Turns given raw string into multi-line HTML expressions. <br/>
     * Used by JLabel components, to be able to render multiple lines
     * @param text Text to be processed
     * @return HTML statement
     */
    protected String toWrappedText(String text) {
        return "<html>"
                + text.replace("\n", "<br/>")
                + "</html>";
    }

}
