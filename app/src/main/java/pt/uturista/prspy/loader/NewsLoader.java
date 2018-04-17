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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import pt.uturista.prspy.model.News;

public class NewsLoader extends AsyncTaskLoader<News[]> {
    private static final String TAG = "NewsLoader";

    private static final URL NEWS_FEED;
    private static final URL TOURNAMENT_FEED;
    private static final URL DEV_BLOG_FEED;
    private static final URL HIGHLIGHTS_FEED;
    private static final URL EVENTS_FEED;
    private static final URL CHANGELOG_FEED;
    private static News[] mData;

    public NewsLoader(@NonNull Context context) {
        super(context);

    }

    @Nullable
    @Override
    public News[] loadInBackground() {
        List<GetNews> taskNews = new ArrayList<>();

        taskNews.add(new GetNews(NEWS_FEED, News.OFFICIAL));
        taskNews.add(new GetNews(TOURNAMENT_FEED, News.TOURNAMENT));
        taskNews.add(new GetNews(DEV_BLOG_FEED, News.BLOG));
        taskNews.add(new GetNews(HIGHLIGHTS_FEED, News.HIGHLIGHTS));
        taskNews.add(new GetNews(EVENTS_FEED, News.EVENT));
        taskNews.add(new GetNews(CHANGELOG_FEED, News.CHANGELOG));


        try {
            ExecutorService service = Executors.newFixedThreadPool(5);


            List<Future<List<News>>> futureNews = service.invokeAll(taskNews);
            service.shutdown();
            service.awaitTermination(1L, TimeUnit.MINUTES);

            List<News> news = new ArrayList<>();
            for (Future<List<News>> futureNew : futureNews) {
                if(futureNew.isDone()){
                    news.addAll(futureNew.get());
                }
            }

            Collections.sort(news, new Comparator<News>() {
                @Override
                public int compare(News o1, News o2) {
                    return o2.getPublishedDate().compareTo(o1.getPublishedDate());
                }
            });


            mData = new News[news.size()];
            mData = news.toArray(mData);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return mData;
    }


    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");

        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }


    static {
        try {
            NEWS_FEED = new URL("https://www.realitymod.com/forum/external.php?forumids=380");
            TOURNAMENT_FEED = new URL("https://www.realitymod.com/forum/external.php?forumids=110");
            DEV_BLOG_FEED = new URL("https://www.realitymod.com/forum/external.php?forumids=389");
            HIGHLIGHTS_FEED = new URL("https://www.realitymod.com/forum/external.php?forumids=196");
            EVENTS_FEED = new URL("https://www.realitymod.com/forum/external.php?forumids=376");
            CHANGELOG_FEED = new URL("https://www.realitymod.com/forum/external.php?forumids=604");
        } catch (MalformedURLException e) {
            throw new AssertionError("Should really not happen");
        }
    }


    class GetNews implements Callable<List<News>> {


        private final URL url;
        private final int type;

        GetNews(URL url, int type) {
            this.url = url;
            this.type = type;
        }

        @Override
        public List<News> call() throws Exception {
            List<News> news = new ArrayList<>();

            SyndFeed newsFeed = download(url);
            if (newsFeed != null) {
                for (SyndEntry syndEntry : newsFeed.getEntries()) {

                    if (syndEntry.getContents().size() == 0 || syndEntry.getTitle() == null || syndEntry.getTitle().isEmpty())
                        continue;

                    news.add(new News(syndEntry, type));
                }
            }
            return news;
        }

        private SyndFeed download(URL url) {
            HttpURLConnection connection = null;
            try {
                // Prepare the connection
                connection = (HttpURLConnection) url.openConnection();
                connection.setUseCaches(false);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream stream = connection.getInputStream();
                    SyndFeedInput input = new SyndFeedInput();
                    return input.build(new XmlReader(stream));
                }

            } catch (IOException | FeedException e) {
                e.printStackTrace();

            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }
}
