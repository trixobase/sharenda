package cm.trixobase.frontend.domain.adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.frontend.R;
import cm.trixobase.library.common.domain.UiArticle;
import cm.trixobase.library.common.manager.Response;
import cm.trixobase.library.common.widget.DialogBox;
import cm.trixobase.library.common.widget.Toast;
import cm.trixobase.library.service.Service;

/*
 * Powered by Trixobase Enterprise on 30/11/20.
 */

public class MessageAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView tv_title, tv_price, tv_price_old, tv_description, tv_advise;
        ImageView iv_picture;
        Button bt_add;
    }

    private final Service service;
    private final List<ContentValues> articles;

    public MessageAdapter(Service service) {
        this.service = service;
        this.articles = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public ContentValues getItem(int i) {
        return articles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return articles.indexOf(articles.get(i));
    }

    @SuppressLint({"InflateParams", "ResourceType"})
    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) service.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (itemView == null) {
            holder = new ViewHolder();
            itemView = inflater.inflate(R.layout.listview_article_item, null);

            holder.iv_picture = itemView.getRootView().findViewById(R.id.iv_file_url_id);
            holder.tv_title = itemView.getRootView().findViewById(R.id.tv_title_id);
            holder.tv_price = itemView.getRootView().findViewById(R.id.tv_price_id);
            holder.tv_price_old = itemView.getRootView().findViewById(R.id.tv_price_old_id);
            holder.tv_description = itemView.getRootView().findViewById(R.id.tv_description_id);
            holder.tv_advise = itemView.getRootView().findViewById(R.id.tv_advise_id);
            holder.bt_add = itemView.getRootView().findViewById(R.id.bt_add_id);

            itemView.setTag(holder);
        } else holder = (ViewHolder) itemView.getTag();

        final UiArticle article = UiArticle.builder(articles.get(position)).build();

        service.setPicture(holder.iv_picture, article.getPicture());
        holder.tv_title.setText(article.getName());
        holder.tv_price.setText(article.getDisplayPrice());
        holder.tv_price_old.setText(article.getDisplayOldPrice());
        holder.tv_price_old.getPaint().setStrikeThruText(true);
        holder.tv_description.setText(article.getDescription());
        holder.tv_advise.setText(article.getAdvise(service.getContext()));

        View.OnClickListener choiceListener = view -> {
            service.setData(article.getData());
            DialogBox.builder(service.getContext()).setActions(new Response.onFrontResult() {
                @Override
                public void onTrue(Response response) {
                    showToast(response.getMessage());
                }
                @Override
                public void onFalse(Response response) {
                }
                @Override
                public void onMessage(String message) {
                    showToast(message);
                }
            }).build().show();
        };

        holder.iv_picture.setOnClickListener(choiceListener);
        holder.bt_add.setOnClickListener(choiceListener);

        return itemView;
    }

    public void add(ContentValues data) {
        articles.add(data);
        notifyDataSetChanged();
    }

    public void clear() {
        articles.clear();
        notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.builder(service.getContext()).setMessage(message).showLong();
    }


}
