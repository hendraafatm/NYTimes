package com.app.nytimes.ui.home;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nytimes.R;
import com.app.nytimes.data.remote.models.ResultsItem;
import com.app.nytimes.data.remote.response.NYArticlesResponse;
import com.app.nytimes.helper.BaseActivity;
import com.app.nytimes.ui.details.ArticleDetailsActivity;
import com.app.nytimes.utils.CommonUtils;
import com.app.nytimes.utils.LoadingProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements ArticleClickListener {

    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;

    LoadingProgressBar loadingProgressBar;
    MainViewModel mainViewModel;

    List<ResultsItem> itemsList;
    ArticlesAdapter articlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRV();
        initViewModel();
    }

    private void initRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvContent.setLayoutManager(linearLayoutManager);

        itemsList = new ArrayList<>();
        articlesAdapter = new ArticlesAdapter(itemsList, this);
        rvContent.setAdapter(articlesAdapter);
    }

    private void initViewModel() {
        loadingProgressBar = new LoadingProgressBar();

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getLoadingRequest().observe(this, this::onLoadingStatusChanged);
        mainViewModel.getErrorMessage().observe(this, this::onErrorReceived);
        mainViewModel.getArticlesResponseMutableLiveData().observe(this, this::onArticlesReceived);

        mainViewModel.getNYPopularArticles();
    }

    private void onArticlesReceived(NYArticlesResponse nyArticlesResponse) {
        if (!nyArticlesResponse.getResults().isEmpty()) {
            itemsList.addAll(nyArticlesResponse.getResults());
            articlesAdapter.notifyDataSetChanged();
        } else {
            llNoData.setVisibility(View.VISIBLE);
        }
    }


    private void onErrorReceived(String message) {
        CommonUtils.showSnackBar(parent, message);
    }

    private void onLoadingStatusChanged(Boolean show) {
        if (show) {
            loadingProgressBar.show(getSupportFragmentManager(), "show");
        } else {
            loadingProgressBar.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onArticleClicked(ResultsItem resultsItem) {
        ArticleDetailsActivity.launch(this,resultsItem);
    }
}
