package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class tagSelectionView extends JDialog {
    private final List<String> availableTags;
    private final List<String> selectedTags;
    private final TagSelectionListener listener;

    public tagSelectionView(Frame parent, List<String> availableTags, List<String> initialSelectedTags, TagSelectionListener listener) {
        super(parent, "Select Tags", true);
        this.availableTags = availableTags;
        this.selectedTags = new ArrayList<>(initialSelectedTags);
        this.listener = listener;
        initUI();
    }

    private void initUI() {
        JPanel checkboxPanel = new JPanel(new GridLayout(availableTags.size(), 1));
        JCheckBox[] checkBoxes = new JCheckBox[availableTags.size()];

        for (int i = 0; i < availableTags.size(); i++) {
            checkBoxes[i] = new JCheckBox(availableTags.get(i));
            checkBoxes[i].setSelected(selectedTags.contains(availableTags.get(i)));
            checkboxPanel.add(checkBoxes[i]);
        }

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            selectedTags.clear();
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    selectedTags.add(availableTags.get(i));
                }
            }
            listener.onTagsSelected(selectedTags);
            dispose();
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(checkboxPanel), BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public interface TagSelectionListener {
        void onTagsSelected(List<String> selectedTags);
    }
}