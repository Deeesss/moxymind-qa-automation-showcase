import SwiftUI

struct TaskDetailView: View {
    let task: TaskItem

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text(task.title)
                    .font(.largeTitle.weight(.semibold))
                    .foregroundStyle(AppTheme.ink)
                    .accessibilityIdentifier("taskDetail.title")

                Text("Automation coverage item")
                    .font(.body)
                    .foregroundStyle(AppTheme.mutedInk)
                    .accessibilityIdentifier("taskDetail.description")

                VStack(alignment: .leading, spacing: 18) {
                    detailSection("Assignment", text: task.assignment)
                    Divider()
                    detailSection("Why this approach", text: task.decision)
                    Divider()
                    detailSection("How it was built", text: task.implementation)
                    Divider()
                    stepsSection
                    Divider()
                    detailSection("What the result proves", text: task.proof)
                }
                .padding(18)
                .background(
                    RoundedRectangle(cornerRadius: 8, style: .continuous)
                        .fill(AppTheme.card)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 8, style: .continuous)
                        .stroke(AppTheme.line)
                )
            }
            .frame(maxWidth: 520, alignment: .leading)
            .padding(24)
        }
        .background(AppTheme.warmSurface.ignoresSafeArea())
        .navigationTitle(task.title)
        .navigationBarTitleDisplayMode(.inline)
    }

    private func detailSection(_ heading: String, text: String) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(heading)
                .font(.headline)
                .foregroundStyle(AppTheme.ink)

            Text(text)
                .font(.body)
                .foregroundStyle(AppTheme.mutedInk)
                .fixedSize(horizontal: false, vertical: true)
        }
    }

    private var stepsSection: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Test flow")
                .font(.headline)
                .foregroundStyle(AppTheme.ink)

            ForEach(Array(task.steps.enumerated()), id: \.offset) { index, step in
                HStack(alignment: .top, spacing: 10) {
                    Text("\(index + 1)")
                        .font(.caption.weight(.bold))
                        .foregroundStyle(.white)
                        .frame(width: 22, height: 22)
                        .background(Circle().fill(AppTheme.accent))

                    Text(step)
                        .font(.body)
                        .foregroundStyle(AppTheme.mutedInk)
                        .fixedSize(horizontal: false, vertical: true)
                }
            }
        }
    }
}

#Preview("Task Detail") {
    NavigationStack {
        TaskDetailView(task: TaskItem.catalog[2])
    }
}
