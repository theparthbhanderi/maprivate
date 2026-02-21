import React, { memo } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Sparkles } from 'lucide-react';
import { useCommand } from '../../context/CommandContext';
import { useImage } from '../../context/ImageContext';
import { cn } from '../../lib/utils';

/**
 * MobileGenerateFAB - Floating Action Button for Generate
 * 
 * Features:
 * - Shows when pending queue has items
 * - Pulsing glow animation
 * - Shows step count
 * - Disabled during processing
 * 
 * From EDITOR_REDESIGN_SPEC.md Section 6.5
 */
const MobileGenerateFAB = memo(() => {
    const { pendingQueue, commitCommands } = useCommand();
    const { isGenerating } = useImage();

    const handleGenerate = async () => {
        if (commitCommands && !isGenerating) {
            await commitCommands();
        }
    };

    const stepCount = pendingQueue?.length || 0;
    const showFAB = stepCount > 0;

    return (
        <AnimatePresence>
            {showFAB && (
                <motion.button
                    initial={{ scale: 0, opacity: 0 }}
                    animate={{ scale: 1, opacity: 1 }}
                    exit={{ scale: 0, opacity: 0 }}
                    transition={{ type: 'spring', stiffness: 500, damping: 30 }}
                    onClick={handleGenerate}
                    disabled={isGenerating}
                    className={cn(
                        "fixed z-50 flex items-center gap-2 px-5 py-3.5",
                        "rounded-full font-semibold text-[15px]",
                        "shadow-lg",
                        isGenerating
                            ? "bg-[var(--ios-text-tertiary)] text-white/70"
                            : "bg-gradient-to-r from-[var(--accent-primary)] to-[var(--accent-enhance)] text-white"
                    )}
                    style={{
                        right: '16px',
                        bottom: 'calc(env(safe-area-inset-bottom, 20px) + 100px)',
                        boxShadow: isGenerating
                            ? '0 8px 24px rgba(0,0,0,0.2)'
                            : '0 8px 32px rgba(var(--studio-glow), 0.4)'
                    }}
                    whileTap={{ scale: 0.95 }}
                >
                    {isGenerating ? (
                        <>
                            <motion.div
                                className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full"
                                animate={{ rotate: 360 }}
                                transition={{ duration: 1, repeat: Infinity, ease: 'linear' }}
                            />
                            <span>Processing...</span>
                        </>
                    ) : (
                        <>
                            <Sparkles size={18} />
                            <span>
                                Generate
                                <span className="ml-1.5 px-1.5 py-0.5 bg-white/20 rounded-full text-[12px]">
                                    {stepCount}
                                </span>
                            </span>
                        </>
                    )}

                    {/* Pulse Animation when ready */}
                    {!isGenerating && (
                        <motion.div
                            className="absolute inset-0 rounded-full bg-gradient-to-r from-[var(--accent-primary)] to-[var(--accent-enhance)]"
                            animate={{
                                scale: [1, 1.15, 1],
                                opacity: [0.5, 0, 0.5]
                            }}
                            transition={{
                                duration: 2,
                                repeat: Infinity,
                                ease: 'easeInOut'
                            }}
                            style={{ zIndex: -1 }}
                        />
                    )}
                </motion.button>
            )}
        </AnimatePresence>
    );
});

MobileGenerateFAB.displayName = 'MobileGenerateFAB';

export default MobileGenerateFAB;
