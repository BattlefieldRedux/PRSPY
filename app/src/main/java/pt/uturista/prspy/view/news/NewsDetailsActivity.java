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

package pt.uturista.prspy.view.news;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;

import pt.uturista.prspy.R;
import pt.uturista.prspy.view.BaseActivity;

public class NewsDetailsActivity extends BaseActivity {
    private static final String TAG = "NewsDetailsActivity";
    private static final String ARGS_CONTENT = "ARGS_CONTENT";
    private static final String ARGS_TYPE = "ARGS_TYPE";
    private WebView webview;


    protected NewsDetailsActivity() {
        super(true, R.layout.news_details_activity, R.id.root);
    }

    public static Intent newIntent(Context context, String type, String content) {
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(ARGS_TYPE, type);
        intent.putExtra(ARGS_CONTENT, content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra(ARGS_TYPE));
        }

        webview = findViewById(R.id.webview);

        // To prevent the (default) white background of the webview
        // while we load the css, we set it to a transparent color
        webview.setBackgroundColor(0x00000000);

        // REMOVE THIS BEFORE PRODUCTION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Load the webview
        String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + getIntent().getStringExtra(ARGS_CONTENT);
        webview.getSettings().setJavaScriptEnabled(false);
        webview.loadDataWithBaseURL("file:///android_res/raw/", htmlData, "text/html", "UTF-8", null);
    }


}
