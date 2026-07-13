enum DemoCredentials {
    static let validUsername = "qa_user"
    static let validPassword = "password123"

    static func isValid(username: String, password: String) -> Bool {
        username == validUsername && password == validPassword
    }
}
