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
package org.zxg.eclipse.plugin.comment.copyright.handlers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.zxg.eclipse.plugin.comment.copyright.Activator;

/**
 * 
 * @author Xianguang Zhou <xianguang.zhou@outlook.com>
 */
public class AddCopyRightHeaderCommentHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return null;
		}

		IWorkbenchWindow activeWorkbenchWindow = workbench
				.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return null;
		}

		IWorkbenchPage activeWorkbenchPage = activeWorkbenchWindow
				.getActivePage();
		if (activeWorkbenchPage == null) {
			return null;
		}

		IEditorPart activeEditorPart = activeWorkbenchPage.getActiveEditor();
		if (activeEditorPart == null) {
			return null;
		}

		if (!(activeEditorPart instanceof ITextEditor)) {
			return null;
		}
		ITextEditor activeTextEditor = (ITextEditor) activeEditorPart;

		IEditorInput editorInput = activeTextEditor.getEditorInput();
		if (!(editorInput instanceof IFileEditorInput)) {
			return null;
		}
		IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;

		IFile file = fileEditorInput.getFile();

		IProject activeProject = file.getProject();

		IFile licenseHeaderFile = activeProject.getFile("licenseheader.txt");
		Map<String, Object> licenseHeaderTemplateContext = new HashMap<String, Object>();
		licenseHeaderTemplateContext.put("project",
				new HashMap<String, Object>());
		licenseHeaderTemplateContext.put("user",
				System.getProperty("user.name"));
		licenseHeaderTemplateContext.put("date", new Date());
		licenseHeaderTemplateContext.put("licenseFirst", "/*");
		licenseHeaderTemplateContext.put("licensePrefix", " * ");
		licenseHeaderTemplateContext.put("licenseLast", " */");
		String licenseHeaderComment;
		try {
			licenseHeaderComment = Activator
					.getDefault()
					.getFreeMarkerService()
					.applyTemplate(licenseHeaderFile,
							licenseHeaderTemplateContext);
		} catch (Exception ex) {
			throw new ExecutionException(ex.getMessage(), ex);
		}

		IDocumentProvider documentProvider = activeTextEditor
				.getDocumentProvider();
		if (documentProvider == null) {
			return null;
		}

		IDocument activeDocument = documentProvider.getDocument(editorInput);
		if (activeDocument == null) {
			return null;
		}

		activeDocument.set(licenseHeaderComment + activeDocument.get());

		return null;
	}

}
