/*
 * Copyright (C) 2015 Xianguang Zhou <xianguang.zhou@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.zxg.eclipse.plugin.comment.copyright.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 
 * @author Xianguang Zhou <xianguang.zhou@outlook.com>
 */
public class FreeMarkerServiceImpl {

	private Configuration configuration;

	public FreeMarkerServiceImpl() {
		configuration = new Configuration(
				Configuration.VERSION_2_3_22);
		configuration
				.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}

	public String applyTemplate(IFile templateFile, Map<String, Object> context)
			throws CoreException, IOException, TemplateException {
		IPath templateFileLocation = templateFile.getLocation();
		File file = templateFileLocation.toFile();
		File dir = file.getParentFile();
		configuration.setDirectoryForTemplateLoading(dir);
		Template template = configuration.getTemplate(templateFile.getName(),
				templateFile.getCharset());
		StringWriter stringWriter = new StringWriter();
		template.process(context, stringWriter);
		return stringWriter.toString();
	}
}
