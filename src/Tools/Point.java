package Tools;

public class Point {
    public float x;
    public float y;
    public float z;

    public void Set(float _x, float _y, float _z) {
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }

    public float GetX() {
        return this.x;
    }

    public float GetY() {
        return this.y;
    }

    public float GetZ() {
        return this.z;
    }
}
