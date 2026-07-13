import SwiftUI

enum AppTheme {
    static let accent = Color(red: 0.82, green: 0.18, blue: 0.14)
    static let accentDeep = Color(red: 0.52, green: 0.08, blue: 0.08)
    static let ink = Color(red: 0.12, green: 0.13, blue: 0.16)
    static let mutedInk = Color(red: 0.39, green: 0.42, blue: 0.48)
    static let line = Color.black.opacity(0.08)
    static let card = Color.white.opacity(0.92)
    static let warmSurface = Color(red: 1.0, green: 0.97, blue: 0.95)

    static let loginBackground = LinearGradient(
        colors: [
            Color(red: 1.0, green: 0.96, blue: 0.94),
            Color(red: 0.98, green: 0.99, blue: 1.0)
        ],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
    )

    static let brandGradient = LinearGradient(
        colors: [
            accent,
            Color(red: 0.94, green: 0.38, blue: 0.28)
        ],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
    )
}
