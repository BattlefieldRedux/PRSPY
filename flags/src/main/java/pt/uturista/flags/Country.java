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

package pt.uturista.flags;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class Country {
    public final String CODE;
    public final String CODE_2;
    public final String CODE_3;
    public final String DOMAIN;
    @StringRes
    public final int NAME;
    @DrawableRes
    public final int FLAG;
    public final int CONTINENT;

    public Country(String code, String code2, String code3, String domain, @StringRes int name, @DrawableRes int flag, int continent) {
        this.CODE = code;
        this.CODE_2 = code2;
        this.CODE_3 = code3;
        this.NAME = name;
        this.DOMAIN = domain;
        this.FLAG = flag;
        this.CONTINENT = continent;
    }
}
