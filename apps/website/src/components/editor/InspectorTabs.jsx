import React from 'react';
import { motion } from 'framer-motion';
import { Wand2, Sparkles, Palette, SlidersHorizontal } from 'lucide-react';

/**
 * InspectorTabs — Micro-polished
 *
 * Tab height: 48px
 * Icon: 18px
 * Label: 11px uppercase
 * Active: accent underline 2px, opacity 100
 * Inactive: opacity 60
 * Gap: 24px (implicit via equal flex)
 */

const tabs = [
    { id: 'restore', label: 'Restore', icon: Wand2 },
    { id: 'enhance', label: 'Enhance', icon: Sparkles },
    { id: 'create', label: 'Create', icon: Palette },
    { id: 'adjust', label: 'Adjust', icon: SlidersHorizontal },
];

const InspectorTabs = ({ activeTab, onTabChange, collapsed = false }) => {
    const activeIndex = tabs.findIndex(t => t.id === activeTab);
    const tabWidth = 100 / tabs.length;

    return (
        <div style={{
            display: 'flex',
            position: 'relative',
            height: 48,
            borderBottom: '1px solid var(--glass-border)',
            paddingBottom: 0,
        }}>
            {tabs.map((tab) => {
                const Icon = tab.icon;
                const isActive = activeTab === tab.id;

                return (
                    <motion.button
                        key={tab.id}
                        onClick={() => onTabChange(tab.id)}
                        whileTap={{ scale: 0.97 }}
                        style={{
                            flex: 1,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            justifyContent: 'center',
                            gap: 4,
                            background: 'none',
                            border: 'none',
                            cursor: 'pointer',
                            opacity: isActive ? 1 : 0.6,
                            color: isActive ? '#007AFF' : 'inherit',
                            transition: 'opacity 150ms ease',
                        }}
                    >
                        <Icon size={18} strokeWidth={isActive ? 2 : 1.75} />
                        {!collapsed && (
                            <span style={{
                                fontSize: 11,
                                fontWeight: 600,
                                textTransform: 'uppercase',
                                letterSpacing: '0.5px',
                            }}>
                                {tab.label}
                            </span>
                        )}
                    </motion.button>
                );
            })}

            {/* Active underline — 2px accent */}
            <motion.div
                style={{
                    position: 'absolute',
                    bottom: 0,
                    height: 2,
                    width: `${tabWidth}%`,
                    borderRadius: 1,
                    backgroundColor: '#007AFF',
                }}
                animate={{ x: `${activeIndex * 100}%` }}
                transition={{ type: 'spring', stiffness: 500, damping: 35 }}
            />
        </div>
    );
};

export default InspectorTabs;
