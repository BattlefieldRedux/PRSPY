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

package pt.uturista.prspy.view.settings.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import pt.uturista.prspy.R;


public class SettingsFragment extends PreferenceFragment {
    public static final String TAG = "SettingsFragment";

    // Preferences Notifications
    public static final String NOTIFICATIONS_SERVERS = "pref_notification_servers";
    public static final String NOTIFICATIONS_NEWS = "pref_notifications_official_pr";
    public static final String NOTIFICATIONS_PRT = "pref_notifications_official_prt";
    public static final String NOTIFICATIONS_EVENTS = "pref_notifications_pr_events";

    // Topics for the notifications
    private static final String TOPIC_NEWS = "pr_news";
    private static final String TOPIC_PRT = "tournament_news";
    private static final String TOPIC_EVENTS = "pr_events";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferen);

        Preference pref = findPreference("pref_friends_clan_name");

        if (pref != null) {
            EditTextPreference txtPref = (EditTextPreference) pref;

            txtPref.setSummary(txtPref.getText());
        }

        Preference themePref = findPreference("pref_theme");
        if (themePref != null) {
            ListPreference listPreference = (ListPreference) themePref;

            listPreference.setSummary(listPreference.getValue());
        }

        Preference versionPref = findPreference("pref_version");
        if (versionPref != null) {

            String versionName;
            try {
                versionName = getActivity().getPackageManager().getPackageInfo(
                        getActivity().getPackageName(), 0).versionName;
                versionPref.setSummary(versionName);
            } catch (PackageManager.NameNotFoundException e) {
                // Do nothing
            }

        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Log.d(TAG, "onPreferenceTreeClick");

        switch (preference.getKey()) {
            case NOTIFICATIONS_NEWS:
                handleNotifications(TOPIC_NEWS, ((CheckBoxPreference)preference).isChecked());
                break;
            case NOTIFICATIONS_PRT:
                handleNotifications(TOPIC_PRT, ((CheckBoxPreference)preference).isChecked());
                break;
            case NOTIFICATIONS_EVENTS:
                handleNotifications(TOPIC_EVENTS, ((CheckBoxPreference)preference).isChecked());
                break;
            default:
                Log.d(TAG, "default");
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


    private void handleNotifications(@NonNull String topic, boolean subscribe) {
        Log.d(TAG, "handleNotifications: " + topic + " - " + subscribe);

        if (subscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        }
    }
}