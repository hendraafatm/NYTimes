package com.app.nytimes.ui.home;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

import static com.app.nytimes.utils.AppConstants.VOICE_RECOGNITION_REQUEST_CODE;

public class MainActivity extends BaseActivity implements View.OnClickListener, ArticleClickListener {

    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.ivRecordVoice)
    ImageView ivRecordVoice;
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
        initVoice();

        ivRecordVoice.setOnClickListener(this);
    }

    private void initVoice() {
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            ivRecordVoice.setOnClickListener(this);
        } else {
            ivRecordVoice.setEnabled(false);
        }
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
            itemsList.clear();
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
        ArticleDetailsActivity.launch(this, resultsItem);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == ivRecordVoice.getId()) {
            startVoiceRecognitionActivity();
        }
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech Recognition");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            // matches is the result of voice input. It is a list of what the
            // user possibly said.
            // Using an if statement for the keyword you want to use allows the
            // use of any activity if keywords match
            // it is possible to set up multiple keywords to use the same
            // activity so more than one word will allow the user
            // to use the activity (makes it so the user doesn't have to
            // memorize words from a list)
            // to use an activity from the voice input information simply use
            // the following format;
            // if (matches.contains("keyword here") { startActivity(new
            // Intent("name.of.manifest.ACTIVITY")

            if (matches != null && matches.contains("load data")) {
                mainViewModel.getNYPopularArticles();
            }
        }
    }
}
