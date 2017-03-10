/**
 * Created by Serozh on 2/14/2017.
 */
public class NoClassDefFoundErrorDemo {
    public static void main(String[] args) {
        try {
            long[][] ary = new long[Integer.MAX_VALUE][Integer.MAX_VALUE];
            // The following line would throw ExceptionInInitializerError
            SimpleCalculator calculator1 = new SimpleCalculator();
            System.out.println("1");
        } catch (Throwable t) {
            System.out.println(t);
        }
        System.out.println("2");
        // The following line would cause NoClassDefFoundError
        SimpleCalculator calculator2 = new SimpleCalculator();
        System.out.println("3");
    }

}




