package dev.therealdan.realtimechess.screens.game;

import com.badlogic.gdx.Gdx;
import dev.therealdan.realtimechess.game.Board;
import dev.therealdan.realtimechess.game.Piece;
import dev.therealdan.realtimechess.main.RealTimeChessApp;
import dev.therealdan.realtimechess.main.Settings;
import dev.therealdan.realtimechess.network.DevicePeer;
import dev.therealdan.realtimechess.network.Server;
import dev.therealdan.realtimechess.network.packets.UsernamePacket;
import dev.therealdan.realtimechess.screens.GameScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerScreen extends GameScreen {

    private Server server;
    private String opponent;

    public ServerScreen(RealTimeChessApp app) {
        super(app);

        colour = app.settings.getToggle(Settings.Setting.PREFERENCE) ? Piece.Colour.WHITE : Piece.Colour.BLACK;
        board = Board.standardBoard();
        server = new Server((int) app.settings.getNumber(Settings.Setting.PORT));

        connectToClient();
    }

    private void connectToClient() {
        new Thread(() -> {
            getServer().accept();
            while (true) {
                try {
                    incoming(new BufferedReader(new InputStreamReader(getServer().getSocket().getInputStream())).readLine());
                } catch (IOException e) {
                    Gdx.app.log(getDevicePeer().getName(), "Error", e);
                }
            }
        }).start();
        new Thread(() -> {
            long checkInterval = 200;
            long lastCheck = System.currentTimeMillis() - checkInterval;
            while (opponent == null) {
                if (System.currentTimeMillis() - lastCheck < checkInterval) continue;
                lastCheck = System.currentTimeMillis();
                getDevicePeer().send(new UsernamePacket("?"));
            }
        }).start();
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    @Override
    public void dispose() {
        getServer().dispose();
    }

    public Server getServer() {
        return server;
    }

    @Override
    public String getOpponentName() {
        return opponent != null ? opponent : "";
    }

    @Override
    public boolean hasGameStarted() {
        return getServer().getSocket() != null;
    }

    @Override
    public DevicePeer getDevicePeer() {
        return getServer();
    }
}
