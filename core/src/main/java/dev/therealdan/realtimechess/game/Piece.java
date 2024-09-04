package dev.therealdan.realtimechess.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dev.therealdan.realtimechess.main.RealTimeChessApp;

import java.util.HashMap;

public class Piece {

    private static HashMap<Type, Texture> textures = new HashMap<>();

    private Type type;
    private Colour colour;
    private Position position;

    public Piece(Type type, Colour colour, Position position) {
        this.type = type;
        this.colour = colour;
        this.position = position;
    }

    public void render(RealTimeChessApp app, float x, float y, float cell) {
        float spacing = cell * 0.1f;
        float width = cell - spacing * 2f, height = width;

        app.batch.setColor(getColor().getColor());
        app.batch.draw(getType().getTexture(), x - cell + spacing, y + spacing, width, height);
    }

    public Type getType() {
        return type;
    }

    public Colour getColor() {
        return colour;
    }

    public Position getPosition() {
        return position;
    }

    public enum Type {
        PAWN, ROOK, KNIGHT, BISHOP, KING, QUEEN;

        public String getNotation() {
            return toString().substring(0, 1);
        }

        public Texture getTexture() {
            if (!textures.containsKey(this))
                textures.put(this, new Texture("images/pieces/" + toString().toLowerCase() + ".png"));
            return textures.get(this);
        }
    }

    public enum Colour {
        BLACK, WHITE;

        public Colour opposite() {
            return equals(BLACK) ? WHITE : BLACK;
        }

        public Color getColor() {
            return equals(BLACK) ? Color.BLACK : Color.WHITE;
        }
    }
}
