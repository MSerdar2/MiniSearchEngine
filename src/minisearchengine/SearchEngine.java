/* Name: Serdar MUSTAFAPASA
 * Project: MiniSearchEngine
 * Last modification date: 25.05.2021
 */

package minisearchengine;

import minisearchengine.auth.PasswordEncrypter;
import minisearchengine.gui.scene.FirstTimeLoginScene;
import minisearchengine.gui.scene.LoginScene;
import minisearchengine.gui.scene.ProgramScene;

public class SearchEngine {

    public static ProgramScene currentScene;

    public static void main(String[] args) {
        if (PasswordEncrypter.loadCurrentPassword() == null) {
            switchScene(new FirstTimeLoginScene());
        } else {
            switchScene(new LoginScene());
        }
    }

    /**
     * Switches current scene with given scene
     * @param scene Scene to be switched with
     */
    public static void switchScene(ProgramScene scene) {
        ProgramScene previousScene = currentScene;

        currentScene = scene;

        if (previousScene != null) {
            previousScene.dispose();
        }

        currentScene.setLocationRelativeTo(null);
        currentScene.setVisible(true);
    }

}
