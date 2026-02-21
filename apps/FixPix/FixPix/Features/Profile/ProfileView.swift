//
//  ProfileView.swift
//  FixPix
//
//

import SwiftUI

struct ProfileView: View {
    @StateObject private var viewModel = ProfileViewModel()
    @EnvironmentObject var router: AppRouter
    
    // Grid Layout Column config
    private let columns = [
        GridItem(.flexible(), spacing: 16),
        GridItem(.flexible(), spacing: 16)
    ]
    
    var body: some View {
        ZStack {
            FixPixColors.background.ignoresSafeArea()
            
            ScrollView {
                VStack(spacing: 24) {
                    
                    // 1. User Header
                    if let user = viewModel.user {
                        HStack(spacing: 16) {
                            // Avatar
                            Circle()
                                .fill(FixPixColors.fixpixBrandBlue)
                                .frame(width: 80, height: 80)
                                .overlay(
                                    Text(String(user.name.prefix(1)))
                                        .font(.title)
                                        .fontWeight(.bold)
                                        .foregroundColor(.white)
                                )
                                .shadow(radius: 5)
                            
                            VStack(alignment: .leading, spacing: 4) {
                                Text(user.name)
                                    .font(FixPixFonts.h2)
                                    .foregroundColor(FixPixColors.text)
                                
                                Text(user.email)
                                    .font(FixPixFonts.body)
                                    .foregroundColor(FixPixColors.secondaryText)
                                
                                if user.isPremium {
                                    HStack(spacing: 4) {
                                        Image(systemName: "crown.fill")
                                            .foregroundColor(.yellow)
                                        Text("Premium Member")
                                            .font(.caption.weight(.semibold))
                                            .foregroundColor(.yellow)
                                    }
                                    .padding(.top, 4)
                                }
                            }
                            
                            Spacer()
                            
                            Button(action: { router.navigate(to: .settings) }) {
                                Image(systemName: "gearshape")
                                    .font(.title3)
                                    .foregroundColor(FixPixColors.text)
                                    .padding(10)
                                    .background(Color.white.opacity(0.1))
                                    .clipShape(Circle())
                            }
                        }
                        .padding(.horizontal)
                        .padding(.top, 20)
                    } else {
                        // Skeleton implementation could go here
                        HStack { Spacer(); ProgressView(); Spacer() }
                            .padding(.top, 50)
                    }
                    
                    // 2. Account Overview Card
                    VStack(spacing: 16) {
                        HStack {
                            VStack(alignment: .leading) {
                                Text("Current Plan")
                                    .font(.caption)
                                    .foregroundColor(.gray)
                                Text("FixPix Pro")
                                    .font(.headline)
                            }
                            Spacer()
                            Button("Manage") { }
                                .font(.caption.weight(.medium))
                                .padding(.horizontal, 12)
                                .padding(.vertical, 6)
                                .background(Color.white.opacity(0.1))
                                .cornerRadius(20)
                        }
                        
                        Divider().background(Color.white.opacity(0.1))
                        
                        HStack {
                            StatItem(value: "\(viewModel.projects.count)", label: "Projects")
                            Spacer()
                            StatItem(value: "148", label: "Edits")
                            Spacer()
                            StatItem(value: "2.4GB", label: "Storage")
                        }
                    }
                    .padding(20)
                    .background(FixPixColors.cardBackground)
                    .cornerRadius(16)
                    .padding(.horizontal)
                    
                    
                    // 3. Projects Section
                    VStack(alignment: .leading, spacing: 16) {
                        HStack {
                            Text("Recent Projects")
                                .font(FixPixFonts.h3)
                            Spacer()
                            Button("See All") { }
                                .font(FixPixFonts.button())
                                .foregroundColor(FixPixColors.fixpixBrandBlue)
                        }
                        .padding(.horizontal)
                        
                        if viewModel.projects.isEmpty {
                            EmptyStateView(
                                icon: "photo.on.rectangle.angled",
                                title: "No Projects Yet",
                                message: "Start your first AI edit to see it here.",
                                actionTitle: "Create New Project",
                                action: { router.navigate(to: .upload) }
                            )
                            .padding(.top, 20)
                        } else {
                            LazyVGrid(columns: columns, spacing: 16) {
                                ForEach(viewModel.projects) { project in
                                    ProjectCardView(project: project) {
                                        router.navigate(to: .editor(project))
                                    }
                                }
                            }
                            .padding(.horizontal)
                        }
                    }
                    
                    // 4. Support / Other
                    VStack(spacing: 0) {
                        MenuRow(icon: "questionmark.circle", title: "Help & Support")
                        Divider().padding(.leading, 50).background(Color.white.opacity(0.05))
                        MenuRow(icon: "bubble.left.and.bubble.right", title: "Send Feedback")
                        Divider().padding(.leading, 50).background(Color.white.opacity(0.05))
                        MenuRow(icon: "rectangle.portrait.and.arrow.right", title: "Sign Out", isDestructive: true)
                    }
                    .background(FixPixColors.cardBackground)
                    .cornerRadius(16)
                    .padding(.horizontal)
                    .padding(.bottom, 40)
                    
                }
            }
        }
        .navigationBarHidden(true)
    }
}

struct StatItem: View {
    let value: String
    let label: String
    
    var body: some View {
        VStack(spacing: 4) {
            Text(value)
                .font(.title3.bold())
                .foregroundColor(FixPixColors.text)
            Text(label)
                .font(.caption)
                .foregroundColor(FixPixColors.secondaryText)
        }
        .frame(minWidth: 60)
    }
}

struct MenuRow: View {
    let icon: String
    let title: String
    var isDestructive: Bool = false
    
    var body: some View {
        Button(action: {}) {
            HStack(spacing: 16) {
                Image(systemName: icon)
                    .font(.system(size: 20))
                    .foregroundColor(isDestructive ? .red : FixPixColors.text)
                    .frame(width: 24)
                
                Text(title)
                    .font(FixPixFonts.body)
                    .foregroundColor(isDestructive ? .red : FixPixColors.text)
                
                Spacer()
                
                Image(systemName: "chevron.right")
                    .font(.system(size: 14))
                    .foregroundColor(FixPixColors.secondaryText)
            }
            .padding()
        }
    }
}

#Preview {
    ProfileView()
        .environmentObject(AppRouter())
        .preferredColorScheme(.dark)
}
