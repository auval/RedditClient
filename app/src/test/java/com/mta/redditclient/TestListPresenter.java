package com.mta.redditclient;

import com.mta.model.IModel;
import com.mta.model.pojo.Child;
import com.mta.model.pojo.Data_;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestListPresenter {

    // this is needed to init the @Mock
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    IModel mockModel;

    @Mock
    IListView mockView;

    @Mock
    IListAdapter mockAdapter;

    ListPresenter actualPresenter;

    @Before
    public void init() {

        actualPresenter = new ListPresenter(mockView, mockModel, mockAdapter);
    }

    /**
     * test that when data fetch fails, the user is presented with an error message
     */
    @Test
    public void checkNoData() {

        actualPresenter.onFetchFailure();

        verify(mockView).showErrorMessage(R.string.failed_fetch);

    }

    /**
     * verify that when the data changes:
     * - the view gets notified
     * - the model getPosts is called
     */
    @Test
    public void checkDataChanged() {

        actualPresenter.onDataChanged();

        verify(mockModel).getPosts();
        verify(mockView).invalidateList(mockModel);

    }

    @Test
    public void webViewStartedOnRow() {

        Child c = new Child();
        Data_ d = new Data_();
        d.setUrl("test");
        d.setId("no id");
        c.setData(d);

        when(mockAdapter.getData(0)).thenReturn(c);

        // row click should get the url for the row clicked
        actualPresenter.openUrl(c);

        // and start the web view activity with this url
        verify(mockView).openWebView(c);
    }

}