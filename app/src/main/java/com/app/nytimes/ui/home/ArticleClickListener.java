package com.app.nytimes.ui.home;

import com.app.nytimes.data.remote.models.ResultsItem;

public interface ArticleClickListener {

    void onArticleClicked(ResultsItem resultsItem);
}
