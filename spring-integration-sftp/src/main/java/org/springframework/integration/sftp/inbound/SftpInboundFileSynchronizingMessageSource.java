/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.sftp.inbound;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import org.springframework.integration.MessagingException;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.remote.synchronizer.AbstractInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.filters.SftpPatternMatchingFileListFilter;

import com.jcraft.jsch.ChannelSftp;

/**
 * a {@link org.springframework.integration.core.MessageSource} implementation for SFTP
 *
 * @author Josh Long
 * @author Oleg Zhurakousky
 * @since 2.0
 */
public class SftpInboundFileSynchronizingMessageSource extends AbstractInboundFileSynchronizingMessageSource<ChannelSftp.LsEntry> {

	private volatile Pattern filenamePattern;


	public void setFilenamePattern(Pattern filenamePattern) {
		this.filenamePattern = filenamePattern;
	}

	public String getComponentType() {
		return "sftp:inbound-channel-adapter";
	}

	@Override
	protected void onInit() {
		try {
			if (this.localDirectory != null && !this.localDirectory.exists()) {
				if (this.autoCreateDirectories) {
					if (logger.isDebugEnabled()) {
						logger.debug("The '" + this.localDirectory + "' directory doesn't exist; Will create.");
					}
					this.localDirectory.getFile().mkdirs();
				}
				else {
					throw new FileNotFoundException(this.localDirectory.getFilename());
				}
			}
			// Forwards files once they appear in the {@link #localDirectory}.
			this.fileSource = new FileReadingMessageSource();
			this.fileSource.setDirectory(this.localDirectory.getFile());
			this.fileSource.afterPropertiesSet();
			if (this.filenamePattern != null) {
				SftpPatternMatchingFileListFilter filter = new SftpPatternMatchingFileListFilter(this.filenamePattern);
				this.synchronizer.setFilter(filter);
			}
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			throw new MessagingException("Failure during initialization of MessageSource for: " + this.getComponentType(), e);
		}
	}

}
