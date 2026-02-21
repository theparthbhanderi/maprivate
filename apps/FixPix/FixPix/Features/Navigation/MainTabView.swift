//
//  MainTabView.swift
//  FixPix
//
//  Created by Antigravity on 2026-01-21.
//

import SwiftUI

struct MainTabView: View {
    @StateObject private var tabManager = TabManager()
    
    // Each tab has its own router stack
    @StateObject private var homeRouter = AppRouter()
    @StateObject private var uploadRouter = AppRouter()
    @StateObject private var editorRouter = AppRouter()
    @StateObject private var projectsRouter = AppRouter()
    @StateObject private var profileRouter = AppRouter()
    
    // Global state
    @StateObject private var editorProject: EditProject?
    
    var body: some View {
        ZStack(alignment: .bottom) {
            
            // Tab Content
            Group {
                switch tabManager.selectedTab {
                case .home:
                    NavigationStack(path: $homeRouter.path) {
                        HomeView()
                            .environmentObject(homeRouter)
                    }
                case .upload:
                    NavigationStack(path: $uploadRouter.path) {
                        UploadView()
                            .environmentObject(uploadRouter)
                    }
                case .editor:
                    // Main Editor Flow
                     NavigationStack(path: $editorRouter.path) {
                         // If no project, show Upload/Empty state or redirect
                         if let project = editorProject {
                             EditorView(project: project)
                                 .environmentObject(editorRouter)
                         } else {
                             UploadView() // fallback
                                 .environmentObject(editorRouter)
                         }
                    }
                case .projects:
                    NavigationStack(path: $projectsRouter.path) {
                        ProjectsListView()
                            .environmentObject(projectsRouter)
                    }
                case .profile:
                     NavigationStack(path: $profileRouter.path) {
                        ProfileView()
                             .environmentObject(profileRouter)
                    }
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            
            // Custom Tab Bar
            FixPixTabBar(selectedTab: $tabManager.selectedTab)
                .padding(.bottom, 20)
        }
        .ignoresSafeArea(.keyboard)
        .environmentObject(tabManager) 
    }
}

#Preview {
    MainTabView()
        .preferredColorScheme(.dark)
}
