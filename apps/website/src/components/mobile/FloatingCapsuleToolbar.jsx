import React, { memo, useState, useCallback, useEffect, useRef, useMemo } from 'react';
import { motion, AnimatePresence, useDragControls } from 'framer-motion';
import { useCommand } from '../../context/CommandContext';
import { useImage } from '../../context/ImageContext';
import { Wand2, Sparkles, Palette, SlidersHorizontal, X } from 'lucide-react';
import { cn } from '../../lib/utils';

// Import new Tool Components
import {
    RestoreTools,
    EnhanceTools,
    CreateTools,
    AdjustTools
} from '../tools';

/**
 * FLOATING CAPSULE TOOLBAR - Performance Optimized
 * 
 * Optimizations:
 * - React.memo on all components
 * - Faster spring animations (500 stiffness)
 * - Memoized callbacks
 * - Reduced backdrop blur for mobile performance
 * - GPU-accelerated transforms
 */

// ═══════════════════════════════════════════════════════════════
// CONSTANTS
// ═══════════════════════════════════════════════════════════════

const CATEGORIES = [
    { id: 'restore', label: 'Restore', icon: Wand2, color: '#007AFF' },
    { id: 'enhance', label: 'Enhance', icon: Sparkles, color: '#34C759' },
    { id: 'create', label: 'Creative', icon: Palette, color: '#FF9500' },
    { id: 'adjust', label: 'Adjust', icon: SlidersHorizontal, color: '#AF52DE' },
];

// ═══════════════════════════════════════════════════════════════
// CAPSULE ICON BUTTON - High Visibility, Performance Optimized
// ═══════════════════════════════════════════════════════════════

const CapsuleIconButton = memo(({ category, isActive, onTap }) => {
    const { id, label, icon: Icon, color } = category;

    const handleClick = useCallback(() => {
        onTap(id);
    }, [onTap, id]);

    // Memoized styles
    const iconStyle = useMemo(() => ({
        color: isActive ? color : undefined,
        filter: isActive
            ? `drop-shadow(0 0 8px ${color}50)`
            : 'drop-shadow(0 1px 1px rgba(0,0,0,0.1))'
    }), [isActive, color]);

    return (
        <motion.button
            onClick={handleClick}
            whileTap={{ scale: 0.88 }}
            className={cn(
                "relative w-16 h-14 flex flex-col items-center justify-center gap-[2px] rounded-xl transition-all duration-150"
            )}
            style={{
                background: 'transparent',
                boxShadow: 'none',
                willChange: 'transform'
            }}
        >
            {/* Icon with high visibility */}
            <motion.div
                animate={{
                    scale: isActive ? 1.1 : 1,
                    y: isActive ? -2 : 0
                }}
                transition={{ type: 'spring', stiffness: 600, damping: 25 }}
                className="relative"
            >
                <Icon
                    size={24}
                    strokeWidth={isActive ? 2.3 : 2}
                    className={cn(
                        "transition-colors duration-150",
                        !isActive && "capsule-icon-inactive"
                    )}
                    style={iconStyle}
                />
            </motion.div>

            {/* Label - always visible */}
            <span className={cn(
                "text-[9px] font-medium tracking-tight transition-colors duration-150",
                isActive
                    ? "text-[rgb(var(--ios-label))]"
                    : "capsule-label-inactive"
            )}>
                {label}
            </span>

            {/* Active indicator dot */}
            {isActive && (
                <motion.div
                    initial={{ scale: 0, opacity: 0 }}
                    animate={{ scale: 1, opacity: 1 }}
                    transition={{ type: 'spring', stiffness: 600, damping: 25 }}
                    className="absolute -bottom-1.5 w-1 h-1 rounded-full"
                    style={{ backgroundColor: color }}
                />
            )}
        </motion.button>
    );
});

CapsuleIconButton.displayName = 'CapsuleIconButton';

// ═══════════════════════════════════════════════════════════════
// MAIN COMPONENT
// ═══════════════════════════════════════════════════════════════

