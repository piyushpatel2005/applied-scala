package koans;

import koans.MathNum;
import koans.TryOption;
import scala.None;
import scala.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dick
 * Date: 1/16/11
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ObtuseJavaLib {

    public String getComplement(String word) {
        if (word.equals("Salt")) return "Pepper";
        if (word.equals("Fish")) return "Chips";
        if (word.equals("Horse")) return "Cart";
        return null;
    }

    public boolean isNull(Object ref) {
        if (ref == null) return true;
        return false;
    }

    public List<Integer> oneToTen() {
        ArrayList<Integer> oneToTen = new ArrayList<Integer>(10);
        for (int i = 1; i <= 10; i++) oneToTen.add(i);
        return oneToTen;
    }

    public interface MathFunc {
        public int apply(int in);
    }

    public List<Integer> doMathFuncOnList(List<Integer> list, MathFunc func) {
        ArrayList<Integer> newList = new ArrayList<Integer>(list.size());
        for(int i: list) {
            newList.add(func.apply(i));
        }
        return newList;
    }

    public boolean isNotDefined(String it) {
        Option<String> something;
        if (it.isEmpty()) something =  Option.empty();
        else something = Option.apply(it);

        TryOption tryOpt = new TryOption();
        return tryOpt.notDefined(something);
    }

    public static class JavaMathObject implements MathNum {
        private final int number;

        public JavaMathObject(int number) { this.number = number; }

        @Override
        public int add(int b) {
            return number + b;
        }

        @Override
        public int sub(int a) {
            return number - a;
        }

        @Override
        public int mul(int a) {
            return number * a;
        }

        @Override
        public int div(int a) {
            return number / a;
        }
    }

    public JavaMathObject makeMathObj(int num) { return new JavaMathObject(num); }

}
