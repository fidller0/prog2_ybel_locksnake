package de.hsbi.lockgame;

import de.hsbi.lockgame.io.LevelLoader;
import de.hsbi.lockgame.logic.GameEngine;
import de.hsbi.lockgame.logic.GameState;
import de.hsbi.lockgame.settings.GameConstants;
import de.hsbi.lockgame.settings.LevelConstants;
import de.hsbi.lockgame.ui.GamePanel;
import de.hsbi.lockgame.ui.render.Java2DRenderer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> {
                    try {
                        var level =
                                LevelLoader.loadLevelFromResource(LevelConstants.defaultLevel());
                        var engine = new GameEngine(level);

                        var panel = new GamePanel(engine.state(), new Java2DRenderer());

                        engine.setGamePanel(panel);
                        panel.setGameEngine(engine);

                        var frame = new JFrame("LockSnake - Prog2");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLayout(new BorderLayout());
                        frame.add(panel, BorderLayout.CENTER);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setResizable(false);
                        frame.setVisible(true);

                        new Timer(
                                        GameConstants.TICK_MS,
                                        e -> {
                                            engine.tick();
                                            handleGameEnd(e, engine.state(), frame);
                                        })
                                .start();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Fehler beim Starten des Spiels. " + e.getMessage(),
                                "Fehler",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
    }

    private static void handleGameEnd(ActionEvent event, GameState state, JFrame frame) {
        if (!state.status().isRunning()) {
            ((Timer) event.getSource()).stop();

            JOptionPane.showMessageDialog(
                    frame,
                    switch (state.status()) {
                        case WON -> "Glückwunsch! Sie haben alle Pins gesetzt.";
                        case LOST_SELF_COLLISION -> "Game over: Die Schlange hat sich gebissen.";
                        case LOST_OUT_OF_BOUNDS -> "Game over: Aus dem Spielfeld gefallen.";
                        default -> "Spiel beendet.";
                    });
        }
    }
}
