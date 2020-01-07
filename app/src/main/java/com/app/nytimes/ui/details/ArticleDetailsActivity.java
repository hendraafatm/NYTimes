package com.app.nytimes.ui.details;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nytimes.R;
import com.app.nytimes.data.remote.models.ResultsItem;
import com.app.nytimes.helper.BaseActivity;
import com.app.nytimes.utils.AppConstants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailsActivity extends BaseActivity {

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etAbstract)
    EditText etAbstract;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvByline)
    TextView tvByline;
    @BindView(R.id.tvSection)
    TextView tvSection;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvCaption)
    TextView tvCaption;

    ResultsItem resultsItem = null;

    public static void launch(Context context, ResultsItem resultsItem) {
        Intent intent = new Intent(context, ArticleDetailsActivity.class);
        intent.putExtra(AppConstants.ARTICLE_ITEM, resultsItem);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);

        resultsItem = (ResultsItem) getIntent().getSerializableExtra(AppConstants.ARTICLE_ITEM);
        if (resultsItem != null) {
            updateUI(resultsItem);
        }
    }

    private void updateUI(ResultsItem resultsItem) {
        etTitle.setText(resultsItem.getTitle());
        etAbstract.setText(resultsItem.get_abstract());
        tvType.setText(resultsItem.getType());
        tvByline.setText(resultsItem.getByline());
        tvSection.setText(resultsItem.getSection());
        tvDate.setText(resultsItem.getPublishedDate());
        tvCaption.setText(resultsItem.getMedia().get(0).getCaption());

        if (!resultsItem.getMedia().isEmpty()) {
            Picasso.get().load(resultsItem.getMedia().get(0).getMediaMetadata().get(0).getUrl()).into(ivImage);
        }
    }
}
