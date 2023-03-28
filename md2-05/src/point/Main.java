package point;

public class Main {
    public static void main(String[] args) {
        MoveablePoint point1 = new MoveablePoint(100, 50, 20, 30);
        MoveablePoint point2 = new MoveablePoint(0, 0, 10, 10);
        System.out.println(point1.toString());
        System.out.println(point2.toString());
        point1.move();
        point1.move();
        System.out.println(point1.toString());

    }
}
