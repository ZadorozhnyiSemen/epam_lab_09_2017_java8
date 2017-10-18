package part1.exercise;

public class B extends A {
    @Override
    public void setVal() {
        val = "B";
    }

    public static void main(String[] args) {
        System.out.println(new B().val);
    }
}

class A {
    protected String val;

    A() {
        setVal();
    }

    public void setVal() {
        val = "A";
    }
}

