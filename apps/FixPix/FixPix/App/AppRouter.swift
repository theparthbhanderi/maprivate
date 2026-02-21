//
//  AppRouter.swift
//  FixPix
//

import SwiftUI
import Combine

enum AppRoute: Hashable {
    case home
    case upload
    case editor(EditProject)
    case result(FixPixImage)
    case profile
    case settings
}

final class AppRouter: ObservableObject {
    @Published var path = NavigationPath()
    
    func navigate(to route: AppRoute) {
        path.append(route)
    }
    
    func popToRoot() {
        path.removeLast(path.count)
    }
    
    func goBack() {
        if !path.isEmpty {
            path.removeLast()
        }
    }
}
