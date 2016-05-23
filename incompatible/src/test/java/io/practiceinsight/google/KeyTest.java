/*
 * Copyright (c) 2015 Practice Insight Pty Ltd.
 */
package io.practiceinsight.google;

import com.google.cloud.datastore.Key;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author ivan.li 5/23/16 4:02 PM
 */
public class KeyTest {

  @Test
  public void testFromSafeUrl() throws Exception {
    Key
        googleKey =
        Key.fromUrlSafe(
            "partition_id+%7B%0A++dataset_id%3A+%22datastore-test-1123%22%0A%7D%0Apath_element+%7B%0A++kind%3A+%22ip_pending_msg%22%0A++id%3A+5629499534213120%0A%7D%0A");
    assertEquals(googleKey.id(), new Long("5629499534213120"));
    assertEquals(googleKey.kind(), "ip_pending_msg");

  }
}
