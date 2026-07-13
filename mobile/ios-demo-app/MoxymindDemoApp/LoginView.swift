import SwiftUI

struct LoginView: View {
    private let validateCredentials: (String, String) -> Bool
    private let onAuthenticated: () -> Void

    @State private var username = ""
    @State private var password = ""
    @State private var errorMessage = ""

    init(
        validateCredentials: @escaping (String, String) -> Bool,
        onAuthenticated: @escaping () -> Void
    ) {
        self.validateCredentials = validateCredentials
        self.onAuthenticated = onAuthenticated
    }

    var body: some View {
        ZStack {
            AppTheme.loginBackground
                .ignoresSafeArea()

            VStack(alignment: .leading, spacing: 22) {
                Spacer(minLength: 32)

                VStack(alignment: .leading, spacing: 14) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 18, style: .continuous)
                            .fill(AppTheme.brandGradient)
                            .frame(width: 56, height: 56)

                        Image(systemName: "bolt.fill")
                            .font(.title2.weight(.bold))
                            .foregroundStyle(.white)
                    }
                    .accessibilityHidden(true)

                    Text("Moxymind Demo")
                        .font(.largeTitle.weight(.semibold))
                        .foregroundStyle(AppTheme.ink)
                        .accessibilityIdentifier("login.title")

                    Text("Mobile automation")
                        .font(.headline.weight(.medium))
                        .foregroundStyle(AppTheme.accent)
                }

                VStack(spacing: 14) {
                    // Fixed local credentials must not trigger a system Password AutoFill sheet.
                    TextField("Username", text: $username)
                        .textContentType(.oneTimeCode)
                        .textInputAutocapitalization(.never)
                        .autocorrectionDisabled()
                        .submitLabel(.next)
                        .textFieldStyle(.plain)
                        .padding(.horizontal, 14)
                        .frame(height: 50)
                        .background(.white, in: RoundedRectangle(cornerRadius: 8, style: .continuous))
                        .overlay {
                            RoundedRectangle(cornerRadius: 8, style: .continuous)
                                .stroke(AppTheme.line)
                        }
                        .accessibilityIdentifier("login.username")

                    SecureField("Password", text: $password)
                        .textContentType(.oneTimeCode)
                        .submitLabel(.go)
                        .textFieldStyle(.plain)
                        .padding(.horizontal, 14)
                        .frame(height: 50)
                        .background(.white, in: RoundedRectangle(cornerRadius: 8, style: .continuous))
                        .overlay {
                            RoundedRectangle(cornerRadius: 8, style: .continuous)
                                .stroke(AppTheme.line)
                        }
                        .accessibilityIdentifier("login.password")
                        .onSubmit(submit)
                }

                Button(action: submit) {
                    Text("Login")
                        .font(.headline.weight(.semibold))
                        .foregroundStyle(.white)
                        .frame(maxWidth: .infinity, minHeight: 50)
                        .background(AppTheme.brandGradient, in: RoundedRectangle(cornerRadius: 8, style: .continuous))
                }
                .buttonStyle(.plain)
                .accessibilityIdentifier("login.submit")

                Text(errorMessage)
                    .font(.callout.weight(.medium))
                    .foregroundStyle(AppTheme.accentDeep)
                    .frame(minHeight: 24, alignment: .leading)
                    .accessibilityIdentifier("login.error")

                Spacer()
            }
            .padding(24)
            .frame(maxWidth: 520)
        }
    }

    private func submit() {
        if validateCredentials(username, password) {
            errorMessage = ""
            onAuthenticated()
        } else {
            errorMessage = "Invalid username or password"
        }
    }
}

#Preview("Login View") {
    LoginView { username, password in
        DemoCredentials.isValid(username: username, password: password)
    } onAuthenticated: {}
}
