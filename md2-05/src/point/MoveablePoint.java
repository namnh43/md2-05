package point;

public class MoveablePoint extends Point{
    private float xSpeed = 0x0f;
    private float ySpeed = 0x0f;

    public MoveablePoint(float x, float y, float xSpeed, float ySpeed) {
        super(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public MoveablePoint(float xSpeed, float ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public MoveablePoint() {
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }
    public void setSpeed(float xSpeed, float ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    public float[] getSpeed() {
        return new float[] {this.xSpeed, this.ySpeed};
    }

    @Override
    public String toString() {
        return super.toString() + ",speed=(" +
                this.xSpeed + "," + this.ySpeed + ")";
    }
    public MoveablePoint move() {
        setX(getX() + this.getxSpeed());
        setY(getY() + this.getySpeed());
        return this;
    }
}
