import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Wand2, Sparkles, Loader2, Check } from 'lucide-react';
import { cn } from '../../lib/utils';

/**
 * AI POWER SWITCH (Generate Button)
 * 
 * A "Ceremony Moment" button.
 * - Pulse/Breathing idle state
 * - Morphing processing state
 * - Haptic press physics
 */
const GenerateButton = ({
    onCommit,
    pendingCount = 0,
    isProcessing = false,
    disabled = false
}) => {

    // Valid state for rendering
    const isReady = pendingCount > 0 && !isProcessing && !disabled;

    return (
        <AnimatePresence mode="wait">
            {/* 1. IDLE / READY STATE */}
            {!isProcessing ? (
                <motion.button
                    key="generate-btn"
                    initial={{ opacity: 0, y: 10, scale: 0.95 }}
                    animate={{
                        opacity: 1, y: 0, scale: 1,
                        // Subtle breathing animation when ready
                        boxShadow: isReady
                            ? [
                                "0 4px 12px rgba(var(--ios-accent), 0.3)",
                                "0 8px 24px rgba(var(--ios-accent), 0.5)",
                                "0 4px 12px rgba(var(--ios-accent), 0.3)"
                            ]
                            : "none"
                    }}
                    exit={{ opacity: 0, scale: 0.9 }}
                    transition={{
                        boxShadow: {
                            duration: 2,
                            repeat: Infinity,
                            ease: "easeInOut"
                        }
                    }}
                    whileHover={isReady ? {
                        scale: 1.02,
                        boxShadow: "0 12px 40px rgba(var(--ios-accent), 0.6)",
                        filter: "brightness(1.1)"
                    } : {}}
                    whileTap={isReady ? { scale: 0.96 } : {}}
                    onClick={onCommit}
                    disabled={!isReady && !disabled} // Logic handled by parent, but visual disabled state here
                    className={cn(
                        "relative w-full h-[56px] rounded-[18px] overflow-hidden group",
                        "flex items-center justify-center gap-3",
                        "text-[16px] font-bold tracking-wide uppercase",
                        "transition-all duration-300",
                        // Cyber-Lab Gradient
                        isReady
                            ? "bg-gradient-to-r from-[#007AFF] via-[#5E5CE6] to-[#AF52DE] text-white"
                            : "bg-black/5 dark:bg-white/5 text-black/30 dark:text-white/30 cursor-not-allowed border border-black/5 dark:border-white/5"
                    )}
                >
                    {/* Inner sheen effect */}
                    {isReady && (
                        <div className="absolute inset-0 bg-gradient-to-b from-white/20 to-transparent pointer-events-none" />
                    )}

                    {/* Content */}
                    <div className="relative z-10 flex items-center gap-2">
                        {isReady ? (
                            <>
                                <Wand2 size={20} strokeWidth={2.5} className="fill-white/20" />
                                <span>Generate</span>
                                {pendingCount > 0 && (
                                    <span className="bg-white/20 px-1.5 py-0.5 rounded text-[12px] font-bold min-w-[20px] text-center">
                                        {pendingCount}
                                    </span>
                                )}
                            </>
                        ) : (
                            <span className="font-medium normal-case tracking-normal italic opacity-80">
                                Select a tool to begin
                            </span>
                        )}
                    </div>

                    {/* Animated Particles (Optional Polish) */}
                    {isReady && (
                        <motion.div
                            className="absolute right-4 top-1/2 -translate-y-1/2 text-white/30"
                            animate={{ rotate: 360 }}
                            transition={{ duration: 10, repeat: Infinity, ease: "linear" }}
                        >
                            <Sparkles size={40} className="w-full h-full opacity-10" />
                        </motion.div>
                    )}

                </motion.button>
            ) : (
                /* 2. PROCESSING STATE (Morphing Loader) */
                <motion.div
                    key="processing-btn"
                    initial={{ opacity: 0, scale: 0.9 }}
                    animate={{ opacity: 1, scale: 1 }}
                    exit={{ opacity: 0, scale: 0.9 }}
                    className="w-full h-[56px] rounded-[18px] bg-black/5 dark:bg-white/10 border border-black/5 dark:border-white/10 flex items-center justify-center gap-3"
                >
                    <motion.div
                        animate={{ rotate: 360 }}
                        transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                    >
                        <Loader2 size={22} className="text-[var(--accent)]" />
                    </motion.div>
                    <span className="text-[14px] font-semibold text-[var(--ios-label-secondary)] tracking-wide uppercase animate-pulse">
                        Processing AI...
                    </span>
                </motion.div>
            )}
        </AnimatePresence>
    );
};

export default GenerateButton;
