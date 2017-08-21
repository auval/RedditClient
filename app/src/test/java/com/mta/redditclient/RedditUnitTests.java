package com.mta.redditclient;

import com.mta.model.IModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RedditUnitTests {
//    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }
//
//    @Test
//    public void testMockito() {
//        // mock creation
//        List mockedList = mock(List.class);
//
//// using mock object - it does not throw any "unexpected interaction" exception
//        mockedList.add("one");
//        mockedList.clear();
//
//// selective, explicit, highly readable verification
//        verify(mockedList).add("one");
//        verify(mockedList).clear();
//    }
//

    /**
     * test that when data fetch fails, the user is presented with an error message
     */
    @Test
    public void checkNoData() {
        IModel mockModel = mock(IModel.class);

        mockModel.fetchPostsList("");
    }
}