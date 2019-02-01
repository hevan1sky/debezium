/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.pipeline.source.spi;

import java.util.Map;

import org.apache.kafka.connect.data.Struct;

import io.debezium.pipeline.spi.OffsetContext;

/**
 * An interface implemented by each connector that enables metrics metadata to be extracted
 * from an event.
 *
 * @author Jiri Pechanec
 *
 */
public interface EventMetadataProvider {

    /**
     * @return source event timestamp in milliseconds
     */
    long getEventTimestamp(Object source, OffsetContext offset, Object key, Struct value);

    /**
     * @return one or more values uniquely position the event in the transaction log - e.g. LSN
     */
    Map<String, String> getEventSourcePosition(Object source, OffsetContext offset, Object key, Struct value);

    /**
     * @return unique identifier of the transaction to which the event belongs
     */
    String getTransactionId(Object source, OffsetContext offset, Object key, Struct value);

    /**
     * @return s String that describes the event
     */
    default String toSummaryString(Object source, OffsetContext offset, Object key, Struct value) {
        return new EventFromatter()
            .sourcePosition(getEventSourcePosition(source, offset, key, value))
            .key(key)
            .value(value)
            .toString();
    }
}