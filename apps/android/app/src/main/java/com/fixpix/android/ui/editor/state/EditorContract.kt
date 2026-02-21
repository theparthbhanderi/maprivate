package com.fixpix.android.ui.editor.state

import android.net.Uri
import androidx.compose.ui.graphics.Path

// Intent: User Actions or System Events
sealed class EditorEvent {
    data class LoadImage(val uri: Uri) : EditorEvent()
    data class UpdateSettings(val settings: EditorSettings) : EditorEvent()
    
    // History
    object Undo : EditorEvent()
    object Redo : EditorEvent()
    
    // Masking
    object EnterMaskMode : EditorEvent()
    object ExitMaskMode : EditorEvent()
    data class AddMaskPath(val path: Path) : EditorEvent()
    object ClearMask : EditorEvent()
    
    // Processing
    object GenerateResult : EditorEvent()
    object SaveResult : EditorEvent()
    object ResetEditor : EditorEvent()
}

// Effect: One-off pulses to UI (Navigation, Toast, Vibration)
sealed class EditorEffect {
    data class ShowError(val message: String) : EditorEffect()
    data class ShowSuccess(val message: String) : EditorEffect()
    object TriggerHaptic : EditorEffect()
    object NavigateBack : EditorEffect()
}
