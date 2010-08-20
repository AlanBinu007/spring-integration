/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.file.filters;

import java.io.File;
import java.util.List;

/**
 * Strategy interface for filtering a group of files.
 *
 * @author Iwein Fuld
 */
@Deprecated
public interface FileListFilter {

    /**
     * Filters out files and returns the files that are left in a list, or an
     * empty list when a null is passed in.
     */
    List<File> filterFiles(File[] files);

}
