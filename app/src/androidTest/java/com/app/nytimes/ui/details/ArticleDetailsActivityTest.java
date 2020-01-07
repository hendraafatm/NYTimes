package com.app.nytimes.ui.details;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.app.nytimes.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArticleDetailsActivityTest {

    @Rule
    public ActivityTestRule<ArticleDetailsActivity> mActivityTestRule =
            new ActivityTestRule<ArticleDetailsActivity>(ArticleDetailsActivity.class);
    private  ArticleDetailsActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.getActivity();
    }
    @Test
    public void testLaunchActivity(){
        View view=mActivity.findViewById(R.id.tilDesc);
        assertNotNull(view);

    }
    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }

}