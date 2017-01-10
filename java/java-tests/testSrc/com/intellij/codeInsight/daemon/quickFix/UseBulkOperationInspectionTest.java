/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.codeInsight.daemon.quickFix;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.bulkOperation.BulkMethodInfo;
import com.intellij.codeInspection.bulkOperation.BulkMethodInfoProvider;
import com.intellij.codeInspection.bulkOperation.UseBulkOperationInspection;
import com.intellij.openapi.extensions.Extensions;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;


public class UseBulkOperationInspectionTest extends LightQuickFixParameterizedTestCase {

  private static final BulkMethodInfoProvider TEST_PROVIDER = new BulkMethodInfoProvider() {
    @NotNull
    @Override
    public Stream<BulkMethodInfo> consumers() {
      return Stream.of(new BulkMethodInfo("testpackage.TestClass", "test", "test"));
    }
  };

  @NotNull
  @Override
  protected LocalInspectionTool[] configureLocalInspectionTools() {
    UseBulkOperationInspection inspection = new UseBulkOperationInspection();
    inspection.USE_ARRAYS_AS_LIST = true;
    return new LocalInspectionTool[]{
      inspection
    };
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Extensions.getArea(null).getExtensionPoint(BulkMethodInfoProvider.KEY.getName()).registerExtension(TEST_PROVIDER);
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      Extensions.getArea(null).getExtensionPoint(BulkMethodInfoProvider.KEY.getName()).unregisterExtension(TEST_PROVIDER);
    }
    finally {
      super.tearDown();
    }
  }

  public void test() throws Exception { doAllTests(); }

  @Override
  protected String getBasePath() {
    return "/codeInsight/daemonCodeAnalyzer/quickFix/useBulkOperation";
  }
}