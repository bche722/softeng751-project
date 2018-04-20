

package bbfs;


public class Reducer<C extends bbfs.CostNamePair> implements pu.RedLib.Reduction<C> {
    @java.lang.Override
    public C reduce(C e1, C e2) {
        if ((e1.getVertex()) == null) {
            return e2;
        }
        if ((e2.getVertex()) == null) {
            return e1;
        }
        if ((e1.getCost()) < (e2.getCost())) {
            return e1;
        }else {
            return e2;
        }
    }
}

