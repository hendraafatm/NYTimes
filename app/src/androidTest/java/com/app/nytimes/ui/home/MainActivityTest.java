package com.app.nytimes.ui.home;

import android.view.View;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.app.nytimes.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    private  MainActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.getActivity();
    }
@Test
public void testLaunchActivity(){
        View view=mActivity.findViewById(R.id.rvContent);
        assertNotNull(view);

}
    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }

}