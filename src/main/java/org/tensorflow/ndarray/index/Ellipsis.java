/*
  Copyright 2020 The TensorFlow Authors. All Rights Reserved.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ==============================================================================
 */
package org.tensorflow.ndarray.index;

import org.tensorflow.ndarray.impl.dimension.Dimension;

final class Ellipsis implements Index {

  static final Ellipsis INSTANCE = new Ellipsis();

  private Ellipsis() {

  }

  @Override
  public long numElements(Dimension dim) {
    throw new UnsupportedOperationException("Should be handled in DimensionalSpace.");
  }

  @Override
  public long mapCoordinate(long coordinate, Dimension dim) {
    throw new UnsupportedOperationException("Should be handled in DimensionalSpace.");
  }

  @Override
  public boolean isEllipsis() {
    return true;
  }

  @Override
  public String toString() {
    return Ellipsis.class.getSimpleName() + "()";
  }
}
