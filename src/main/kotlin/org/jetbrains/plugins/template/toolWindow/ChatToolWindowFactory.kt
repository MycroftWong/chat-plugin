package org.jetbrains.plugins.template.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JLabel

class ChatToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // create a ComposePanel
        /*val composePanel = ComposePanel().apply {
            // set the content of the panel
            setContent {
                ChatToolWindow()
            }
        }
        val content = ContentFactory.getInstance().createContent(composePanel, "Chat", false)*/
        val panel = SimpleToolWindowPanel(false, true)
        val label = JLabel("Hello, World!")
        panel.add(label)
        val content = ContentFactory.getInstance().createContent(panel, "Chat", false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

}
