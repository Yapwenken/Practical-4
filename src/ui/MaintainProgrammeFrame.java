package ui;

import control.MaintainProgrammeControl;
import domain.Programme;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MaintainProgrammeFrame extends JFrame {

    private MaintainProgrammeControl progControl;
    private JTextField jtfCode = new JTextField();
    private JTextField jtfName = new JTextField();
    private JTextField jtfFaculty = new JTextField();
    private JButton jbtAdd = new JButton("Create");
    private JButton jbtRetrieve = new JButton("Retrieve");
    private JButton jbtUpdate = new JButton("Update");
    private JButton jbtDelete = new JButton("Delete");

    public MaintainProgrammeFrame() {
        progControl = new MaintainProgrammeControl();
        
        JPanel jpCenter = new JPanel(new GridLayout(3, 2));
        jpCenter.add(new JLabel("Programme Code"));
        jpCenter.add(jtfCode);
        jpCenter.add(new JLabel("Programme Name"));
        jpCenter.add(jtfName);
        jpCenter.add(new JLabel("Faculty"));
        jpCenter.add(jtfFaculty);
        add(jpCenter);

        JPanel jpSouth = new JPanel();
        jpSouth.add(jbtAdd);
        jpSouth.add(jbtRetrieve);
        jpSouth.add(jbtUpdate);
        jpSouth.add(jbtDelete);
        add(jpSouth, BorderLayout.SOUTH);

        jbtRetrieve.addActionListener(new RetrieveListener());
        jbtAdd.addActionListener(new AddListener());
        jbtUpdate.addActionListener(new UpdateListener());
        jbtDelete.addActionListener(new DeleteListener());
    }

    private class RetrieveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String code = jtfCode.getText();
            Programme programme = progControl.selectRecord(code);
            if (programme != null) {
                jtfName.setText(programme.getName());
                jtfFaculty.setText(programme.getFaculty());
            } else {
                JOptionPane.showMessageDialog(null, "No such programme code.", "RECORD NOT FOUND", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class AddListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String code = jtfCode.getText();
            
            //Check if programme already exists in DB
            Programme p = progControl.selectRecord(code);
            
            //Do not execute INSERT statement if programme already exists
            if(p!= null){
                JOptionPane.showMessageDialog(null, "This programme code already exists.", "DUPLICATE RECORD",
                        JOptionPane.WARNING_MESSAGE);
            }else{
                //Execute INSERT statemnet if programme DOES NOT exists
                //in DB
                p = new Programme(code, jtfName.getText(), jtfFaculty.getText());
                int result = progControl.addRecord(p);
                if(result > 0){
                    JOptionPane.showMessageDialog(null, result + "row(s) added into DB.", "INSERT SUCCESSFUL", 
                            JOptionPane.INFORMATION_MESSAGE);
                    clearText();
                }else{
                    JOptionPane.showMessageDialog(null, "No row added into DB.", "INSERT FAILED", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
      private class UpdateListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String code = jtfCode.getText();
            
            //Update only if Programme EXISTS IN DB!
            Programme p = progControl.selectRecord(code);
            
            if(p!= null){
                p.setName(jtfName.getText());
                p.setFaculty(jtfFaculty.getText());
                
                int result = progControl.updateRecord(p);
                
                if(result > 0){
                     JOptionPane.showMessageDialog(null, result + "row(s) updated into DB.", "UPDATE SUCCESSFUL", 
                            JOptionPane.INFORMATION_MESSAGE);
                    clearText();
                }else{
                    //Display Error Message if Programme DOES NOT exists in DB
                    JOptionPane.showMessageDialog(null, "No row added into DB.", "UPDATE FAILED", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }else{
                 JOptionPane.showMessageDialog(null, "No such programme code.", "DUPLICATE RECORD",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
      }
      
      private class DeleteListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String code = jtfCode.getText();
            
            //Check if programme exists in the Database
            Programme p = progControl.selectRecord(code);
            
             if(p!= null){
                 jtfName.setText(p.getName());
                 jtfFaculty.setText(p.getFaculty());
                 
                 int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete?");
                 if(option == JOptionPane.YES_OPTION){
                     int result = progControl.deleteRecord(code);
                     JOptionPane.showMessageDialog(null, result + "row(s) deleted into DB.", "DELETE SUCCESSFUL", 
                            JOptionPane.INFORMATION_MESSAGE);
                     
                     clearText();
                 }else{
                     JOptionPane.showMessageDialog(null, "No such programme code.", "RECORD NOT FOUND",
                        JOptionPane.WARNING_MESSAGE);
                 }
             }
        }
      }
    
    private void clearText(){
        jtfCode.setText("");
        jtfName.setText("");
        jtfFaculty.setText("");
        jtfCode.requestFocusInWindow();
    }

    public static void main(String[] args) {
          MaintainProgrammeFrame frm=new MaintainProgrammeFrame();   
          frm.setTitle("Programme CRUD");
          frm.setSize(600, 200);
          frm.setLocationRelativeTo(null);
          frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frm.setVisible(true);
    }
}
