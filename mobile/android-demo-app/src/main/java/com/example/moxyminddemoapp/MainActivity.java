package com.example.moxyminddemoapp;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String VALID_USERNAME = "qa_user";
    private static final String VALID_PASSWORD = "password123";
    private static final TaskInfo[] TASK_CATALOG = {
        new TaskInfo(
            "API automation",
            "tasks.row.api",
            "Technical task #2 asks for REST API automation against ReqRes, specifically GET List Users and POST Create User. The result must run from the command line and be clear enough for a reviewer to reproduce.",
            "I made API the primary layer because it gives fast feedback on the service contract. Playwright's API client lets the API and frontend suites share one runner and one HTML report. A small ReqRes client centralizes authentication and headers, while data tables add useful coverage without creating a custom framework.",
            "TypeScript tests are separated from the ReqRes client, response assertions and test data. Authenticated cases read REQRES_API_KEY. A public missing-key test still runs without the secret, so reviewer CI remains useful instead of failing only because configuration is absent.",
            new String[] {
                "Request user pages 1 and 2 through the shared ReqRes client.",
                "Validate pagination fields and the shape of every returned user.",
                "Create two users from reusable name and job data rows.",
                "Validate the echoed data, generated ID and creation timestamp.",
                "Check both the missing API-key boundary and a user that does not exist."
            },
            "The suite proves the expected 200, 201, 401 and 404 behavior, including response fields and negative boundaries. It deliberately does not claim backend ownership, load testing or broad security coverage."
        ),
        new TaskInfo(
            "Frontend automation",
            "tasks.row.frontend",
            "Technical task #1 asks for three to four essential frontend scenarios against SauceDemo, runnable from the command line. The important part is to show why the selected behavior matters, not to automate the entire website.",
            "I chose the user-critical journey: authentication, product handling and checkout. These areas expose both successful behavior and useful validation failures. Six small tests keep each failure precise, while the overall scope still stays within those three business areas.",
            "Playwright drives Chromium and produces the same report format as the API suite. Page Objects own SauceDemo's data-test selectors and screen actions, so the tests read as user intent without adding a large abstraction layer. Credentials and the base URL remain configurable.",
            new String[] {
                "Log in as the standard user and confirm the inventory page opens.",
                "Try the locked user and verify the visible rejection message.",
                "Add and remove a product, then check the cart count.",
                "Sort products by ascending price and compare the displayed values.",
                "Exercise missing checkout data and complete a valid two-product order."
            },
            "The suite proves that the main shopping path works and that common mistakes are rejected visibly. Stable selectors, explicit assertions and isolated tests make failures readable for a reviewer."
        ),
        new TaskInfo(
            "Mobile automation",
            "tasks.row.mobile",
            "Technical task #3 asks for a simple mobile automation framework, at least two scenarios, command-line execution and an emulator or simulator. The application under test may be chosen freely, so the solution uses a small deterministic native demo instead of depending on an unstable third-party app.",
            "I chose Kotlin and Appium because one readable test suite can drive both Android and iOS while UiAutomator2 and XCUITest keep the platform interaction native. Three scenarios cover the required positive and negative behavior plus navigation without expanding into a real product.",
            "The same login, task list and detail flow exists in a native Android Activity and a SwiftUI iOS app. Screen Objects use shared accessibility IDs instead of coordinates or XPath. Environment-driven capabilities select the app artifact, device and platform, and every test starts a fresh driver session.",
            new String[] {
                "Launch a fresh app session and verify the branded login screen.",
                "Sign in with the fixed QA account and verify all three task rows.",
                "Start a clean session with invalid credentials and verify the exact error.",
                "Start another clean session, sign in and open Mobile automation detail.",
                "Run the same Kotlin scenarios on Android and iOS through Appium."
            },
            "The suite proves real app launch, stable accessibility selection, deterministic validation and equivalent core navigation on both platforms. The demo intentionally has no backend, network calls, database, permissions or analytics."
        )
    };

    private final int accentStart = Color.rgb(209, 46, 36);
    private final int accentEnd = Color.rgb(240, 96, 72);
    private final int loginBackgroundStart = Color.rgb(255, 245, 240);
    private final int loginBackgroundEnd = Color.rgb(250, 252, 255);
    private final int tasksBackground = Color.rgb(255, 247, 242);
    private final int darkText = Color.rgb(31, 33, 41);
    private final int subtleText = Color.rgb(91, 98, 112);
    private final int softBorder = Color.argb(20, 0, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLogin();
    }

    private void showLogin() {
        LinearLayout root = contentRoot();

        root.addView(brandIcon(), fixedParams(56, 56, 0, 0, 0, 18));

        TextView brand = text("Moxymind Demo", 30, darkText, true);
        brand.setContentDescription("login.title");
        root.addView(brand, wrapParams(0, 0, 0, 2));

        TextView subtitle = text("Mobile automation", 18, accentStart, true);
        root.addView(subtitle, wrapParams(0, 0, 0, 28));

        EditText username = input("Username", "login.username", false);
        root.addView(username, fixedParams(-1, 50, 0, 0, 0, 12));

        EditText password = input("Password", "login.password", true);
        root.addView(password, fixedParams(-1, 50, 0, 0, 0, 10));

        TextView error = text("", 14, Color.rgb(133, 13, 13), false);
        error.setContentDescription("login.error");
        error.setVisibility(View.GONE);
        root.addView(error, wrapParams(0, 2, 0, 10));

        Button submit = button("Login", "login.submit");
        submit.setOnClickListener(view -> {
            boolean valid = VALID_USERNAME.contentEquals(username.getText())
                && VALID_PASSWORD.contentEquals(password.getText());

            if (valid) {
                showTasks();
                return;
            }

            error.setText("Invalid username or password");
            error.setVisibility(View.VISIBLE);
        });
        root.addView(submit, fixedParams(-1, 50, 0, 0, 0, 0));

        setContentView(wrap(root, background(loginBackgroundStart, loginBackgroundEnd)));
    }

    private void showTasks() {
        LinearLayout root = contentRoot();

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = text("Tasks", 30, darkText, true);
        title.setContentDescription("tasks.title");
        header.addView(title, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button logout = secondaryButton("Logout", "tasks.logout");
        logout.setOnClickListener(view -> showLogin());
        header.addView(logout, fixedParams(96, 42, 0, 0, 0, 0));
        root.addView(header, wrapParams(0, 0, 0, 20));

        for (int index = 0; index < TASK_CATALOG.length; index++) {
            TaskInfo task = TASK_CATALOG[index];
            int bottomMargin = index == TASK_CATALOG.length - 1 ? 0 : 12;
            root.addView(
                taskRow(task, () -> showTaskDetail(task)),
                fixedParams(-1, 64, 0, 0, 0, bottomMargin)
            );
        }

        setContentView(wrap(root, solidBackground(tasksBackground)));
    }

    private void showTaskDetail(TaskInfo task) {
        LinearLayout root = contentRoot();

        Button back = secondaryButton("Back", "taskDetail.back");
        back.setOnClickListener(view -> showTasks());
        root.addView(back, fixedParams(96, 42, 0, 0, 0, 22));

        TextView title = text(task.title, 28, darkText, true);
        title.setContentDescription("taskDetail.title");
        root.addView(title, wrapParams(0, 0, 0, 6));

        TextView description = text("Automation coverage item", 18, subtleText, false);
        description.setContentDescription("taskDetail.description");
        root.addView(description, wrapParams(0, 0, 0, 18));

        addDetailSection(root, "Assignment", task.assignment, true);
        addDetailSection(root, "Why this approach", task.decision, true);
        addDetailSection(root, "How it was built", task.implementation, true);
        addDetailSection(root, "Test flow", numberedSteps(task.steps), true);
        addDetailSection(root, "What the result proves", task.proof, false);

        setContentView(wrap(root, solidBackground(tasksBackground)));
    }

    private LinearLayout taskRow(TaskInfo task, Runnable action) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setContentDescription(task.accessibilityId);
        row.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        row.setPadding(dp(18), 0, dp(14), 0);
        row.setBackground(rounded(Color.WHITE, 8, softBorder));
        row.setElevation(dp(1));
        row.setOnClickListener(view -> action.run());

        TextView titleView = text(task.title, 17, darkText, true);
        titleView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        row.addView(titleView, new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
        ));

        TextView chevron = text("›", 24, subtleText, false);
        chevron.setGravity(Gravity.CENTER);
        chevron.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        row.addView(chevron, new LinearLayout.LayoutParams(
            dp(24),
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        return row;
    }

    private void addDetailSection(LinearLayout parent, String heading, String body, boolean addDivider) {
        TextView headingView = text(heading, 17, darkText, true);
        parent.addView(headingView, wrapParams(0, 0, 0, 5));

        TextView bodyView = text(body, 15, subtleText, false);
        bodyView.setLineSpacing(dp(3), 1.0f);
        parent.addView(bodyView, wrapParams(0, 0, 0, addDivider ? 14 : 0));

        if (addDivider) {
            View divider = new View(this);
            divider.setBackgroundColor(softBorder);
            parent.addView(divider, fixedParams(-1, 1, 0, 0, 0, 14));
        }
    }

    private String numberedSteps(String[] steps) {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < steps.length; index++) {
            if (index > 0) {
                result.append("\n\n");
            }
            result.append(index + 1).append(". ").append(steps[index]);
        }
        return result.toString();
    }

    private LinearLayout contentRoot() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        return root;
    }

    private ScrollView wrap(LinearLayout root, GradientDrawable pageBackground) {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackground(pageBackground);

        FrameLayout frame = new FrameLayout(this);
        frame.setMinimumHeight(getResources().getDisplayMetrics().heightPixels);
        frame.setPadding(dp(24), dp(32), dp(24), dp(24));

        FrameLayout.LayoutParams rootParams = new FrameLayout.LayoutParams(
            contentWidth(),
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.TOP | Gravity.CENTER_HORIZONTAL
        );
        frame.addView(root, rootParams);

        scrollView.addView(frame, new ScrollView.LayoutParams(
            ScrollView.LayoutParams.MATCH_PARENT,
            ScrollView.LayoutParams.WRAP_CONTENT
        ));
        return scrollView;
    }

    private TextView text(String value, int sizeSp, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sizeSp);
        view.setTextColor(color);
        view.setIncludeFontPadding(true);
        if (bold) {
            view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return view;
    }

    private EditText input(String hint, String accessibilityId, boolean password) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setTextSize(16);
        input.setSingleLine(true);
        input.setContentDescription(accessibilityId);
        input.setGravity(Gravity.CENTER_VERTICAL);
        input.setPadding(dp(14), 0, dp(14), 0);
        input.setHintTextColor(Color.rgb(125, 130, 141));
        input.setTextColor(darkText);
        input.setBackground(rounded(Color.WHITE, 8, softBorder));
        input.setInputType(password
            ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
            : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        return input;
    }

    private Button button(String label, String accessibilityId) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(16);
        button.setContentDescription(accessibilityId);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setTextColor(Color.WHITE);
        button.setBackground(gradient(accentStart, accentEnd, 8));
        return button;
    }

    private Button secondaryButton(String label, String accessibilityId) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(14);
        button.setContentDescription(accessibilityId);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setTextColor(accentStart);
        button.setBackground(rounded(Color.argb(24, 209, 46, 36), 8, Color.argb(36, 209, 46, 36)));
        return button;
    }

    private FrameLayout brandIcon() {
        FrameLayout icon = new FrameLayout(this);
        icon.setBackground(gradient(accentStart, accentEnd, 18));
        icon.addView(new BoltView(this), new FrameLayout.LayoutParams(dp(28), dp(28), Gravity.CENTER));
        icon.setElevation(dp(2));
        return icon;
    }

    private GradientDrawable background(int startColor, int endColor) {
        return gradient(startColor, endColor, 0);
    }

    private GradientDrawable solidBackground(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        return drawable;
    }

    private GradientDrawable gradient(int startColor, int endColor, int radiusDp) {
        GradientDrawable drawable = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] { startColor, endColor }
        );
        drawable.setCornerRadius(dp(radiusDp));
        return drawable;
    }

    private GradientDrawable rounded(int color, int radiusDp, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        drawable.setStroke(dp(1), strokeColor);
        return drawable;
    }

    private int contentWidth() {
        int available = getResources().getDisplayMetrics().widthPixels - dp(48);
        return Math.min(dp(520), available);
    }

    private LinearLayout.LayoutParams wrapParams(int leftDp, int topDp, int rightDp, int bottomDp) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dp(leftDp), dp(topDp), dp(rightDp), dp(bottomDp));
        return params;
    }

    private LinearLayout.LayoutParams fixedParams(int widthDp, int heightDp, int leftDp, int topDp, int rightDp, int bottomDp) {
        int width = widthDp == -1 ? LinearLayout.LayoutParams.MATCH_PARENT : dp(widthDp);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, dp(heightDp));
        params.setMargins(dp(leftDp), dp(topDp), dp(rightDp), dp(bottomDp));
        return params;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private static final class BoltView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();

        BoltView(Activity activity) {
            super(activity);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float width = getWidth();
            float height = getHeight();

            path.reset();
            path.moveTo(width * 0.58f, height * 0.02f);
            path.lineTo(width * 0.18f, height * 0.56f);
            path.lineTo(width * 0.48f, height * 0.56f);
            path.lineTo(width * 0.36f, height * 0.98f);
            path.lineTo(width * 0.82f, height * 0.42f);
            path.lineTo(width * 0.52f, height * 0.42f);
            path.close();

            canvas.drawPath(path, paint);
        }
    }

    private static final class TaskInfo {
        private final String title;
        private final String accessibilityId;
        private final String assignment;
        private final String decision;
        private final String implementation;
        private final String[] steps;
        private final String proof;

        private TaskInfo(
            String title,
            String accessibilityId,
            String assignment,
            String decision,
            String implementation,
            String[] steps,
            String proof
        ) {
            this.title = title;
            this.accessibilityId = accessibilityId;
            this.assignment = assignment;
            this.decision = decision;
            this.implementation = implementation;
            this.steps = steps;
            this.proof = proof;
        }
    }
}
