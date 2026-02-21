package com.fixpix.android.ui.editor.state

import android.net.Uri
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(EditorState())
    val state: StateFlow<EditorState> = _state.asStateFlow()
    
    // Effects Channel
    private val _effects = Channel<EditorEffect>()
    val effects = _effects.receiveAsFlow()
    
    // History Stack
    private val historyStack = ArrayDeque<EditorSettings>()
    private var historyIndex = -1

    fun onEvent(event: EditorEvent) {
        when (event) {
            is EditorEvent.LoadImage -> loadImage(event.uri)
            is EditorEvent.UpdateSettings -> updateSettings(event.settings)
            is EditorEvent.Undo -> undo()
            is EditorEvent.Redo -> redo()
            is EditorEvent.EnterMaskMode -> enterMaskMode()
            is EditorEvent.ExitMaskMode -> exitMaskMode()
            is EditorEvent.AddMaskPath -> updateMaskPath(event.path)
            is EditorEvent.ClearMask -> clearMask()
            is EditorEvent.GenerateResult -> generateResult()
            is EditorEvent.SaveResult -> saveResult()
            is EditorEvent.ResetEditor -> resetEditor()
        }
    }

    private fun loadImage(uri: Uri) {
        _state.update { 
            it.copy(
                originalImageUri = uri,
                processedImageUri = null,
                mode = EditorMode.DEFAULT,
                settings = EditorSettings(),
                maskPaths = emptyList(),
                showComparison = false
            ) 
        }
        historyStack.clear()
        historyIndex = -1
        addToHistory(EditorSettings())
    }

    private fun updateSettings(newSettings: EditorSettings) {
        if (_state.value.settings != newSettings) {
            _state.update { it.copy(settings = newSettings) }
            addToHistory(newSettings)
            sendEffect(EditorEffect.TriggerHaptic)
        }
    }

    private fun addToHistory(settings: EditorSettings) {
        // Prevent storing identical consecutive states
        if (historyIndex >= 0 && historyStack.getOrNull(historyIndex) == settings) return

        // Remove forward history if branching
        while (historyStack.size > historyIndex + 1) {
            historyStack.removeLast()
        }
        historyStack.add(settings)
        if (historyStack.size > 20) historyStack.removeFirst() else historyIndex++ // Increased history limit
        
        updateHistoryState()
    }
    
    private fun undo() {
        if (historyIndex > 0) {
            historyIndex--
            val prevSettings = historyStack[historyIndex]
            _state.update { it.copy(settings = prevSettings) }
            updateHistoryState()
            sendEffect(EditorEffect.TriggerHaptic)
        }
    }
    
    private fun redo() {
        if (historyIndex < historyStack.size - 1) {
            historyIndex++
            val nextSettings = historyStack[historyIndex]
            _state.update { it.copy(settings = nextSettings) }
            updateHistoryState()
            sendEffect(EditorEffect.TriggerHaptic)
        }
    }
    
    private fun updateHistoryState() {
        _state.update { 
            it.copy(
                canUndo = historyIndex > 0,
                canRedo = historyIndex < historyStack.size - 1
            ) 
        }
    }

    private fun enterMaskMode() {
        _state.update { it.copy(mode = EditorMode.MASKING) }
    }

    private fun exitMaskMode() {
        _state.update { it.copy(mode = EditorMode.DEFAULT) }
    }
    
    private fun updateMaskPath(newPath: Path) {
         val currentPaths = _state.value.maskPaths.toMutableList()
         currentPaths.add(newPath)
         _state.update { it.copy(maskPaths = currentPaths) }
    }
    
    private fun clearMask() {
        _state.update { it.copy(maskPaths = emptyList()) }
        sendEffect(EditorEffect.TriggerHaptic)
    }

    private fun generateResult() {
        if (_state.value.isProcessing) return
        
        viewModelScope.launch {
            _state.update { 
                it.copy(isProcessing = true, processingStep = ProcessingStep.ANALYZING) 
            }
            delay(1000)
            _state.update { it.copy(processingStep = ProcessingStep.ENHANCING) }
            delay(1500)
            _state.update { it.copy(processingStep = ProcessingStep.FINALIZING) }
            delay(800)
            
            // Success
            _state.update { 
                it.copy(
                    isProcessing = false, 
                    processingStep = ProcessingStep.IDLE,
                    processedImageUri = it.originalImageUri, // Mock
                    mode = EditorMode.PROCESSED,
                    showComparison = true
                ) 
            }
            sendEffect(EditorEffect.ShowSuccess("Image processed successfully"))
            sendEffect(EditorEffect.TriggerHaptic)
        }
    }
    
    private fun saveResult() {
        // Mock Save
        viewModelScope.launch {
            delay(500)
            sendEffect(EditorEffect.ShowSuccess("Saved to Gallery"))
        }
    }
    
    private fun resetEditor() {
        _state.update { 
             it.copy(settings = EditorSettings(), maskPaths = emptyList()) 
        }
        addToHistory(EditorSettings())
        sendEffect(EditorEffect.TriggerHaptic)
    }

    private fun sendEffect(effect: EditorEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
