package main;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogueManager {
    private final GamePanel gp;
    private final HashMap<Integer, ArrayList<String>> dialogues = new HashMap<>();
    private int currentLineIndex = 0;

    public DialogueManager(GamePanel gp) {
        this.gp = gp;
        preloadDialogues();
    }

    private void preloadDialogues() {
        dialogues.put(1, new ArrayList<>(java.util.List.of(
                "Ryzen: \"Look at this place, a miserable den. Let's finish this swiftly. \"",
                "Krikk: \"I smell Orc blood! Fresh and warm... my brothers, prepare for feast!\""
        )));
        dialogues.put(2, new ArrayList<>(java.util.List.of(
                "Ryzen: \"Echoes of the fallen linger here... Time to lay them to rest.\"",
                "Krikk: \"Wait till you meet our GENERAL!\"",
                "Zarvok: \"An intruder dares disturb our eternal watch? Soldiers! Arise! Let him join our ranks!\""
        )));
        dialogues.put(3, new ArrayList<>(java.util.List.of(
                "Ryzen: \"So, it ends here—face to face with the darkness itself.\"",
                "Veidris: \"You’ve wandered far from the light, warrior. In my abyss, your blade means nothing!\""
        )));
    }

    public void startDialogue(int world) {
        currentLineIndex = 0;
        gp.currentDialogue = dialogues.get(world);
        gp.gameState = GamePanel.DIALOGUE_STATE;
    }

    public void nextLine() {
        currentLineIndex++;
        if (currentLineIndex >= gp.currentDialogue.size()) {
            gp.gameState = GamePanel.PLAY_STATE;
        }
    }

    public String getCurrentLine() {
        if (gp.currentDialogue == null || currentLineIndex >= gp.currentDialogue.size()) return "";
        return gp.currentDialogue.get(currentLineIndex);
    }
}
