package com.app.nytimes.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nytimes.R;
import com.app.nytimes.data.remote.models.ResultsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    List<ResultsItem> itemList;
    ArticleClickListener articleClickListener;

    public ArticlesAdapter(List<ResultsItem> itemList, ArticleClickListener articleClickListener) {
        this.itemList = itemList;
        this.articleClickListener = articleClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResultsItem resultsItem = itemList.get(position);
        holder.tvTitle.setText(resultsItem.getTitle());
        holder.tvDesc.setText(resultsItem.getByline());
        holder.tvType.setText(resultsItem.getType());
        holder.tvDate.setText(resultsItem.getPublishedDate());

        holder.itemView.setOnClickListener(v -> {
            articleClickListener.onArticleClicked(resultsItem);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvDate)
        TextView tvDate;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
