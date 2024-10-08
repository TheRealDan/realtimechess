package dev.therealdan.realtimechess.game;

public class Notation {

    private String notation;
    private Piece.Type type;
    private Piece.Colour colour;
    private Position from, to;

    public Notation(Piece.Colour colour) {
        this.notation = colour.getNotation();

        this.colour = colour;
    }

    public Notation(Piece piece, Position position) {
        this.notation = piece.getType().getNotation() + piece.getPosition().getNotation() + position.getNotation();

        this.type = piece.getType();
        this.from = piece.getPosition().copy();
        this.to = position.copy();
    }

    public Notation(Piece pawn, Piece.Type promoteTo) {
        this.notation = pawn.getPosition().getNotation() + promoteTo.getNotation();

        this.type = promoteTo;
        this.from = pawn.getPosition().copy();
        this.to = pawn.getPosition().copy();
    }

    public Notation(Board board) {
        StringBuilder builder = new StringBuilder();
        for (Piece piece : board.getPieces()) {
            builder.append(",");
            builder.append(piece.getColour().getNotation());
            builder.append(piece.getType().getNotation());
            builder.append(piece.getPosition().getNotation());
        }
        this.notation = builder.toString().replaceFirst(",", "");
    }

    public Notation(String notation) {
        this.notation = notation;

        if (isAssignment()) {
            this.colour = Piece.Colour.byNotation(notation);
        } else if (isPromotion()) {
            this.type = Piece.Type.byNotation(notation.substring(2, 3));
            this.from = Position.byNotation(notation.substring(0, 2));
            this.to = Position.byNotation(notation.substring(0, 2));
        } else if (isMove()) {
            this.type = Piece.Type.byNotation(notation.substring(0, 1));
            this.from = Position.byNotation(notation.substring(1, 3));
            this.to = Position.byNotation(notation.substring(3, 5));
        }
    }

    public boolean isBoard() {
        return type == null;
    }

    public boolean isPromotion() {
        return getNotation().length() == 3;
    }

    public boolean isMove() {
        return getNotation().length() == 5;
    }

    public boolean isAssignment() {
        return getNotation().startsWith("B") || getNotation().startsWith("W");
    }

    public String getNotation() {
        return notation;
    }

    public Piece.Type getType() {
        return type;
    }

    public Piece.Colour getColour() {
        return colour;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }
}
