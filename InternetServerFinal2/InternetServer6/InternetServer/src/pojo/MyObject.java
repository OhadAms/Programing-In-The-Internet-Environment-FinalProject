package pojo;

public class MyObject {
    String name;
    int amount;

    public MyObject(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public static void main(String[] args) {
        MyObject myObject = new MyObject("Alice",3);
    }
}
