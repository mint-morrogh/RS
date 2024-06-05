package net.botwithus

import net.botwithus.rs3.imgui.ImGui
import net.botwithus.rs3.imgui.ImGuiWindowFlag
import net.botwithus.rs3.script.ScriptConsole
import net.botwithus.rs3.script.ScriptGraphicsContext

class ZezimaxGraphicsContext(
    private val script: Zezimax,
    console: ScriptConsole
) : ScriptGraphicsContext (console) {

    override fun drawSettings() {
        super.drawSettings()
        if (ImGui.Begin("My script", ImGuiWindowFlag.None.value)) {
            if (ImGui.BeginTabBar("My bar", ImGuiWindowFlag.None.value)) {
                if (ImGui.BeginTabItem("Settings", ImGuiWindowFlag.None.value)) {
                    ImGui.Text("Welcome to my script!")
                    ImGui.Text("My scripts state is: " + script.botState)
                    ImGui.EndTabItem()
                }
                if (ImGui.BeginTabItem("Other", ImGuiWindowFlag.None.value)) {
                    script.someBoolean = ImGui.Checkbox("Are you cool?", script.someBoolean)
                    ImGui.EndTabItem()
                }
                ImGui.EndTabBar()
            }
            ImGui.End()
        }
    }

    override fun drawOverlay() {
        super.drawOverlay()
    }

}