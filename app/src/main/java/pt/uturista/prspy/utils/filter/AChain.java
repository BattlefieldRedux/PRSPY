/*
 * Copyright (c) 2018 uturista.pt
 *
 * Licensed under the Attribution-NonCommercial 4.0 International (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
