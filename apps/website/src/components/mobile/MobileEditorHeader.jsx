import { motion, AnimatePresence } from 'framer-motion';
import { ChevronLeft, Download } from 'lucide-react';
import { useImage } from '../../context/ImageContext';
import { cn } from '../../lib/utils';

/**
 * iOS Pro Mobile Header
 * 
 * Premium refinements:
 * - Thinner, more minimal height
 * - Subtle blur material
 * - Minimal back button (iOS native style)
 * - Light download icon
 * - Soft shadow under navbar
 */
const MobileEditorHeader = ({
    title = 'Restoration',
    onBack,
    onExport,
    className
}) => {
    const { processedImage, isProcessing } = useImage();

    // Only show download if we have a result and are not currently processing
    const showDownload = !!processedImage && !isProcessing;

    return (
        <motion.header
            initial={{ y: -60 }}
            animate={{ y: 0 }}
            transition={{ type: 'spring', stiffness: 400, damping: 30 }}
            className={cn(
                "fixed top-0 left-0 right-0 z-50",
                "ios-pro-header",
                className
            )}
        >
            {/* Safe area spacer for notch */}
            <div className="h-safe-top" />

            {/* Header content - thinner, more refined */}
            <div className="flex items-center justify-between h-11 px-4">
                {/* Left: Minimal iOS Back button */}
                <motion.button
                    onClick={onBack}
                    whileTap={{ scale: 0.95 }}
                    className="flex items-center gap-0.5 -ml-1 text-[rgb(var(--ios-accent))] active:opacity-60 transition-opacity"
                >
                    <ChevronLeft size={28} strokeWidth={2.5} className="-mr-1" />
                    <span className="text-[17px] font-normal">Back</span>
                </motion.button>

                {/* Center: Title - perfectly centered */}
                <h1 className="absolute left-1/2 -translate-x-1/2 text-[17px] font-semibold text-[rgb(var(--ios-label))] tracking-[-0.2px]">
                    {title}
                </h1>

                {/* Right: Download - lighter, secondary */}
                {/* Right: Download - Appearing only when result is ready */}
                <AnimatePresence>
                    {showDownload && onExport && (
                        <motion.button
                            initial={{ opacity: 0, scale: 0.8, x: 10 }}
                            animate={{ opacity: 1, scale: 1, x: 0 }}
                            exit={{ opacity: 0, scale: 0.8 }}
                            transition={{ type: 'spring', stiffness: 400, damping: 25 }}
                            onClick={onExport}
                            whileTap={{ scale: 0.9 }}
                            className="p-1.5 -mr-1.5 active:opacity-60 transition-opacity"
                            aria-label="Export"
                        >
                            <Download
                                size={22}
                                strokeWidth={2}
                                className="text-[rgb(var(--ios-accent))]"
                            />
                        </motion.button>
                    )}
                </AnimatePresence>
            </div>
        </motion.header>
    );
};

export default MobileEditorHeader;
