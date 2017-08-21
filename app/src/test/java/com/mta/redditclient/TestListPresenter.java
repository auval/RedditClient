package com.mta.redditclient;

import com.mta.model.IModel;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

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

    /**
     * test that when data fetch fails, the user is presented with an error message
     */
    @Test
    public void checkNoData() {

        ListPresenter actualPresenter = new ListPresenter(mockView, mockModel, mockAdapter);

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

        ListPresenter actualPresenter = new ListPresenter(mockView, mockModel, mockAdapter);

        actualPresenter.onDataChanged();

        verify(mockModel).getPosts();
        verify(mockView).invalidateList(mockModel);

    }

}