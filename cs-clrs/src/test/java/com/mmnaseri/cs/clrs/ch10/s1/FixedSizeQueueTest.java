package com.mmnaseri.cs.clrs.ch10.s1;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

/**
 * @author Mohammad Milad Naseri (mmnaseri@programmer.net)
 * @author Ramin Farhanian (rf.tech@icloud.com)
 * @since 1.0 (7/12/15, 9:49 PM)
 */
public class FixedSizeQueueTest {

    @Test
    public void testQueueIsInitiallyEmpty() throws Exception {
        final FixedSizeQueue<Integer> queue = new FixedSizeQueue<>(10);
        assertThat(queue.getSize(), is(0));
        assertThat(queue.isEmpty(), is(true));
    }

    @DataProvider
    public Object[][] queueExpansionDataProvider() {
        return new Object[][]{
                new Object[]{new Integer[]{8, 7, 6, 5, 4}},
                new Object[]{new Integer[]{null, null, null, null}},
                new Object[]{new Integer[]{1, 2, 3, 9, 7, 6}},
                new Object[]{new Integer[]{Integer.MAX_VALUE, 0, 0, 0, 0, 0, 0, 0}},
        };
    }

    @Test(dataProvider = "queueExpansionDataProvider")
    public void testQueueExpandsProperly(Integer[] items) throws Exception {
        final FixedSizeQueue<Integer> queue = new FixedSizeQueue<>(items.length);
        for (int i = 0; i < items.length; i++) {
            Integer item = items[i];
            queue.enqueue(item);
            assertThat(queue.getSize(), is(i + 1));
            assertThat(queue.isEmpty(), is(false));
        }
    }

    @Test(dataProvider = "queueExpansionDataProvider")
    public void testQueueShrinksProperly(Integer[] items) throws Exception {
        final FixedSizeQueue<Integer> queue = new FixedSizeQueue<>(items.length);
        for (Integer item : items) {
            queue.enqueue(item);
        }
        assertThat(queue.getSize(), is(items.length));
        assertThat(queue.isEmpty(), is(false));
        for (int i = 0; i < items.length; i++) {
            assertThat(queue.getSize(), is(items.length - i));
            queue.dequeue();
            assertThat(queue.getSize(), is(items.length - i - 1));
        }
        assertThat(queue.isEmpty(), is(true));
    }

    @Test
    public void testQueueCanRecoverAfterExtremeShrinkage() throws Exception {
        final FixedSizeQueue<Integer> queue = new FixedSizeQueue<>(2);
        queue.enqueue(5);
        queue.enqueue(4);
        queue.dequeue();
        queue.dequeue();
        assertThat(queue.isEmpty(), is(true));
        try {
            queue.dequeue();
            fail("dequeue operation should not be possible when the queue is empty");
        } catch (Exception e) {
            final int value = 1;
            queue.enqueue(value);
            assertThat(queue.dequeue(), is(value));
        }

    }

    @Test(dataProvider = "queueExpansionDataProvider")
    public void testQueuePolicy(Integer[] items) throws Exception {
        final FixedSizeQueue<Integer> queue = new FixedSizeQueue<>(items.length);
        for (Integer item : items) {
            queue.enqueue(item);
        }
        for (Integer item : items) {
            final Integer actual = queue.dequeue();
            assertThat(actual, is(item));
        }
    }

}