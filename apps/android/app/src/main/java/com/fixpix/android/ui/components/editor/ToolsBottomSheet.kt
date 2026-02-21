package com.fixpix.android.ui.components.editor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixpix.android.ui.components.FixPixButton
import com.fixpix.android.ui.theme.FixPixTheme
import kotlinx.coroutines.launch

data class EditorSettings(
    // Enhance
    val faceRestoration: Boolean = false,
    val scratchRemoval: Boolean = false,
    val colorization: Boolean = false,
    val upscaleLevel: Int = 0, // 0=Off, 1=2x, 2=4x
    // Magic
    val autoEnhance: Boolean = false,
    val removeBackground: Boolean = false,
    // Adjust
    val brightness: Float = 1f,
    val contrast: Float = 1f,
    val saturation: Float = 1f,
    // Filters
    val selectedFilter: String = "None"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun ToolsBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    settings: EditorSettings,
    onSettingsChange: (EditorSettings) -> Unit,
    onObjectEraserClick: () -> Unit,
    onGenerate: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!visible) return
    
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    
    val tabs = listOf("Enhance", "Magic", "Adjust", "Filters")
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FixPixTheme.colors.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FixPixTheme.colors.fill.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Tabs
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = FixPixTheme.colors.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = FixPixTheme.colors.primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = FixPixTheme.colors.primary,
                        unselectedContentColor = FixPixTheme.colors.textSecondary
                    )
                }
            }
            
            // Tab Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) { page ->
                when (page) {
                    0 -> EnhancePanel(settings, onSettingsChange)
                    1 -> MagicPanel(settings, onSettingsChange, onObjectEraserClick)
                    2 -> AdjustPanel(settings, onSettingsChange)
                    3 -> FiltersPanel(settings, onSettingsChange)
                }
            }
            
            // Generate Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                FixPixButton(
                    onClick = onGenerate,
                    text = "Generate",
                    fullWidth = true
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun EnhancePanel(settings: EditorSettings, onSettingsChange: (EditorSettings) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        ToolToggleRow(
            label = "Face Restoration",
            checked = settings.faceRestoration,
            onCheckedChange = { onSettingsChange(settings.copy(faceRestoration = it)) }
        )
        ToolToggleRow(
            label = "Scratch Removal",
            checked = settings.scratchRemoval,
            onCheckedChange = { onSettingsChange(settings.copy(scratchRemoval = it)) }
        )
        ToolToggleRow(
            label = "Colorization",
            checked = settings.colorization,
            onCheckedChange = { onSettingsChange(settings.copy(colorization = it)) }
        )
        UpscaleSegmentedControl(
            selected = settings.upscaleLevel,
            onSelectionChange = { onSettingsChange(settings.copy(upscaleLevel = it)) }
        )
    }
}

@Composable
private fun MagicPanel(
    settings: EditorSettings, 
    onSettingsChange: (EditorSettings) -> Unit,
    onObjectEraserClick: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        ToolToggleRow(
            label = "Auto Enhance",
            checked = settings.autoEnhance,
            onCheckedChange = { onSettingsChange(settings.copy(autoEnhance = it)) }
        )
        ToolToggleRow(
            label = "Remove Background",
            checked = settings.removeBackground,
            onCheckedChange = { onSettingsChange(settings.copy(removeBackground = it)) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            FixPixButton(
                onClick = onObjectEraserClick,
                text = "Object Eraser",
                fullWidth = true,
                variant = com.fixpix.android.ui.components.ButtonVariant.Tinted
            )
        }
    }
}

@Composable
private fun AdjustPanel(settings: EditorSettings, onSettingsChange: (EditorSettings) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        ToolSliderRow(
            label = "Brightness",
            value = settings.brightness,
            onValueChange = { onSettingsChange(settings.copy(brightness = it)) }
        )
        ToolSliderRow(
            label = "Contrast",
            value = settings.contrast,
            onValueChange = { onSettingsChange(settings.copy(contrast = it)) }
        )
        ToolSliderRow(
            label = "Saturation",
            value = settings.saturation,
            onValueChange = { onSettingsChange(settings.copy(saturation = it)) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FiltersPanel(settings: EditorSettings, onSettingsChange: (EditorSettings) -> Unit) {
    val filters = listOf("None", "Vintage", "Cinematic", "Warm", "Cool", "B&W")
    
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        filters.forEach { filter ->
            FilterGridItem(
                name = filter,
                isSelected = settings.selectedFilter == filter,
                onClick = { onSettingsChange(settings.copy(selectedFilter = filter)) }
            )
        }
    }
}
