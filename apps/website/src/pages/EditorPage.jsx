import React, { useState, useEffect, useRef } from 'react';
import { AnimatePresence, motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import {
    Wand2, Sparkles, Palette, SlidersHorizontal,
    Download, Undo2, Redo2, Columns, ChevronLeft
} from 'lucide-react';

// Editor Components
import { StudioBackdrop, CanvasStage } from '../components/editor';

// Tool Panels
import { RestoreTools, EnhanceTools, CreateTools, AdjustTools } from '../components/tools';
import GenerateButton from '../components/editor/GenerateButton';
import PendingQueue from '../components/editor/PendingQueue';

// Core Features
import ImageWorkspace from '../components/features/ImageWorkspace';
import OnboardingTour from '../components/features/OnboardingTour';
import QuickStartModal from '../components/features/QuickStartModal';
import KeyboardShortcutsHelp from '../components/ui/KeyboardShortcutsHelp';
import ExportModal from '../components/features/ExportModal';

// Mobile Components
import FloatingCapsuleToolbar from '../components/mobile/FloatingCapsuleToolbar';
import MobileEditorHeader from '../components/mobile/MobileEditorHeader';
import MobileGenerateFAB from '../components/mobile/MobileGenerateFAB';

// Context
import { useCommand } from '../context/CommandContext';
import { useImage } from '../context/ImageContext';

// Hooks
import useKeyboardShortcuts from '../hooks/useKeyboardShortcuts';

import '../styles/studio-system.css';

/* ────────────────────────────────────────────────────
   TAB DEFINITIONS
   ──────────────────────────────────────────────────── */
const tabs = [
    { id: 'restore', label: 'Restore', icon: Wand2 },
    { id: 'enhance', label: 'Enhance', icon: Sparkles },
    { id: 'create', label: 'Create', icon: Palette },
    { id: 'adjust', label: 'Adjust', icon: SlidersHorizontal },
];

/* ────────────────────────────────────────────────────
   LEFT TOOLS PANEL — Full iOS-style sidebar
   All editing tools in one place
   ──────────────────────────────────────────────────── */
const LeftToolsPanel = ({
    activeTab, onTabChange,
    onExport, onGenerate, isProcessing,
}) => {
    const {
        undo, redo, canUndo, canRedo,
        showComparison, setShowComparison
    } = useImage();

    const activeIndex = tabs.findIndex(t => t.id === activeTab);

    const renderTabContent = () => {
        switch (activeTab) {
            case 'restore': return <RestoreTools />;
            case 'enhance': return <EnhanceTools />;
            case 'create': return <CreateTools />;
            case 'adjust': return <AdjustTools />;
            default: return null;
        }
    };

    return (
        <motion.div
            initial={{ opacity: 0, x: 16 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.18, ease: [0.25, 1, 0.5, 1] }}
            style={{
                width: 340,
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                borderRadius: 28,
                background: 'var(--glass-bg-solid)',
                backdropFilter: 'blur(24px) saturate(150%)',
                WebkitBackdropFilter: 'blur(24px) saturate(150%)',
                border: '1px solid var(--glass-border)',
                boxShadow: '0 10px 40px rgba(0,0,0,0.15)',
                overflow: 'hidden',
            }}
        >
            {/* ─── Quick Actions Bar ───────────────── */}
            <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                gap: 16,
                padding: '0 20px',
                height: 56,
                flexShrink: 0,
                borderBottom: '1px solid var(--glass-border)',
            }}>
                {[
                    { icon: Undo2, label: 'Undo', onClick: undo, disabled: !canUndo },
                    { icon: Redo2, label: 'Redo', onClick: redo, disabled: !canRedo },
                    { icon: Columns, label: 'Compare', onClick: () => setShowComparison?.(!showComparison), active: showComparison },
                    { icon: Download, label: 'Export', onClick: onExport },
                ].map((btn, i) => {
                    const Icon = btn.icon;
                    const isDisabled = isProcessing || btn.disabled;
                    return (
                        <motion.button
                            key={i}
                            onClick={btn.onClick}
                            disabled={isDisabled}
                            whileTap={!isDisabled ? { scale: 0.96 } : {}}
                            whileHover={!isDisabled ? { background: 'rgba(0,122,255,0.08)' } : {}}
                            title={btn.label}
                            style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                width: 40, height: 40,
                                borderRadius: 12,
                                border: 'none',
                                cursor: isDisabled ? 'not-allowed' : 'pointer',
                                opacity: isDisabled ? 0.3 : btn.active ? 1 : 0.6,
                                transition: 'all 150ms ease',
                                background: btn.active ? 'rgba(0,122,255,0.08)' : 'transparent',
                                color: btn.active ? '#007AFF' : 'inherit',
                            }}
                        >
                            <Icon size={20} strokeWidth={1.8} />
                        </motion.button>
                    );
                })}
            </div>

            {/* ─── Tab Navigation ───────────────── */}
            <div style={{
                display: 'flex',
                position: 'relative',
                height: 64,
                borderBottom: '1px solid var(--glass-border)',
                flexShrink: 0,
            }}>
                {tabs.map((tab) => {
                    const Icon = tab.icon;
                    const isActive = activeTab === tab.id;
                    return (
                        <motion.button
                            key={tab.id}
                            onClick={() => onTabChange(tab.id)}
                            whileTap={{ scale: 0.96 }}
                            style={{
                                flex: 1,
                                minWidth: 88,
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                                justifyContent: 'center',
                                gap: 6,
                                padding: '10px 0',
                                background: 'none',
                                border: 'none',
                                cursor: 'pointer',
                                opacity: isActive ? 1 : 0.55,
                                color: isActive ? 'var(--accent)' : 'inherit',
                                fontWeight: isActive ? 600 : 500,
                                transition: 'all 150ms ease',
                            }}
                        >
                            <Icon size={18} strokeWidth={isActive ? 2 : 1.75} />
                            <span style={{
                                fontSize: 13,
                                textTransform: 'uppercase',
                                letterSpacing: '0.3px',
                            }}>
                                {tab.label}
                            </span>
                        </motion.button>
                    );
                })}
                {/* Active underline — 60% width of each tab, centered */}
                <motion.div
                    style={{
                        position: 'absolute', bottom: 0,
                        height: 2, borderRadius: 2,
                        backgroundColor: 'var(--accent)',
                    }}
                    animate={{
                        left: `${(activeIndex * (100 / tabs.length)) + ((100 / tabs.length) * 0.2)}%`,
                        width: `${(100 / tabs.length) * 0.6}%`,
                    }}
                    transition={{ type: 'spring', stiffness: 500, damping: 35 }}
                />
            </div>

            {/* ─── Scrollable Tool Cards ─── */}
            <div style={{
                flex: 1,
                overflowY: 'auto',
                padding: 18,
                display: 'flex',
                flexDirection: 'column',
                gap: 12,
            }}>
                <AnimatePresence mode="wait">
                    <motion.div
                        key={activeTab}
                        initial={{ opacity: 0, y: 8 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: -8 }}
                        transition={{ duration: 0.12 }}
                    >
                        {renderTabContent()}
                    </motion.div>
                </AnimatePresence>
            </div>

            {/* ─── Pending Queue ─── */}
            <AnimatePresence>
                <PendingQueue />
            </AnimatePresence>

            {/* ─── Generate Button (sticky footer) ─── */}
            <div style={{
                padding: '16px 18px 24px',
                borderTop: '1px solid var(--glass-border)',
            }}>
                <GenerateButton />
            </div>
        </motion.div>
    );
};

