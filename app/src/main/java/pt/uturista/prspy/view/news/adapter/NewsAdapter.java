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

package pt.uturista.prspy.view.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.Holder> {
    private static DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    private News[] mNews = new News[0];
    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public NewsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);

        return new Holder(v, mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.Holder holder, int position) {


        switch (mNews[position].getType()) {
            case News.EVENT:
                holder.Type.setBackgroundColor(Color.parseColor("#aceca1"));
                break;

            case News.TOURNAMENT:
                holder.Type.setBackgroundColor(Color.parseColor("#f46036"));
                break;

            case News.BLOG:
                holder.Type.setBackgroundColor(Color.parseColor("#3399ff"));
                break;

            case News.HIGHTLIGHTS:
                holder.Type.setBackgroundColor(Color.parseColor("#3399ff"));
                break;

            default:
            case News.OFFICIAL:
                holder.Type.setBackgroundColor(Color.parseColor("#3399ff"));
                break;
        }

        holder.Title.setText(mNews[position].getTitle());

        Context context = holder.Date.getContext();
        holder.Content.setText(mNews[position].getLocalizedType(context));

        String metaInformation = holder.Date.getContext().getString(R.string.news_row_meta,
                format.format(mNews[position].getPublishedDate()),
                mNews[position].getAuthor());

        holder.Date.setText(metaInformation);

        holder.setNews(mNews[position]);
    }

    @Override
    public int getItemCount() {
        return mNews.length;
    }


    public void setCollection(News[] news) {
        mNews = news;
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Listener mListener;
        final TextView Title;
        final TextView Content;
        final View Type;
        final TextView Date;
        News News;


        Holder(View itemView, Listener listener) {
            super(itemView);
            Date = itemView.findViewById(R.id.date);
            Title = itemView.findViewById(R.id.title);
            Content = itemView.findViewById(R.id.content);
            Type = itemView.findViewById(R.id.type);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        public void setNews(News news){
            News = news;
        }



        @Override
        public void onClick(View view) {
            if (mListener != null && News != null) {
                mListener.onNewsClicked(News);
            }
        }
    }

    public interface Listener {
        void onNewsClicked(News news);
    }
}
