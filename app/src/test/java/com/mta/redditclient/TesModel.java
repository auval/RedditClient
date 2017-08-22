package com.mta.redditclient;

import com.mta.model.fav.ChildFavTypeConverter;
import com.mta.model.fav.Favorite;
import com.mta.model.pojo.Child;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TesModel {

    // this is needed to init the @Mock
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    IListView mockView;
    @Mock
    IListPresenter mockPresenter;

    @Mock
    IListAdapter mockAdapter;

    /**
     * this test can have multiple points of failure: type conversions and id generation.
     * todo: split to lower level tests
     */
    @Test
    public void testIdsConvertingCorrectly() {
        Favorite f = new Favorite();
        f.setId("123_456");
        f.setThumbnailImgUrl("asd");
        f.setTitle("poi");
        f.setUrl("789");

        Child child = ChildFavTypeConverter.toChild(f);

        assertEquals(ChildFavTypeConverter.getId(child),
                ChildFavTypeConverter.getId(f));

    }

}