package org.jetbrains.plugins.template.toolWindow

import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class ChatToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // create a ComposePanel
        val composePanel = ComposePanel().apply {
            // set the content of the panel
            setContent {
                ChatToolWindow()
            }
        }
        val content = ContentFactory.getInstance().createContent(composePanel, "Chat", false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

}
