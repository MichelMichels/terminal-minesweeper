import java.util.Objects;

public class Mine {
    private final int x, y;

    public Mine(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mine mine = (Mine) o;
        return x == mine.x && y == mine.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
