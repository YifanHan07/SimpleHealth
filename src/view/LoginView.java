package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginView extends JPanel implements PropertyChangeListener {

    private final String viewName = "Log in";
    private final LoginViewModel loginViewModel;
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel usernameErrorField = new JLabel();
    private LoginController loginController;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        // Set main layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Compact spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18)); // Optional: Title styling
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both columns
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(title, gbc);

        // Username
        addLabelAndField("Username", usernameInputField, gbc, 1);

        // Username Error
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(usernameErrorField, gbc);

        // Password
        addLabelAndField("Password", passwordInputField, gbc, 3);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton signUp = new JButton("Sign Up");
        JButton logIn = new JButton("Log In");
        buttonsPanel.add(signUp);
        buttonsPanel.add(logIn);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(buttonsPanel, gbc);

        // Event listeners
        usernameInputField.getDocument().addDocumentListener(createDocumentListener(usernameInputField, true));
        passwordInputField.getDocument().addDocumentListener(createDocumentListener(passwordInputField, false));

        logIn.addActionListener(e -> {
            LoginState currentState = loginViewModel.getState();
            loginController.execute(currentState.getUsername(), currentState.getPassword());
        });

        signUp.addActionListener(e -> loginController.switchToSignupView());
    }

    private void addLabelAndField(String labelText, JComponent inputField, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        this.add(inputField, gbc);
    }

    private DocumentListener createDocumentListener(JTextField field, boolean isUsername) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateState();
            }

            private void updateState() {
                LoginState state = loginViewModel.getState();
                if (isUsername) {
                    state.setUsername(field.getText());
                } else {
                    state.setPassword(new String(((JPasswordField) field).getPassword()));
                }
                loginViewModel.setState(state);
            }
        };
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LoginState state = (LoginState) evt.getNewValue();
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
        usernameErrorField.setText(state.getLoginError());
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public String getViewName() {
        return viewName;
    }
}