const FloatingCapsuleToolbar = memo(() => {
    const [isExpanded, setIsExpanded] = useState(false);
    const [activeCategory, setActiveCategory] = useState(null);
    const dragControls = useDragControls();
    const panelRef = useRef(null);

    const { setExpandedZone } = useCommand();

    // Sync with command context
    useEffect(() => {
        if (activeCategory) {
            setExpandedZone(activeCategory);
        }
    }, [activeCategory, setExpandedZone]);

    // ─────────────────────────────────────────────────────────────
    // HANDLERS - Memoized
    // ─────────────────────────────────────────────────────────────

    const handleIconTap = useCallback((categoryId) => {
        if (activeCategory === categoryId && isExpanded) {
            setIsExpanded(false);
            setActiveCategory(null);
        } else {
            setActiveCategory(categoryId);
            setIsExpanded(true);
        }
    }, [activeCategory, isExpanded]);

    const handleClose = useCallback(() => {
        setIsExpanded(false);
        setActiveCategory(null);
    }, []);

    const handleBackdropTap = useCallback((e) => {
        if (e.target === e.currentTarget) {
            handleClose();
        }
    }, [handleClose]);

    const handleDragEnd = useCallback((event, info) => {
        if (info.velocity.y > 300 || info.offset.y > 100) {
            handleClose();
        }
    }, [handleClose]);

    const handleDragStart = useCallback((e) => {
        dragControls.start(e);
    }, [dragControls]);

    // ─────────────────────────────────────────────────────────────
    // TOOL CONTENT RENDERER - Memoized
    // ─────────────────────────────────────────────────────────────
    const toolContent = useMemo(() => {
        switch (activeCategory) {
            case 'restore': return <RestoreTools />;
            case 'enhance': return <EnhanceTools />;
            case 'create': return <CreateTools />;
            case 'adjust': return <AdjustTools />;
            default: return null;
        }
    }, [activeCategory]);

    const activeConfig = useMemo(() =>
        CATEGORIES.find(c => c.id === activeCategory),
        [activeCategory]);

    const activeColor = activeConfig?.color || '#007AFF';
    const ActiveIcon = activeConfig?.icon;

    // Memoized animation configs
    const panelAnimation = useMemo(() => ({
        initial: { y: 100, opacity: 0, scale: 0.95 },
        animate: { y: 0, opacity: 1, scale: 1 },
        exit: { y: 60, opacity: 0, scale: 0.98 },
        transition: {
            type: 'spring',
            stiffness: 500,
            damping: 35,
            mass: 0.8
        }
    }), []);

    const capsuleAnimation = useMemo(() => ({
        initial: { y: 100 },
        animate: { y: 0 },
        transition: {
            type: 'spring',
            stiffness: 500,
            damping: 30,
            delay: 0.1
        }
    }), []);

    // ═══════════════════════════════════════════════════════════════
    // RENDER
    // ═══════════════════════════════════════════════════════════════
    return (
        <>
            {/* BACKDROP - Dims background when expanded */}
            <AnimatePresence>
                {isExpanded && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        transition={{ duration: 0.15 }}
                        className="fixed inset-0 z-40 bg-black/20 backdrop-blur-sm"
                        onClick={handleBackdropTap}
                    />
                )}
            </AnimatePresence>

            {/* EXPANDED TOOL PANEL - Slides up above capsule */}
            <AnimatePresence>
                {isExpanded && activeCategory && (
                    <motion.div
                        ref={panelRef}
                        {...panelAnimation}
                        drag="y"
                        dragControls={dragControls}
                        dragListener={false}
                        dragConstraints={{ top: 0, bottom: 0 }}
                        dragElastic={0.1}
                        onDragEnd={handleDragEnd}
                        className="fixed left-4 right-4 z-50 floating-tool-panel"
                        style={{
                            bottom: 'calc(env(safe-area-inset-bottom, 20px) + 120px)',
                            maxHeight: '50vh',
                            willChange: 'transform'
                        }}
                    >
                        {/* Panel Header with Drag Handle */}
                        <div
                            className="flex items-center justify-between px-5 py-3 border-b border-black/5 dark:border-white/5"
                            onPointerDown={handleDragStart}
                        >
                            <div className="flex items-center gap-3">
                                <div
                                    className="w-8 h-8 rounded-xl flex items-center justify-center"
                                    style={{ backgroundColor: `${activeColor}15` }}
                                >
                                    {ActiveIcon && (
                                        <motion.div
                                            initial={{ rotate: -10 }}
                                            animate={{ rotate: 0 }}
                                            transition={{ type: 'spring', stiffness: 600, damping: 30 }}
                                        >
                                            <ActiveIcon size={18} style={{ color: activeColor }} strokeWidth={2} />
                                        </motion.div>
                                    )}
                                </div>
                                <span className="text-[15px] font-semibold text-[rgb(var(--ios-label))]">
                                    {activeConfig?.label}
                                </span>
                            </div>

                            <motion.button
                                whileTap={{ scale: 0.9 }}
                                onClick={handleClose}
                                className="w-8 h-8 rounded-full flex items-center justify-center bg-black/5 dark:bg-white/10"
                            >
                                <X size={16} strokeWidth={2.5} className="text-[rgb(var(--ios-label-secondary))]" />
                            </motion.button>
                        </div>

                        {/* Panel Content */}
                        <div className="px-5 pt-5 pb-8 overflow-y-auto overscroll-contain space-y-3" style={{ maxHeight: 'calc(50vh - 56px)' }}>
                            {toolContent}
                        </div>
                    </motion.div>
                )}
            </AnimatePresence>

            {/* FLOATING CAPSULE - Always visible, 4 icons */}
            <motion.div
                {...capsuleAnimation}
                className="fixed left-4 right-4 z-50 floating-capsule"
                style={{
                    bottom: 'calc(env(safe-area-inset-bottom, 20px) + 16px)',
                    willChange: 'transform'
                }}
            >
                {/* Icons container */}
                <div className="flex items-center justify-evenly py-1">
                    {CATEGORIES.map((category) => (
                        <CapsuleIconButton
                            key={category.id}
                            category={category}
                            isActive={activeCategory === category.id && isExpanded}
                            onTap={handleIconTap}
                        />
                    ))}
                </div>
            </motion.div>
        </>
    );
});

FloatingCapsuleToolbar.displayName = 'FloatingCapsuleToolbar';

export default FloatingCapsuleToolbar;
