package bbfs;

import pu.RedLib.Reduction;

public class Reducer<C extends CostNamePair> implements Reduction<C> {
    @Override
    public C reduce(C e1, C e2) {
        if (e1.getVertex() == null) {
            return e2;
        }
        if (e2.getVertex() == null) {
            return e1;
        }

        if (e1.getCost() < e2.getCost()) {
            return e1;
        } else {
            return e2;
        }

    }
}
