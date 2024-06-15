import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Contact implements Serializable {
    private String name;
    private String phone;
    private String email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phone + ", Email: " + email;
    }
}

public class ContactManagerAWT extends Frame implements ActionListener {
    private List<Contact> contacts;
    private static final String FILE_NAME = "contacts.dat";

    private java.awt.List contactList;
    private TextField nameField, phoneField, emailField;
    private Button addButton, editButton, deleteButton, saveButton;

    public ContactManagerAWT() {
        contacts = new ArrayList<>();
        loadContactsFromFile();

        setLayout(new FlowLayout());
        setTitle("Contact Manager");
        setSize(400, 300);

        contactList = new java.awt.List(10);
        for (Contact contact : contacts) {
            contactList.add(contact.toString());
        }

        Label nameLabel = new Label("Name:");
        nameField = new TextField(30);
        Label phoneLabel = new Label("Phone:");
        phoneField = new TextField(30);
        Label emailLabel = new Label("Email:");
        emailField = new TextField(30);

        addButton = new Button("Add Contact");
        editButton = new Button("Edit Contact");
        deleteButton = new Button("Delete Contact");
        saveButton = new Button("Save Contacts");

        add(contactList);
        add(nameLabel);
        add(nameField);
        add(phoneLabel);
        add(phoneField);
        add(emailLabel);
        add(emailField);
        add(addButton);
        add(editButton);
        add(deleteButton);
        add(saveButton);

        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        saveButton.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                saveContactsToFile();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        switch (command) {
            case "Add Contact":
                addContact();
                break;
            case "Edit Contact":
                editContact();
                break;
            case "Delete Contact":
                deleteContact();
                break;
            case "Save Contacts":
                saveContactsToFile();
                break;
        }
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        Contact contact = new Contact(name, phone, email);
        contacts.add(contact);
        contactList.add(contact.toString());

        clearFields();
    }

    private void editContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            Contact contact = contacts.get(selectedIndex);
            contact.setName(name);
            contact.setPhone(phone);
            contact.setEmail(email);

            contactList.replaceItem(contact.toString(), selectedIndex);
            clearFields();
        } else {
            showMessage("Please select a contact to edit.");
        }
    }

    private void deleteContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex >= 0) {
            contacts.remove(selectedIndex);
            contactList.remove(selectedIndex);
            clearFields();
        } else {
            showMessage("Please select a contact to delete.");
        }
    }

    private void saveContactsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
            showMessage("Contacts saved successfully.");
        } catch (IOException e) {
            showMessage("Error saving contacts to file.");
        }
    }

    private void loadContactsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            contacts = (List<Contact>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous contacts found.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.setVisible(false));
        dialog.add(okButton);
        dialog.setSize(200, 100);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new ContactManagerAWT();
    }
}
