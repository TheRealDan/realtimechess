package dev.therealdan.realtimechess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import dev.therealdan.realtimechess.game.Bot;
import dev.therealdan.realtimechess.main.Mouse;
import dev.therealdan.realtimechess.main.RealTimeChessApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenuScreen implements Screen, InputProcessor {

    final RealTimeChessApp app;

    private ScreenViewport viewport;
    private OrthographicCamera camera;

    private Texture black;
    private Texture firebrick;
    private Texture brown;

    private Option hovering = null;
    private Option menu = null;
    private Bot.Difficulty difficulty = null;

    public MainMenuScreen(RealTimeChessApp app) {
        this.app = app;

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        black = new Texture("images/black.png");
        firebrick = new Texture("images/firebrick.png");
        brown = new Texture("images/brown.png");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.2f, 0.1f, 1);

        camera.update();
        app.shapeRenderer.setProjectionMatrix(camera.combined);
        app.batch.setProjectionMatrix(camera.combined);

        float oheight = Gdx.graphics.getHeight() * 0.8f;
        float height = Gdx.graphics.getHeight() - oheight;
        float y = Gdx.graphics.getHeight() / 2f;

        String title = menu != null && menu.equals(Option.BOTS) ? "Choose your opponent" : "Real Time Chess";

        app.batch.begin();
        app.font.center(app.batch, title, 0, y - height / 2f, (int) (40f * app.font.scale), Color.WHITE);
        app.batch.end();

        y -= height;

        if (menu == null) {
            menu(y, oheight);
        } else {
            bots(y, oheight);
        }
    }

    private void menu(float oy, float oheight) {
        List<Option> options = new ArrayList<>();
        options.add(Option.BOTS);
        options.add(Option.QUIT);

        float spacing = Gdx.graphics.getHeight() / 25f;
        float width = Gdx.graphics.getWidth() * 0.4f;
        float height = Math.min(oheight / options.size() - spacing, 80f);
        float theight = (height + spacing) * options.size();
        float x = -width / 2f;
        float y = oy - oheight / 2f + theight / 2f - height;

        hovering = null;
        for (Option option : options) {
            if (Mouse.containsMouse(x, y, width, height)) hovering = option;
            app.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            app.shapeRenderer.setColor(Mouse.containsMouse(x, y, width, height) ? Color.BROWN : Color.FIREBRICK);
            app.shapeRenderer.rect(x, y, width, height);
            app.shapeRenderer.end();

            app.batch.begin();
            app.font.center(app.batch, option.getName(), x + width / 2f, y + height / 2f, (int) (16f * app.font.scale), Color.WHITE);
            app.batch.end();

            y -= height + spacing;
        }
    }

    private void bots(float oy, float oheight) {
        if (difficulty == null) difficulty = Arrays.stream(Bot.Difficulty.values()).findFirst().get();
        hovering = null;

        float spacing = Gdx.graphics.getHeight() / 25f;
        float height = oheight * 0.8f;
        float width = Math.min((Gdx.graphics.getWidth() - spacing * 3f) / 2f, height);
        height = width;

        float buttonWidth = width * 0.4f;
        float buttonHeight = Math.min(buttonWidth / 2f, 80f);

        float x = -spacing / 2f - width;
        float y = oy - height;

        app.batch.begin();
        app.batch.setColor(Color.WHITE);
        app.batch.draw(difficulty.getTexture(), x, y, width, height);
        x += width + spacing;
        app.batch.draw(black, x, y, width, height);

        float ox = x;
        float buttonX = ox + width / 2f - buttonWidth / 2f;
        float buttonY = y + spacing;
        int buttonFontSize = (int) (12f * app.font.scale);

        x += spacing / 2f;
        y += height - spacing / 2f;
        app.font.draw(app.batch, difficulty.getName() + " " + difficulty.getDifficulty(), x, y, (int) (20f * app.font.scale), Color.WHITE);
        y -= spacing * 2f;
        app.font.draw(app.batch, difficulty.getDescription(), x, y, width - spacing, (int) (12f * app.font.scale), Color.WHITE);

        if (Mouse.containsMouse(buttonX, buttonY, buttonWidth, buttonHeight)) hovering = Option.PLAY;
        app.batch.draw(Mouse.containsMouse(buttonX, buttonY, buttonWidth, buttonHeight) ? brown : firebrick, buttonX, buttonY, buttonWidth, buttonHeight);
        app.font.center(app.batch, Option.PLAY.getName(), buttonX + buttonWidth / 2f, buttonY + buttonHeight / 2f, buttonFontSize, Color.WHITE);
        buttonWidth /= 2f;
        buttonX = ox + spacing;
        if (Mouse.containsMouse(buttonX, buttonY, buttonWidth, buttonHeight)) hovering = Option.PREVIOUS;
        app.batch.draw(Mouse.containsMouse(buttonX, buttonY, buttonWidth, buttonHeight) ? brown : firebrick, buttonX, buttonY, buttonWidth, buttonHeight);
        app.font.center(app.batch, Option.PREVIOUS.getName(), buttonX + buttonWidth / 2f, buttonY + buttonHeight / 2f, buttonFontSize, Color.WHITE);
        buttonX = ox + width - spacing - buttonWidth;
        if (Mouse.containsMouse(buttonX, buttonY, buttonWidth, buttonHeight)) hovering = Option.NEXT;
        app.batch.draw(Mouse.containsMouse(buttonX, buttonY, buttonWidth, buttonHeight) ? brown : firebrick, buttonX, buttonY, buttonWidth, buttonHeight);
        app.font.center(app.batch, Option.NEXT.getName(), buttonX + buttonWidth / 2f, buttonY + buttonHeight / 2f, buttonFontSize, Color.WHITE);

        app.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        app.font.scale = Gdx.graphics.getWidth() / 1000f;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int i) {
        switch (i) {
            case 111:
                hovering = null;
                menu = null;
                difficulty = null;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        if (hovering != null) {
            switch (hovering) {
                default:
                    menu = hovering;
                    return false;
                case PLAY:
                    app.setScreen(new GameScreen(app, difficulty));
                    return false;
                case PREVIOUS:
                    difficulty = difficulty.equals(Bot.Difficulty.values()[0]) ? Bot.Difficulty.values()[Bot.Difficulty.values().length - 1] : Bot.Difficulty.values()[Arrays.stream(Bot.Difficulty.values()).collect(Collectors.toList()).indexOf(difficulty) - 1];
                    return false;
                case NEXT:
                    difficulty = difficulty.equals(Bot.Difficulty.values()[Bot.Difficulty.values().length - 1]) ? Bot.Difficulty.values()[0] : Bot.Difficulty.values()[Arrays.stream(Bot.Difficulty.values()).collect(Collectors.toList()).indexOf(difficulty) + 1];
                    return false;
                case QUIT:
                    Gdx.app.exit();
                    return false;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public enum Option {
        BOTS, QUIT,
        PLAY, PREVIOUS, NEXT;

        public String getName() {
            return toString();
        }
    }
}