/* ────────────────────────────────────────────────────
   EDITOR CONTENT
   ──────────────────────────────────────────────────── */
const EditorContent = () => {
    const [isMobile, setIsMobile] = useState(false);
    const [quickStartDone, setQuickStartDone] = useState(false);
    const [showExportModal, setShowExportModal] = useState(false);
    const [activeTab, setActiveTab] = useState('restore');
    const navigate = useNavigate();

    const { commitCommands } = useCommand();
    const {
        originalImage, isGenerating,
        undo, redo, canUndo, canRedo,
        zoom, setZoom,
        showComparison, setShowComparison
    } = useImage();

    useEffect(() => {
        const checkMobile = () => setIsMobile(window.innerWidth < 768);
        checkMobile();
        window.addEventListener('resize', checkMobile);
        return () => window.removeEventListener('resize', checkMobile);
    }, []);

    const handleGenerate = async () => {
        if (commitCommands) await commitCommands();
    };
    const handleExport = () => setShowExportModal(true);
    const handleZoomIn = () => setZoom?.(Math.min((zoom || 1) + 0.25, 3));
    const handleZoomOut = () => setZoom?.(Math.max((zoom || 1) - 0.25, 0.5));
    const handleZoomReset = () => setZoom?.(1);

    const { isPanning } = useKeyboardShortcuts({
        onUndo: undo, onRedo: redo,
        onSave: handleExport, onExport: handleExport,
        onProcess: handleGenerate,
        onCancel: () => setShowExportModal(false),
        onToggleCompare: () => setShowComparison?.(!showComparison),
        onZoomIn: handleZoomIn, onZoomOut: handleZoomOut, onZoomReset: handleZoomReset,
        onTabChange: setActiveTab,
        enabled: !isMobile
    });

    /* ── MOBILE ── */
    if (isMobile) {
        return (
            <div className="fixed inset-0 flex flex-col ios-mobile-bg">
                <MobileEditorHeader
                    title="Restoration"
                    onBack={() => navigate('/app')}
                    onExport={handleExport}
                />
                <div className="flex-1 relative overflow-hidden pt-14 px-4 pb-[120px]">
                    <div className="w-full h-full ios-pro-canvas flex items-center justify-center">
                        <ImageWorkspace />
                    </div>
                </div>
                <FloatingCapsuleToolbar />
                <MobileGenerateFAB />
                <QuickStartModal onClose={() => setQuickStartDone(true)} />
                <AnimatePresence>
                    {showExportModal && <ExportModal onClose={() => setShowExportModal(false)} />}
                </AnimatePresence>
            </div>
        );
    }

    /* ── DESKTOP — Left tools panel + Canvas (no right panel) ── */
    return (
        <div className={`w-full h-full flex-1 relative overflow-hidden ${isPanning ? 'cursor-grab' : ''}`}>

            {/* LAYER 0: Dark canvas backdrop */}
            <StudioBackdrop />

            {/* LAYER 1: Canvas Stage — full width minus left panel */}
            <div
                className="absolute inset-0 z-10 flex items-center justify-center"
                style={{
                    paddingTop: 24,
                    paddingBottom: 24,
                    paddingLeft: 24,
                    paddingRight: 392,
                }}
            >
                <CanvasStage
                    hasImage={!!originalImage}
                    isProcessing={isGenerating}
                >
                    <ImageWorkspace />
                    <div className="absolute bottom-6 left-1/2 -translate-x-1/2 z-50">
                        {quickStartDone && <OnboardingTour />}
                        <KeyboardShortcutsHelp />
                    </div>
                </CanvasStage>
            </div>

            {/* LAYER 2: Left Tools Panel — all editing tools here */}
            <div
                className="absolute z-30 pointer-events-none"
                style={{ top: 24, right: 24, bottom: 24 }}
            >
                <div className="pointer-events-auto h-full">
                    <LeftToolsPanel
                        activeTab={activeTab}
                        onTabChange={setActiveTab}
                        onExport={handleExport}
                        onGenerate={handleGenerate}
                        isProcessing={isGenerating}
                    />
                </div>
            </div>

            <QuickStartModal onClose={() => setQuickStartDone(true)} />
            <AnimatePresence>
                {showExportModal && <ExportModal onClose={() => setShowExportModal(false)} />}
            </AnimatePresence>
        </div>
    );
};

const EditorPage = () => <EditorContent />;

export default EditorPage;
