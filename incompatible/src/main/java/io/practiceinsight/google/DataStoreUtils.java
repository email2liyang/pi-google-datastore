/*
 * Copyright (c) 2015 Practice Insight Pty Ltd.
 */
package io.practiceinsight.google;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.api.services.datastore.DatastoreV1;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.PathElement;
import com.google.protobuf.TextFormat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ivan.li 5/24/16 1:01 PM
 */
public class DataStoreUtils {

  public static Key fromV1Beta2UrlSafe(String urlSafe) {
    DatastoreV1.Key old;
    try {
      String utf8Str = URLDecoder.decode(urlSafe, UTF_8.name());
      DatastoreV1.Key.Builder builder = DatastoreV1.Key.newBuilder();
      TextFormat.merge(utf8Str, builder);
      old = builder.build();
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Unexpected decoding exception", e);
    } catch (TextFormat.ParseException e) {
      throw new IllegalArgumentException("Could not parse key", e);
    }
    List<DatastoreV1.Key.PathElement> path = old.getPathElementList();
    DatastoreV1.Key.PathElement leaf = path.get(path.size() - 1);
    Key.Builder newKey;
    if (leaf.hasName()) {
      newKey = Key.builder(old.getPartitionId().getDatasetId(), leaf.getKind(), leaf.getName());
    } else {
      newKey = Key.builder(old.getPartitionId().getDatasetId(), leaf.getKind(), leaf.getId());
    }
    newKey.namespace(old.getPartitionId().getNamespace());
    List<DatastoreV1.Key.PathElement> oldPath = old.getPathElementList();
    List<PathElement> ancestors = new ArrayList<PathElement>(oldPath.size() - 1);
    for (DatastoreV1.Key.PathElement pathElement : oldPath) {
      if (pathElement.hasName()) {
        ancestors.add(PathElement.of(pathElement.getKind(), pathElement.getName()));
      } else {
        ancestors.add(PathElement.of(pathElement.getKind(), pathElement.getId()));
      }
    }
    newKey.ancestors(ancestors);
    return newKey.build();
  }
}
