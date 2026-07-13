import SwiftUI

@MainActor
struct AppRootView: View {
    @State private var isAuthenticated = false
    @State private var navigationPath: [TaskItem] = []

    var body: some View {
        Group {
            if isAuthenticated {
                NavigationStack(path: $navigationPath) {
                    TasksView(
                        tasks: TaskItem.catalog,
                        onSelectTask: { task in
                            navigationPath.append(task)
                        },
                        onLogout: {
                            navigationPath.removeAll()
                            isAuthenticated = false
                        }
                    )
                    .navigationDestination(for: TaskItem.self) { task in
                        TaskDetailView(task: task)
                    }
                }
            } else {
                LoginView { username, password in
                    DemoCredentials.isValid(username: username, password: password)
                } onAuthenticated: {
                    navigationPath.removeAll()
                    isAuthenticated = true
                }
            }
        }
    }
}

#Preview("Login") {
    AppRootView()
}
