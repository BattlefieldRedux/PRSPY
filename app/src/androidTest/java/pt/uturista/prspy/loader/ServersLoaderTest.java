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

package pt.uturista.prspy.loader;

import android.os.Parcel;
import android.test.AndroidTestCase;

import pt.uturista.prspy.model.ServerData;

public class ServersLoaderTest extends AndroidTestCase {

    public ServersLoaderTest(){
        super();
    }

    public void testOne(){
        ServersLoader loader = new ServersLoader(getContext());
        ServerData expected = loader.loadInBackground();

        Parcel parcel = Parcel.obtain();
        expected.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for reading:
        parcel.setDataPosition(0);

        // Reconstruct object from parcel and asserts:
        ServerData createdFromParcel = ServerData.CREATOR.createFromParcel(parcel);


        // Since I don't want to override Equals/HashCode in ServerData
        // I make the comparision rules manually
        assertEquals(expected.getTime(), createdFromParcel.getTime());

        assertEquals(expected.getServers().length, createdFromParcel.getServers().length);

        for (int i = 0; i < expected.getServers().length; i++) {
            assertEquals(expected.getServers()[i], createdFromParcel.getServers()[i]);
        }
    }
}