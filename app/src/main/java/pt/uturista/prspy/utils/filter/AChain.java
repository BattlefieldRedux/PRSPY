package pt.uturista.prspy.utils.filter;

import java.util.ArrayList;
import java.util.List;

public class AChain<T> implements Filter<T> {
    private List<Filter<T>> mChain = new ArrayList<>();

    @Override
    public boolean filter(T object) {

        Filter<T> filter;
        for (int i = 0, length = mChain.size(); i < length; i++) {
            filter = mChain.get(i);
            if (!filter.filter(object)) {
                return false;
            }

        }
        return true;
    }

    public AChain<T> and(Filter<T> filter) {
        if (filter != null)
            mChain.add(filter);
        return this;
    }
}
