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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import pt.uturista.prspy.R;
import pt.uturista.prspy.loader.NewsLoader;
import pt.uturista.prspy.model.News;
import pt.uturista.prspy.view.BaseNavigationActivity;
import pt.uturista.prspy.view.news.adapter.NewsAdapter;

public class NewsActivity extends BaseNavigationActivity implements LoaderManager.LoaderCallbacks<News[]>, NewsAdapter.Listener {

    private RecyclerView mListView;
    private LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;

    protected NewsActivity() {
        super(true, R.layout.news_activity, R.id.root);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);

        // Set CustomAdapter as the adapter for RecyclerView.
        mAdapter = new NewsAdapter();
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(564631, null, this);

    }

    @Override
    protected int getDrawerId() {
        return R.id.nav_news;
    }

    @NonNull
    @Override
    public Loader<News[]> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<News[]> loader, News[] data) {
        mAdapter.setCollection(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<News[]> loader) {

    }

    @Override
    public void onNewsClicked(News news) {
        if (news != null) {
            startActivity(NewsDetailsActivity.newIntent(this, news.getLocalizedType(this), news.getContent()));
        }
    }
}
