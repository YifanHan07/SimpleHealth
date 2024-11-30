package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SignupView extends JPanel implements PropertyChangeListener {

    private final String viewName = "Sign up";
    private final SignupViewModel signupViewModel;
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);

    private SignupController signupController;

    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        this.signupViewModel.addPropertyChangeListener(this);

        // Set layout for vertical centering
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Compact spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel(SignupViewModel.TITLE_LABEL, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18)); // Optional styling for the title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(title, gbc);

        // Username field
        addLabelAndField(SignupViewModel.USERNAME_LABEL, usernameInputField, gbc, 1);

        // Password field
        addLabelAndField(SignupViewModel.PASSWORD_LABEL, passwordInputField, gbc, 2);
        addLabelAndField(SignupViewModel.REPEAT_PASSWORD_LABEL, repeatPasswordInputField, gbc, 3);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton toLogin = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        JButton signUp = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        buttonPanel.add(toLogin);
        buttonPanel.add(signUp);

        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(buttonPanel, gbc);

        // Event listeners
        addDocumentListener(usernameInputField, text -> {
            SignupState state = signupViewModel.getState();
            state.setUsername(text);
            signupViewModel.setState(state);
        });

        addDocumentListener(passwordInputField, text -> {
            SignupState state = signupViewModel.getState();
            state.setPassword(text);
            signupViewModel.setState(state);
        });

        addDocumentListener(repeatPasswordInputField, text -> {
            SignupState state = signupViewModel.getState();
            state.setRepeatPassword(text);
            signupViewModel.setState(state);
        });

        signUp.addActionListener(e -> {
            SignupState currentState = signupViewModel.getState();
            signupController.execute(
                    currentState.getUsername(),
                    currentState.getPassword(),
                    currentState.getRepeatPassword(),
                    "", ""
            );
        });

        toLogin.addActionListener(e -> signupController.switchToLoginView());
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

    private void addDocumentListener(JTextField field, DocumentUpdateListener listener) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                listener.update(field.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                listener.update(field.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                listener.update(field.getText());
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SignupState state = (SignupState) evt.getNewValue();
        usernameInputField.setText("");
        passwordInputField.setText("");
        repeatPasswordInputField.setText("");
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }

    @FunctionalInterface
    private interface DocumentUpdateListener {
        void update(String text);
    }
}
