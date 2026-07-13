import SwiftUI

struct TasksView: View {
    let tasks: [TaskItem]
    let onSelectTask: (TaskItem) -> Void
    let onLogout: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            HStack(alignment: .center) {
                Text("Tasks")
                    .font(.largeTitle.weight(.semibold))
                    .foregroundStyle(AppTheme.ink)
                    .accessibilityIdentifier("tasks.title")

                Spacer()

                Button("Logout", action: onLogout)
                    .buttonStyle(.bordered)
                    .tint(AppTheme.accent)
                    .accessibilityIdentifier("tasks.logout")
            }

            VStack(spacing: 0) {
                ForEach(tasks) { task in
                    Button {
                        onSelectTask(task)
                    } label: {
                        HStack {
                            Text(task.title)
                                .font(.body)
                                .foregroundStyle(AppTheme.ink)

                            Spacer()

                            Image(systemName: "chevron.right")
                                .font(.footnote.weight(.semibold))
                                .foregroundStyle(.secondary)
                        }
                        .frame(minHeight: 52)
                        .contentShape(Rectangle())
                    }
                    .buttonStyle(.plain)
                    .accessibilityIdentifier(task.accessibilityIdentifier)

                    if task.id != tasks.last?.id {
                        Divider()
                    }
                }
            }
            .padding(.horizontal, 16)
            .background(
                RoundedRectangle(cornerRadius: 8, style: .continuous)
                    .fill(AppTheme.card)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 8, style: .continuous)
                    .stroke(AppTheme.line)
            )

            Spacer()
        }
        .padding(24)
        .background(AppTheme.warmSurface.ignoresSafeArea())
        .navigationBarBackButtonHidden(true)
    }
}

#Preview("Tasks") {
    NavigationStack {
        TasksView(tasks: TaskItem.catalog, onSelectTask: { _ in }, onLogout: {})
    }
}
