/* Copyright 2021 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
=======================================================================*/
package org.tensorflow.ndarray.impl.sparse.slice;

import org.tensorflow.ndarray.LongNdArray;
import org.tensorflow.ndarray.NdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.buffer.DataBuffer;
import org.tensorflow.ndarray.buffer.DataBuffers;
import org.tensorflow.ndarray.buffer.LongDataBuffer;
import org.tensorflow.ndarray.impl.dimension.DimensionalSpace;
import org.tensorflow.ndarray.impl.dimension.RelativeDimensionalSpace;
import org.tensorflow.ndarray.impl.sparse.AbstractSparseNdArray;
import org.tensorflow.ndarray.index.Index;

import java.nio.ReadOnlyBufferException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class LongSparseSlice extends SparseSlice<Long, LongNdArray> implements LongNdArray {

  /**
   * Creates a LongSparseSlice
   *
   * @param source the source Sparse Array that this object slices.
   * @param sourcePosition the relative source position into the source
   * @param dimensions the dimensional space for the window
   */
  public LongSparseSlice(
      AbstractSparseNdArray<Long, LongNdArray> source,
      long sourcePosition,
      DimensionalSpace dimensions) {
    super(source, sourcePosition, dimensions);
  }

  /** {@inheritDoc} */
  @Override
  public LongNdArray toDense() {
    LongDataBuffer dataBuffer = DataBuffers.ofLongs(shape().size());
    read(dataBuffer);
    return NdArrays.wrap(shape(), dataBuffer);
  }

  @Override
  public long getLong(long... coordinates) {
    return getObject(coordinates);
  }

  @Override
  public LongNdArray setLong(long value, long... coordinates) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public LongNdArray setObject(Long value, long... coordinates) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public LongNdArray set(NdArray<Long> src, long... coordinates) {
    throw new ReadOnlyBufferException();
  }

  /** {@inheritDoc} */
  @Override
  public LongNdArray read(DataBuffer<Long> dst) {
    // set the values in buf to the default, then overwrite with indices/values
    Long[] defaults = new Long[(int) shape().size()];
    Arrays.fill(defaults, getDefaultValue());
    dst.write(defaults);

    AtomicLong i = new AtomicLong();
    getIndices()
        .elements(0)
        .forEachIndexed(
            (idx, l) -> {
              long[] coordinates = getIndicesCoordinates(l);
              long value = getValues().getLong(i.getAndIncrement());
              dst.setObject(value, dimensions.positionOf(coordinates));
            });
    return this;
  }

  @Override
  public LongNdArray read(LongDataBuffer dst) {
    return read((DataBuffer<Long>) dst);
  }

  @Override
  public LongNdArray write(DataBuffer<Long> src) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public LongNdArray write(LongDataBuffer src) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public LongNdArray slice(Index... indices) {
    if (indices == null) {
      throw new IllegalArgumentException("Slicing requires at least one index");
    }
    RelativeDimensionalSpace sliceDimensions = dimensions().mapTo(indices);
    return slice(sliceDimensions.position(), sliceDimensions);
  }

  /** {@inheritDoc} */
  @Override
  public LongNdArray slice(long position, DimensionalSpace sliceDimensions) {
    return new LongSparseSlice(this.source, position + sourcePosition, sliceDimensions);
  }

  @Override
  public LongNdArray get(long... coordinates) {
    return (LongNdArray) super.get(coordinates);
  }

  @Override
  public LongNdArray copyTo(NdArray<Long> dst) {
    return (LongNdArray) super.copyTo(dst);
  }

  @Override
  public LongNdArray createDefaultArray() {
    return source.getDefaultArray();
  }
}
